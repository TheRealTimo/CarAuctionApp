"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle retrieving a user
Version: 1.0
"""
from flask import jsonify

from Scripts import tools, databaseTools


def getUser(request):
    userIDApi = tools.verifyApiKey(request)
    if userIDApi == False:
        return jsonify({'status': 'error', 'message': 'Invalid API key'}), 401

    userId = request.args.get('userId')
    email = request.args.get('email')

    if not userId and not email:
        return jsonify({'status': 'error', 'message': 'Missing required parameter: userId or email'}), 400


    if userId is not None:
        db, sqlCursor = databaseTools.connectToDatabase()
        query = "SELECT userID, name, surname, email, shippingAddress, billingAddress, phone, profileViews, userType, paymentOption FROM user WHERE userID = %s"
        values = (userId,)
        try:
            sqlCursor.execute(query, values)
            result = sqlCursor.fetchone()
        except:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Problems communicating with database'}), 400
        if not result:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'No such user'}), 400
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'success', 'message': 'User retrieved',
                        'user': {'userID': result[0], 'name': result[1], 'surname': result[2], 'email': result[3],
                                 'shippingAddress': result[4], 'billingAddress': result[5], 'phone': result[6],
                                 'profileViews': result[7], 'userType': result[8], 'paymentOption': result[9]}}), 200
    elif email is not None:
        db, sqlCursor = databaseTools.connectToDatabase()
        query = "SELECT  userID, name, surname, email, shippingAddress, billingAddress, phone, profileViews, userType, paymentOption FROM user WHERE email = %s"
        values = (email,)
        try:
            sqlCursor.execute(query, values)
            result = sqlCursor.fetchone()
        except:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Problems communicating with database'}), 400
        if not result:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'No such user'}), 400
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'success', 'message': 'User retrieved',
                        'user': {'userID': result[0], 'name': result[1], 'surname': result[2], 'email': result[3],
                                 'shippingAddress': result[4], 'billingAddress': result[5], 'phone': result[6],
                                 'profileViews': result[7], 'userType': result[8], 'paymentOption': result[9]}}), 200
