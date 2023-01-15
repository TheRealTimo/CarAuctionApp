"""
Author: Timo Hennig
Date: 05.12.2022
Description: The Main API which will redirect to the different endpoints
Version: 1.0
"""
#!/usr/local/bin/python3.9
import configparser
import os

from flask import Flask, request

from endpoints import getAuction, deleteAuction, createAuction, updateAuction, getUser, createUser, deleteUser, \
    updateUser, getBid, createBid, loginUser, watchItem, unwatchItem, getWatchedItem, getItem, createItem, updateItem, \
    deleteItem, publicKey, verifyIban

directory = os.path.dirname(os.path.abspath(__file__))
secretKey = configparser.ConfigParser()
secretKey.read(directory + '/config/Flask.ini')
apiConfig = configparser.ConfigParser()
apiConfig.read(directory + '/config/API.ini')

if not secretKey.sections() or not apiConfig.sections():
    print('Missing or empty config files for API to run')
    exit()

app = Flask(__name__)
app.secret_key = secretKey['Flask']['secretKey']

version = apiConfig['API']['version']


@app.route('/api/' + version + '/auction', methods=['GET', 'POST', 'DELETE', 'PUT'])
def auction():
    if request.method == 'GET':
        return getAuction.getAuction(request)
    elif request.method == 'POST':
        return createAuction.createAuction(request)
    elif request.method == 'DELETE':
        return deleteAuction.deleteAuction(request)
    elif request.method == 'PUT':
        return updateAuction.updateAuction(request)


@app.route('/api/' + version + '/user', methods=['GET', 'POST', 'DELETE', 'PUT'])
def user():
    if request.method == 'GET':
        return getUser.getUser(request)
    elif request.method == 'POST':
        return createUser.createUser(request)
    elif request.method == 'DELETE':
        return deleteUser.deleteUser(request)
    elif request.method == 'PUT':
        return updateUser.updateUser(request)


@app.route('/api/' + version + '/bid', methods=['GET', 'POST'])
def bid():
    if request.method == 'GET':
        return getBid.getBid(request)
    elif request.method == 'POST':
        return createBid.createBid(request)


@app.route('/api/' + version + '/loginUser', methods=['POST'])
def login():
    if request.method == 'POST':
        return loginUser.loginUser(request)


@app.route('/api/' + version + '/watch', methods=['POST', 'DELETE', 'GET'])
def watch():
    if request.method == 'POST':
        return watchItem.watchItem(request)
    elif request.method == 'DELETE':
        return unwatchItem.unwatchItem(request)
    elif request.method == 'GET':
        return getWatchedItem.getWatchedItem(request)


@app.route('/api/' + version + '/item', methods=['GET', "POST", "DELETE", "PUT"])
def items():
    if request.method == 'GET':
        return getItem.getItem(request)
    elif request.method == 'POST':
        return createItem.createItem(request)
    elif request.method == 'DELETE':
        return deleteItem.deleteItem(request)
    elif request.method == 'PUT':
        return updateItem.updateItem(request)


@app.route('/api/' + version + '/key', methods=['GET'])
def key():
    if request.method == 'GET':
        return publicKey.publicKey()


@app.route('/api/' + version + '/iban', methods=['POST'])
def iban():
    if request.method == 'POST':
        return verifyIban.verifyIban(request)

#if __name__ == "__main__": app.run()
#app.run(host=apiConfig['API']['host'], port=apiConfig['API']['port'])
