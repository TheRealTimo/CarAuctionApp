"""
Author: Timo Hennig
Date: 05.12.2022
Description: File for general functions
Version: 1.0
"""

import re

from flask import jsonify

from Scripts import databaseTools, encryption, jsonTools


def sanitize(input):
    return re.sub('[^0-9a-zA-Z@]+', '', input)


def verifyApiKey(request):
    if 'apiKey' not in request.headers:
        return False
    apiKey = request.headers['apiKey']

    keyHash = encryption.generateApiKeyHash(apiKey)
    db, sqlCursor = databaseTools.connectToDatabase()
    sqlCursor.execute("SELECT userID FROM user WHERE apiKey = %s", (keyHash,))
    userID = sqlCursor.fetchone()
    databaseTools.closeDatabaseConnection(db, sqlCursor)
    if userID is None:
        return False
    return userID[0]


def verifyData(request, jsonScheme, apiKeyRequired=True):
    userID = None
    if request.is_json:
        requestData = request.get_json()
        json = jsonTools.validateJson(requestData, jsonScheme)
        if json is not True:
            return json
        if apiKeyRequired:
            userID = verifyApiKey(request)
            if userID is False:
                return jsonify({'status': 'error', 'message': 'Invalid API key'}), 401
    else:
        return jsonify({"error": "Request is not JSON"}), 400
    return requestData, userID
