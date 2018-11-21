from dataclasses import dataclass
from datetime import datetime, timedelta

from flask.json import JSONEncoder
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import and_, or_
from sqlalchemy.exc import IntegrityError, InvalidRequestError
from sqlalchemy.ext.hybrid import hybrid_property

from koob.utils import make_day_bounds, replace_date


db = SQLAlchemy()


class CRUDMixin(object):

    __table_args__ = {'extend_existing': True}  # type: Any

    id = db.Column(db.Integer, primary_key=True, autoincrement=True)

    @classmethod
    def create(cls, commit=True, ignore_if_exists=False, **kwargs):
        instance = cls(**kwargs)

        if hasattr(instance, 'created_at') \
                and getattr(instance, 'created_at') is None:
            instance.created_at = datetime.now()

        try:
            return instance.save(commit=commit)
        except (IntegrityError, InvalidRequestError):
            if ignore_if_exists:
                db.session.rollback()
                return cls.find(**kwargs)
            else:
                raise

    @classmethod
    def get(cls, id):
        return cls.query.get(id)

    # We will also proxy Flask-SqlAlchemy's get_or_404
    # for symmetry
    @classmethod
    def get_or_404(cls, id):
        return cls.query.get_or_404(id)

    @classmethod
    def find(cls, **kwargs):
        return cls.query.filter_by(**kwargs).first()

    @classmethod
    def exists(cls, **kwargs):
        row = cls.find(**kwargs)
        return row is not None

    def update(self, commit=True, **kwargs):
        for attr, value in kwargs.iteritems():
            setattr(self, attr, value)
        return commit and self.save() or self

    def save(self, commit=True):
        db.session.add(self)
        if commit:
            db.session.commit()
        return self

    def delete(self, commit=True):
        db.session.delete(self)
        return commit and db.session.commit()

    def __iter__(self):
        for column in self.__table__.columns:
            yield column.name, str(getattr(self, column.name))


class KoobJSONEncoder(JSONEncoder):

    def default(self, obj):
        if isinstance(obj, datetime):
            return obj.strftime('%Y-%m-%dT%H:%M')
        elif isinstance(obj, (Reservation, Resource, Recurrence)):
            return dict(obj)
        else:
            return super(JSONEncoder, self).default(obj)


class Resource(db.Model, CRUDMixin):

    __tablename__ = 'resources'

    title = db.Column(db.String)
    capacity = db.Column(db.Integer)


class Reservation(db.Model, CRUDMixin):

    __tablename__ = 'reservations'

    resource_id = db.Column(db.Integer, db.ForeignKey('resources.id'))
    created_at = db.Column(db.DateTime)
    starts_at_ = db.Column('starts_at', db.DateTime)
    ends_at_ = db.Column('ends_at', db.DateTime)
    is_recurring = db.Column(db.Boolean)
    reserved_by = db.Column(db.String)
    title = db.Column(db.String)
    description = db.Column(db.Text)
    recurrence = db.relationship(
        'Recurrence', uselist=False, back_populates='reservation')

    def __init__(self, *args, **kwargs):
        super(Reservation, self).__init__(*args, **kwargs)
        if self.is_recurring:
            self.recurring_date = self.starts_at
        else:
            self.recurring_date = None

    def register_as_recurring(self, frequency, count):
        if frequency is RecurringFrequency.weekly:
            recur = Recurrence.create(
                reservation_id=self.id,
                frequency=frequency,
                count=count,
                weekday=self.starts_at.weekday())
            return recur
        else:
            raise NotImplementedError

    def set_recurring_date(self, date):
        self.recurring_date = date
        return self

    @hybrid_property
    def starts_at(self):
        try:
            return replace_date(self.starts_at_, self.recurring_date)
        except AttributeError:
            return self.starts_at_

    @starts_at.setter
    def starts_at(self, value):
        self.starts_at_ = value

    @starts_at.expression
    def starts_at(cls):
        return cls.starts_at_

    @hybrid_property
    def ends_at(self):
        try:
            return replace_date(self.ends_at_, self.recurring_date)
        except AttributeError:
            return self.ends_at_

    @ends_at.setter
    def ends_at(self, value):
        self.ends_at_ = value

    @ends_at.expression
    def ends_at(cls):
        return cls.ends_at_

    @property
    def duration(self):
        """Duration of the reservation in terms of minutes."""
        pass

    @classmethod
    def create(cls, commit=True, ignore_if_exists=False, **kwargs):
        frequency = int(kwargs.pop(
            'recurring_frequency', RecurringFrequency.none))
        count = int(kwargs.pop('recurring_count', 0))

        overlappings = cls.find_overlappings(
            kwargs['resource_id'], kwargs['starts_at'], kwargs['ends_at'])
        if overlappings.count() > 0:
            raise ValueError('Overlapping reservation exists')

        if frequency is RecurringFrequency.none:
            kwargs['is_recurring'] = False
        elif frequency is RecurringFrequency.weekly:
            kwargs['is_recurring'] = True
        else:
            raise NotImplementedError

        obj = super(Reservation, cls) \
            .create(commit, ignore_if_exists, **kwargs)

        if obj.is_recurring:
            obj.register_as_recurring(frequency, count)

        return obj

    @classmethod
    def find_overlappings(cls, resource_id, starts_at, ends_at):
        return cls.query \
            .filter(cls.resource_id == resource_id) \
            .filter(cls.starts_at < ends_at) \
            .filter(cls.ends_at > starts_at)

    @classmethod
    def find_reservations_on(cls, date):
        lower, upper = make_day_bounds(date)
        reservations = cls.query.outerjoin(Recurrence).filter(or_(
            and_(
                cls.is_recurring == True,  # noqa (E712)
                Recurrence.weekday == date.weekday(),
                # NOTE: See Recurrence.ends_at()
                # Recurrence.ends_at >= lower
            ),
            and_(
                cls.is_recurring == False,
                or_(
                    cls.starts_at.between(lower, upper),
                    cls.ends_at.between(lower, upper)
                )
            )
        ))

        # NOTE: This may lead to poor performance. See Recurrence.ends_at() for
        # more detailed explanation.
        reservations = [r.set_recurring_date(date) for r in reservations.all()
                        if not r.is_recurring or r.recurrence.ends_at >= lower]

        # NOTE: Normally, we would like to return a query object here, however,
        # since we have eagerly evaluated the query above.  And therefore we
        # provide something that mimics the original query object for
        # compatability reasons.
        return EvaluatedQuery(reservations)


class Recurrence(db.Model, CRUDMixin):

    __tablename__ = 'recurrences'

    reservation_id = db.Column(db.Integer, db.ForeignKey('reservations.id'))
    reservation = db.relationship(
        'Reservation', uselist=False, foreign_keys=[reservation_id])
    # FIXME: We would like to use an enum type here, but SQLite does not suppor
    # enums out-of-box
    frequency = db.Column(db.Integer)
    count = db.Column(db.Integer)
    weekday = db.Column(db.Integer)

    @hybrid_property
    def ends_at(self):
        return self.reservation.ends_at_ + \
            timedelta(days=self.frequency * (self.count - 1))

    @ends_at.expression
    def ends_at(cls):
        # NOTE: Ideally, we would like to do something like this, however,
        # SQLite unfortunately does not support date/time operations based on
        # column values. So we will do this on the application code.
        return cls.reservation.ends_at_ + (cls.frequency * (cls.count - 1))


@dataclass
class RecurringFrequency:
    none = 0
    daily = NotImplemented
    weekly = 7
    monthly = NotImplemented
    annually = NotImplemented
    custom = NotImplemented


class EvaluatedQuery:

    def __init__(self, records):
        self.records = records

    def count(self):
        return len(self.records)

    def first(self):
        return self.records[0]

    def all(self):
        return self.records
