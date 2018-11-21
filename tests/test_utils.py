from koob.utils import make_day_bounds, parse_datetime


# NOTE: Decided not to write test cases for parse_date, parse_datetime as it
# directly invokes datetime.strptime() which is a part of the Python's standard
# library. Thus there is no point of testing it again.

def test_make_day_bounds():
    rng = make_day_bounds(parse_datetime('2018-11-21T23:23'))
    assert rng[0] == parse_datetime('2018-11-21T00:00')
    assert rng[1] == parse_datetime('2018-11-21T23:59')
