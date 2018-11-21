from wtforms import DateTimeField, Form, IntegerField, StringField
from wtforms.validators import DataRequired, Length, ValidationError


def valid_hours(form, field):
    if field.data.minute % 30 != 0:
        raise ValidationError(
            'Reservations can be made at 30-minute increments only')


class ReservationForm(Form):
    resource_id = IntegerField(validators=[DataRequired()])

    starts_at = DateTimeField(
        format='%Y-%m-%dT%H:%M', validators=[DataRequired(), valid_hours])

    ends_at = DateTimeField(
        format='%Y-%m-%dT%H:%M', validators=[DataRequired(), valid_hours])

    reserved_by = StringField(validators=[DataRequired()])

    title = StringField(
        validators=[DataRequired(), Length(max=255)])

    description = StringField(validators=[Length(max=1024 * 8)])

    # FIXME: Not sure why zero value causes validation errors
    recurring_frequency = IntegerField(validators=[])

    recurring_count = IntegerField(validators=[])

    def validate(self):
        validators = [
            super(Form, self).validate,
            self.validate_datetimes,
            self.validate_recurring_info,
        ]
        return all([v() for v in validators])

    def validate_datetimes(self):
        if self.starts_at.errors:
            return False
        if self.ends_at.errors:
            return False
        if self.starts_at.data >= self.ends_at.data:
            self.starts_at.errors.append(
                'The start datetime must be earlier than the end datetime')
            return False

        return True

    def validate_recurring_info(self):
        if self.recurring_frequency.errors:
            return False
        if self.recurring_frequency.data > 0:
            if self.recurring_count.errors:
                return False
            if self.recurring_count.data < 2:
                self.recurring_count.errors.append(
                    'Must be at least two')
                return False
        return True
