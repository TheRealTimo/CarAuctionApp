"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle retrival of a watched item
Version: 1.0
"""
from flask import jsonify

from Scripts import tools, databaseTools


def getWatchedItem(request):
    data = tools.verifyData(request, 'getWatchedItem')
    requestData, userID = data
    if not isinstance(requestData, dict):
        return data

    if 'watchId' in requestData:
        watchId = requestData['watchId']
        db, sqlCursor = databaseTools.connectToDatabase()
        query = "SELECT * FROM watch WHERE watchID = %s"
        values = (watchId,)
        try:
            sqlCursor.execute(query, values)
            result = sqlCursor.fetchone()
        except:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Problems communicating with database'}), 400
        if not result:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'No such watched item'}), 400
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        # Return with labels
        return jsonify({'status': 'success', 'message': 'Watched item retrieved',
                        'watchedItems': {'watchId': result[0], 'userID': result[1], 'auctionID': result[2],
                                         'watchingDate': result[3]}}), 200


    else:
        db, sqlCursor = databaseTools.connectToDatabase()
        query = "SELECT * FROM watch WHERE userID = %s"
        values = (userID,)
        try:
            sqlCursor.execute(query, values)
            result = sqlCursor.fetchall()
        except:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Problems communicating with database'}), 400
        if not result:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'No such watched item'}), 400
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        watchedItems = []
        for item in result:
            watchedItems.append({'watchId': item[0], 'userID': item[1], 'auctionID': item[2], 'watchingDate': item[3]})
        return jsonify({'status': 'success', 'message': 'Watched items retrieved', 'watchedItems': watchedItems}), 200
