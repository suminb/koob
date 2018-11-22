from koob.models import RecurringFrequency


DT_FORMAT = '%Y-%m-%d %H:%M:%S'


def create_reservation(
    testapp,
    resource_id=1,
    starts_at='2018-11-21T15:00',
    ends_at='2018-11-21T16:00',
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
    data = {
        'resource_id': resource_id,
        'starts_at': starts_at,
        'ends_at': ends_at,
        'reserved_by': reserved_by,
        'title': title,
        'description': description,
        'recurring_frequency': recurring_frequency,
        'recurring_count': recurring_count,
    }
    return testapp.post('/api/v1/reservations', data=data)


def test_initially_empty_reservation_list(testapp):
    resp = testapp.get('/api/v1/reservations')
    assert resp.json['reservations'] == []


def test_create_reservation(testapp):
    resp = create_reservation(testapp, resource_id=11)
    assert resp.status_code == 200
    assert resp.json['title'] == 'Meeting'
    assert resp.json['description'] == 'Some description about this meeting'
    assert resp.json['reserved_by'] == 'Tony Stark'


def test_reservation_list(testapp):
    # Looking for previously made reservations
    resp = testapp.get('/api/v1/reservations?date=2018-11-21')
    assert resp.status_code == 200
    assert len(resp.json['reservations']) == 1

    # Make addtional reservations
    create_reservation(testapp, resource_id=12)
    create_reservation(testapp, resource_id=13)
    create_reservation(testapp, resource_id=14)

    resp = testapp.get('/api/v1/reservations?date=2018-11-21')
    assert resp.status_code == 200
    assert len(resp.json['reservations']) == 4


def test_create_recurring_reservation(testapp):
    pass


def test_delete_reservation(testapp):
    resp = create_reservation(testapp, resource_id=21)
    rid = resp.json['id']
    resp = testapp.delete(f'/api/v1/reservations/{rid}')
    assert resp.status_code == 200

    resp = testapp.delete(f'/api/v1/reservations/{rid}')
    assert resp.status_code == 404
