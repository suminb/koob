import click

from koob import create_app
from koob.models import db, Resource


def insert_test_data():
    params = [
        (1, 'Won', 8),
        (2, 'Dollar', 12),
        (3, 'Yen', 10),
        (4, 'Baht', 4),
    ]

    for id, title, capacity in params:
        Resource.create(id=id, title=title, capacity=capacity)


@click.group()
def cli():
    pass


@cli.command()
def create_db():
    app = create_app()
    with app.app_context():
        db.create_all()
        insert_test_data()


if __name__ == '__main__':
    cli()
