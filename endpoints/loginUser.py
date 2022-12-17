"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle logging in of a user
Version: 1.0
"""
import hashlib
from datetime import datetime

from flask import jsonify

from Scripts import tools, databaseTools, encryption


def loginUser(request):
    data = tools.verifyData(request, 'loginUser', False)
    requestData, userID = data
    if not isinstance(requestData, dict):
        return data

    email = requestData['user']['email']
    password = requestData['user']['password']
    db, sqlCursor = databaseTools.connectToDatabase()
    query = "SELECT password FROM user WHERE email = %s"
    values = (email,)
    sqlCursor.execute(query, values)
    result = sqlCursor.fetchone()
    if not result:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'No such user'}), 400
    if encryption.verifyBcryptHash(password, result[0]):
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'Wrong password'}), 400
    else:
        verificationCode = hashlib.sha256((email + str(datetime.now())).encode('utf-8')).hexdigest()[:12]
        query = "UPDATE user SET apiKey = %s WHERE email = %s"
        values = (verificationCode, email)
        try:
            sqlCursor.execute(query, values)
            db.commit()
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'success', 'message': 'User logged in', 'apiKey': verificationCode}), 200
        except:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Something went wrong'}), 500
