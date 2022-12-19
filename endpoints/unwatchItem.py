"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle unwatching an item
Version: 1.0
"""
from flask import jsonify

from Scripts import tools, databaseTools


def unwatchItem(request):
    data = tools.verifyData(request, 'loginUser')
    requestData, userID = data
    if not isinstance(requestData, dict):
        return data

    watchId = requestData['watchId']
    db, sqlCursor = databaseTools.connectToDatabase()
    query = "DELETE FROM watch WHERE watchID = %s AND userID = %s"
    values = (watchId, userID)
    try:
        sqlCursor.execute(query, values)
        db.commit()
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'success', 'message': 'Watch removed'}), 200
    except:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'Problems communicating with database'}), 400
