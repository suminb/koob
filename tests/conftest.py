import os

import pytest

from koob import create_app
from koob.models import db as db_


DEFAULT_TEST_DB_URL = 'sqlite:///test.db'


@pytest.fixture(scope='session')
def app(request):
    """Session-wide test `Flask` application."""
    settings_override = {
        'TESTING': True,
    }
    settings_override['SQLALCHEMY_DATABASE_URI'] = \
        os.environ.get('TEST_DB_URL', DEFAULT_TEST_DB_URL)
    app = create_app(__name__, config=settings_override)

    return app


@pytest.fixture(scope='module', autouse=True)
def db(app, request):
    """Session-wide test database."""
    def teardown():
        with app.app_context():
            db_.session.close()
            db_.drop_all()

    request.addfinalizer(teardown)

    db_.app = app
    with app.app_context():
        db_.create_all()

        yield db_


@pytest.fixture
def testapp(app, db):
    with app.app_context():
        yield app.test_client()
