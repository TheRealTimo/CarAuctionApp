#Import a few libraries that could be useful
import os
from flask import Flask, jsonify, request
import mysql.connector
import json
import jsonschema
from jsonschema import validate
import re
import configparser
from endpoints import getAuction, deleteAuction, createAuction, updateAuction, getUser, createUser, deleteUser, updateUser, getBid, createBid, loginUser, watchItem, unwatchItem, getWatchedItem, getItem, createItem, updateItem, deleteItem

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

@app.route('/api/v1.0/auction', methods=['GET', 'POST', 'DELETE', 'PUT'])
def auction():
    if request.method == 'GET':
        return getAuction.getAuction(request)
    elif request.method == 'POST':
        return createAuction.createAuction(request)
    elif request.method == 'DELETE':
        return deleteAuction.deleteAuction(request)
    elif request.method == 'PUT':
        return updateAuction.updateAuction(request)

@app.route('/api/v1.0/user', methods=['GET', 'POST', 'DELETE', 'PUT'])
def user():
    if request.method == 'GET':
        return getUser.getUser(request)
    elif request.method == 'POST':
        return createUser.createUser(request)
    elif request.method == 'DELETE':
        return deleteUser.deleteUser(request)
    elif request.method == 'PUT':
        return updateUser.updateUser(request)

@app.route('/api/v1.0/bid', methods=['GET', 'POST'])
def bid():
    if request.method == 'GET':
        return getBid.getBid(request)
    elif request.method == 'POST':
        return createBid.createBid(request)

@app.route('/api/v1.0/loginUser', methods=['POST'])
def login():
    if request.method == 'POST':
        return loginUser.loginUser(request)

@app.route('/api/v1.0/watch', methods=['POST', 'DELETE', 'GET'])
def watch():
    if request.method == 'POST':
        return watchItem.watchItem(request)
    elif request.method == 'DELETE':
        return unwatchItem.unwatchItem(request)
    elif request.method == 'GET':
        return getWatchedItem.getWatchedItem(request)

@app.route('/api/v1.0/item', methods=['GET', "POST", "DELETE", "PUT"])
def items():
    if request.method == 'GET':
        return getItem.getItem(request)
    elif request.method == 'POST':
        return createItem.createItem(request)
    elif request.method == 'DELETE':
        return deleteItem.deleteItem(request)
    elif request.method == 'PUT':
        return updateItem.updateItem(request)

app.run(host=apiConfig['API']['host'], port=apiConfig['API']['port'], debug=apiConfig['API']['debug'])
