"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle updating a user
Version: 1.0
"""
from flask import jsonify

from Scripts import tools, databaseTools, encryption


def updateUser(request):
    data = tools.verifyData(request, 'updateUser')
    requestData, userID = data
    if not isinstance(requestData, dict):
        return data

    email = requestData['user']['email'] if 'email' in requestData['user'] else None
    password = requestData['user']['password'] if 'password' in requestData['user'] else None
    firstName = requestData['user']['firstName'] if 'firstName' in requestData['user'] else None
    lastName = requestData['user']['lastName'] if 'lastName' in requestData['user'] else None
    phone = requestData['user']['phone'] if 'phone' in requestData['user'] else None
    billingAddress = requestData['user']['billingAddress'] if 'billingAddress' in requestData['user'] else None
    shippingAddress = requestData['user']['shippingAddress'] if 'shippingAddress' in requestData['user'] else None

    if password is not None:
        password = encryption.hashPassword(password)

    db, sqlCursor = databaseTools.connectToDatabase()
    # Update the user with the new data. If the data is None, the old data will be kept
    query = "SELECT * FROM user WHERE userID = %s"
    values = (userID,)
    sqlCursor.execute(query, values)
    result = sqlCursor.fetchone()
    if not result:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'No such user'}), 400
    else:
        query = "UPDATE user SET email = %s, password = %s, firstName = %s, lastName = %s, phone = %s, billingAddress = %s, shippingAddress = %s WHERE userID = %s"
        values = (email if email else result[3], password if password else result['password'],
                  firstName if firstName else result[1], lastName if lastName else result[2],
                  phone if phone else result[7], billingAddress if billingAddress else result[6],
                  shippingAddress if shippingAddress else result[5], userID)
        try:
            sqlCursor.execute(query, values)
            db.commit()
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'success', 'message': 'User updated'}), 200
        except:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Something went wrong'}), 500
