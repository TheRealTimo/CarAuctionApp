"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle deletion of users
Version: 1.0
"""
from flask import jsonify

from Scripts import tools, databaseTools, encryption


def deleteUser(request):
    data = tools.verifyData(request, 'deleteUser')
    requestData, userID = data
    if not isinstance(requestData, dict):
        return data

    email = requestData["user"]['email']
    password = requestData["user"]['password']

    db, sqlCursor = databaseTools.connectToDatabase()
    query = "SELECT * FROM user WHERE email = %s AND userID = %s"
    values = (email, userID)
    sqlCursor.execute(query, values)
    result = sqlCursor.fetchone()
    if not result:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'User not associated'}), 401
    # Check if the provided password is correct
    if not encryption.verifyBcryptHash(password, result[4]):
        return jsonify({'status': 'error', 'message': 'Incorrect password'}), 400
    else:
        # Update the user status to deleted
        query = "UPDATE user SET userType = 'deleted' WHERE userID = %s"
        values = (userID,)
        try:
            sqlCursor.execute(query, values)
            db.commit()
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'success', 'message': 'User deleted'}), 200
        except:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Something went wrong'}), 500
