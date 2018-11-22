import pytest

from koob.models import RecurringFrequency, Reservation
from koob.utils import parse_date, parse_datetime


def create_reservation(
    resource_id=1,
    starts_at=parse_datetime('2018-11-21T15:00'),
    ends_at=parse_datetime('2018-11-21T16:00'),
    is_recurring=False,
    reserved_by='Tony Stark',
    title='Meeting',
    description='Some description about this meeting',
    recurring_frequency=RecurringFrequency.none,
    recurring_count=0,
):
    """The verb *make* is generally used with reservations (e.g., make a
    reservation), but we decided to use *create* as a verb to keep things
    consistent in the context of CRUD operations.
    """
    return Reservation.create(
        resource_id=resource_id,
        starts_at=starts_at,
        ends_at=ends_at,
        is_recurring=is_recurring,
        reserved_by=reserved_by,
        title=title,
        description=description,
        recurring_frequency=recurring_frequency,
        recurring_count=recurring_count,
    )


def test_find_reservations():
    assert Reservation.query.count() == 0

    create_reservation(
        starts_at=parse_datetime('2018-11-19T12:00'),
        ends_at=parse_datetime('2018-11-19T13:00'),
        title='Reservation 1')
    create_reservation(
        starts_at=parse_datetime('2018-11-21T12:00'),
        ends_at=parse_datetime('2018-11-21T13:00'),
        title='Reservation 2')
    create_reservation(
        starts_at=parse_datetime('2018-11-21T13:00'),
        ends_at=parse_datetime('2018-11-21T14:00'),
        title='Reservation 3')

    reservations = Reservation.find_reservations_on(parse_date('2018-11-19'))
    assert reservations.count() == 1
    assert reservations.first().title == 'Reservation 1'

    reservations = Reservation.find_reservations_on(parse_date('2018-11-20'))
    assert reservations.count() == 0

    reservations = Reservation.find_reservations_on(parse_date('2018-11-21'))
    assert reservations.count() == 2
    reservations = reservations.all()
    assert reservations[0].title == 'Reservation 2'
    assert reservations[1].title == 'Reservation 3'


def test_create_recurring_reservation():
    create_reservation(
        starts_at=parse_datetime('2018-11-01T12:00'),
        ends_at=parse_datetime('2018-11-01T13:00'),
        title='Weekly Event 1',
        recurring_frequency=RecurringFrequency.weekly,
        recurring_count=3)

    rs = Reservation.find_reservations_on(parse_date('2018-11-01'))
    assert rs.count() == 1
    r = rs.first()
    assert r.title == 'Weekly Event 1'
    assert r.starts_at == parse_datetime('2018-11-01T12:00')
    assert r.ends_at == parse_datetime('2018-11-01T13:00')

    rs = Reservation.find_reservations_on(parse_date('2018-11-08'))
    assert rs.count() == 1
    r = rs.first()
    assert r.title == 'Weekly Event 1'
    assert r.starts_at == parse_datetime('2018-11-08T12:00')
    assert r.ends_at == parse_datetime('2018-11-08T13:00')

    rs = Reservation.find_reservations_on(parse_date('2018-11-15'))
    assert rs.count() == 1
    r = rs.first()
    assert r.title == 'Weekly Event 1'
    assert r.starts_at == parse_datetime('2018-11-15T12:00')
    assert r.ends_at == parse_datetime('2018-11-15T13:00')

    rs = Reservation.find_reservations_on(parse_date('2018-11-22'))
    assert rs.count() == 0

    rs = Reservation.find_reservations_on(parse_date('2018-11-02'))
    assert rs.count() == 0


def test_create_overlapping_reservations():
    with pytest.raises(ValueError):
        create_reservation(
            resource_id=2,
            starts_at=parse_datetime('2018-11-01T12:00'),
            ends_at=parse_datetime('2018-11-01T13:00'),
        )
        create_reservation(
            resource_id=2,
            starts_at=parse_datetime('2018-11-01T12:30'),
            ends_at=parse_datetime('2018-11-01T13:30'),
        )

    # Making a reservation for a different resource should not be an issue
    create_reservation(
        resource_id=3,
        starts_at=parse_datetime('2018-11-01T12:30'),
        ends_at=parse_datetime('2018-11-01T13:30'),
    )


@pytest.mark.skip
def test_create_overlapping_recurring_reservations():
    # TODO: Need to deal with overlapping recurring reservations
    with pytest.raises(ValueError):
        create_reservation(
            starts_at=parse_datetime('2018-11-02T12:00'),
            ends_at=parse_datetime('2018-11-02T13:00'),
            recurring_frequency=RecurringFrequency.weekly,
            recurring_count=2,
        )
        create_reservation(
            starts_at=parse_datetime('2018-11-09T12:00'),
            ends_at=parse_datetime('2018-11-09T13:00'),
        )
