from koob.utils import parse_datetime as pd


DT_FORMAT = '%Y-%m-%d %H:%M:%S'


def create_reservation(
    testapp,
    starts_at='2018-11-21T15:00',
    ends_at='2018-11-21T16:00',
    reserved_by='Tony Stark',
    title='Meeting',
    description='Some description about this meeting',
):
    """The verb *make* is generally used with reservations (e.g., make a
    reservation), but we decided to use *create* as a verb to keep things
    consistent in the context of CRUD operations.
    """
    data = {
        'starts_at': starts_at,
        'ends_at': ends_at,
        'reserved_by': reserved_by,
        'title': title,
        'description': description,
    }
    return testapp.post('/api/v1/reservations', data=data)


def test_initially_empty_reservation_list(testapp):
    resp = testapp.get('/api/v1/reservations')
    assert resp.json['reservations'] == []


def test_create_reservation(testapp):
    resp = create_reservation(testapp)
    assert resp.status_code == 200
    assert resp.json['title'] == 'Meeting'
    assert resp.json['description'] == 'Some description about this meeting'
    assert resp.json['reserved_by'] == 'Tony Stark'
    assert pd(resp.json['starts_at'], DT_FORMAT) == pd('2018-11-21T15:00')
    assert pd(resp.json['ends_at'], DT_FORMAT) == pd('2018-11-21T16:00')


def test_reservation_list(testapp):
    # Looking for previously made reservations
    resp = testapp.get('/api/v1/reservations')
    assert resp.status_code == 200
    assert len(resp.json['reservations']) == 1

    # Make addtional reservations
    create_reservation(testapp)
    create_reservation(testapp)
    create_reservation(testapp)

    resp = testapp.get('/api/v1/reservations')
    assert resp.status_code == 200
    assert len(resp.json['reservations']) == 4


def test_create_recurring_reservation(testapp):
    pass
