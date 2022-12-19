"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle creating of users
Version: 1.0
"""
import hashlib
from datetime import datetime

from flask import jsonify

from Scripts import tools, databaseTools, encryption


def createUser(request):
    data = tools.verifyData(request, 'createUser', False)
    requestData, userID = data
    if not isinstance(requestData, dict):
        return data
    email = requestData["user"]['email']
    password = encryption.bcryptHash(requestData["user"]["password"])
    firstName = requestData["user"]['firstName']
    lastName = requestData["user"]['lastName']
    phone = requestData["user"]['phone']
    billingAddress = requestData["user"]['billingAddress']
    shippingAddress = requestData["user"]['shippingAddress']

    # Check if email is already in use
    db, sqlCursor = databaseTools.connectToDatabase()
    query = "SELECT * FROM user WHERE email = %s"
    values = (email,)
    sqlCursor.execute(query, values)
    result = sqlCursor.fetchone()
    if result is not None:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'Email already in use'}), 400
    else:
        verificationCode = hashlib.sha256((email + str(datetime.now())).encode('utf-8')).hexdigest()[:12]
        query = "INSERT INTO user (name, surname, email, password, shippingAddress, billingAddress, phone, apiKey) VALUES ( %s, %s, %s, %s, %s, %s, %s, %s)"
        values = (firstName, lastName, email, password, shippingAddress, billingAddress, phone,
                  encryption.generateApiKeyHash(verificationCode))
        print(query)
        try:
            sqlCursor.execute(query, values)
            db.commit()
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'success', 'message': 'User created', 'apiKey': verificationCode}), 200
        except:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Something went wrong'}), 500
