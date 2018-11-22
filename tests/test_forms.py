import pytest

from koob.forms import ReservationForm
from koob.utils import parse_datetime as dt


@pytest.fixture
def dummy_data():
    return {
        'resource_id': 1,
        'starts_at': dt('2018-11-22T05:00'),
        'ends_at': dt('2018-11-22T06:30'),
        'reserved_by': 'Thomas Anderson',
        'title': 'The Truth',
        'description': '42',
        'recurring_frequency': 0,
        'recurring_count': 0,
    }


def test_dummy_data(dummy_data):
    form = ReservationForm(**dummy_data)
    assert form.validate()


def test_valid_hours_starts_at(dummy_data):
    dummy_data['starts_at'] = dt('2018-11-22T05:01')
    form = ReservationForm(**dummy_data)
    assert not form.validate()


def test_valid_hours_ends_at(dummy_data):
    dummy_data['ends_at'] = dt('2018-11-22T05:29')
    form = ReservationForm(**dummy_data)
    assert not form.validate()


def test_time_travel(dummy_data):
    # The start is later than the end
    dummy_data['starts_at'] = dt('2018-11-22T05:30')
    dummy_data['ends_at'] = dt('2018-11-22T05:00')
    form = ReservationForm(**dummy_data)
    assert not form.validate()
