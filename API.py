#Import a few libraries that could be useful
import os
from flask import Flask, jsonify, request
import mysql.connector
import json
import jsonschema
from jsonschema import validate
import re
import configparser

directory = os.path.dirname(os.path.abspath(__file__))
secretKey = configparser.ConfigParser()
secretKey.read(directory + '/config/Flask.ini')
apiConfig = configparser.ConfigParser()
apiConfig.read(directory + '/config/API.ini')

if not secretKey.sections() or not apiConfig.sections():
    print ('Missing or empty config files for API to run')
    exit()


app = Flask(__name__)
app.secret_key = secretKey['Flask']['secretKey']


app.run(host=apiConfig['API']['host'], port=apiConfig['API']['port'], debug=apiConfig['API']['debug'])

