"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle deletion of items
Version: 1.0
"""
from flask import jsonify

from Scripts import tools, databaseTools


def deleteItem(request):
    data = tools.verifyData(request, 'deleteItem')
    requestData, userID = data
    if not isinstance(requestData, dict):
        return data
    itemID = requestData['itemId']

    db, sqlCursor = databaseTools.connectToDatabase()
    query = "SELECT * FROM item WHERE itemID = %s AND userID = %s"
    values = (itemID, userID)
    sqlCursor.execute(query, values)
    result = sqlCursor.fetchone()
    if not result:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'No such item for user'}), 400
    # Delete the item
    query = "DELETE FROM item WHERE itemID = %s"
    values = (itemID,)
    try:
        sqlCursor.execute(query, values)
        db.commit()
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'success', 'message': 'Item deleted'}), 200
    except:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'Something went wrong'}), 500
