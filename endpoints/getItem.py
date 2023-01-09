"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle retrieving an item
Version: 1.0
"""
from flask import jsonify

from Scripts import tools, databaseTools


def getItem(request):
    userID = tools.verifyApiKey(request)
    if userID == False:
        return jsonify({'status': 'error', 'message': 'Invalid API key'}), 401

    itemID = request.args.get('itemId')

    db, sqlCursor = databaseTools.connectToDatabase()
    query = "SELECT * FROM item WHERE itemID = %s"
    values = (itemID,)
    try:
        sqlCursor.execute(query, values)
        result = sqlCursor.fetchone()
    except:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'Problems communicating with database'}), 400
    if not result:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'No such item'}), 400
    databaseTools.closeDatabaseConnection(db, sqlCursor)
    return jsonify({'status': 'success', 'message': 'Item retrieved', 'item': result}), 200
