#!/usr/bin/env python

from distutils.core import setup
from setuptools import find_packages

import koob


def readme():
    try:
        with open('README.rst') as f:
            return f.read()
    except:
        return '(Could not read from README.rst)'


setup(
    name='koob',
    version=koob.__version__,
    description='Koob: Meeting room reservation system',
    long_description=readme(),
    url='http://github.com/suminb/koob',
    license='MIT',
    packages=find_packages(),
    entry_points={
        'console_scripts': [
            'koob = koob.__main__:cli'
        ]
    },
)
