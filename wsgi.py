#!/usr/local/bin/python3.9
import os

from flask import Flask
from API import app

from flask import Flask

application = Flask(__name__)

def application(environ, start_response):
    return app(environ, start_response)


