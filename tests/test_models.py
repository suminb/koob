from datetime import datetime

from koob.models import Reservation
from koob.utils import parse_datetime as pd


def create_reservation(
    starts_at=pd('2018-11-21T15:00'),
    ends_at=pd('2018-11-21T16:00'),
    reserved_by='Tony Stark',
    title='Meeting',
    description='Some description about this meeting',
):
    """The verb *make* is generally used with reservations (e.g., make a
    reservation), but we decided to use *create* as a verb to keep things
    consistent in the context of CRUD operations.
    """
    return Reservation.create(
        starts_at=starts_at,
        ends_at=ends_at,
        reserved_by=reserved_by,
        title=title,
        description=description,
    )

def setup_module(module):
    # import pdb; pdb.set_trace()
    # Reservation.query.delete()
    pass


def test_find_reservations():
    assert Reservation.query.count() == 0

    create_reservation(
        starts_at=pd('2018-11-19T12:00'),
        ends_at=pd('2018-11-19T13:00'),
        title='Reservation 1')
    create_reservation(
        starts_at=pd('2018-11-21T12:00'),
        ends_at=pd('2018-11-21T13:00'),
        title='Reservation 2')
    create_reservation(
        starts_at=pd('2018-11-21T13:00'),
        ends_at=pd('2018-11-21T14:00'),
        title='Reservation 3')

    reservations = Reservation.find_reservations_on(pd('2018-11-19T23:23'))
    assert reservations.count() == 1
    assert reservations.first().title == 'Reservation 1'

    reservations = Reservation.find_reservations_on(pd('2018-11-20T23:23'))
    assert reservations.count() == 0

    reservations = Reservation.find_reservations_on(pd('2018-11-21T23:23'))
    assert reservations.count() == 2
    reservations = reservations.all()
    assert reservations[0].title == 'Reservation 2'
    assert reservations[1].title == 'Reservation 3'
