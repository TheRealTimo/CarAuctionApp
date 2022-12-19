"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle adding an item to the watchlist
Version: 1.0
"""
from flask import jsonify

from Scripts import tools, databaseTools


def watchItem(request):
    data = tools.verifyData(request, 'watchItem')
    requestData, userID = data
    if not isinstance(requestData, dict):
        return data

    itemID = requestData['itemId']
    db, sqlCursor = databaseTools.connectToDatabase()
    query = "INSERT INTO watch (userID, itemID) VALUES (%s, %s)"
    values = (userID, itemID)
    try:
        sqlCursor.execute(query, values)
        db.commit()
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'success', 'message': 'Item added to watchlist'}), 200
    except:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'Problems communicating with database'}), 400
