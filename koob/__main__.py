import os

import click

from koob import create_app, run_server
from koob.models import db, Resource


def insert_sample_data():
    params = [
        (1, 'Won', 8),
        (2, 'Dollar', 12),
        (3, 'Yen', 10),
        (4, 'Pound', 8),
        (5, 'Baht', 4),
        (6, 'Dong', 5),
    ]

    for id, title, capacity in params:
        Resource.create(id=id, title=title, capacity=capacity)


@click.group()
def cli():
    pass


@cli.command()
def run():
    """Runs the server."""
    app = create_app()
    with app.app_context():
        run_server(app)


@cli.command()
def create_db():
    """Initializes database, insert some sample data."""
    app = create_app()
    with app.app_context():
        db.create_all()
        insert_sample_data()


if __name__ == '__main__':
    cli()
