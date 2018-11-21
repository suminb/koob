from datetime import datetime


def parse_date(date, format='%Y-%m-%d'):
    """Makes a date object from a string.
    :type date: str or int
    :rtype: datetime.date
    """
    return parse_datetime(date, format=format)


def parse_datetime(dt, format='%Y-%m-%dT%H:%M'):
    """Makes a datetime object from a string.
    :param dt: Datetime
    :param at: Time at which the relative time is evaluated
    :param format: Datetime string format
    """
    return datetime.strptime(dt, format)


def make_day_bounds(dt):
    """Makes a tuple of (beginning, end) of a given day. The granularity of the
    latter value is up to minutes.

    :param date: datetime.now() is expected in general
    """
    return (dt.replace(hour=0, minute=0, second=0, microsecond=0),
            dt.replace(hour=23, minute=59, second=0, microsecond=0))
