"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle the creation of a new auction
Version: 1.0
"""
from datetime import datetime, timedelta

from flask import jsonify

from Scripts import tools, databaseTools


def createAuction(request):
    data = tools.verifyData(request, 'createAuction')
    requestData, userID = data
    if not isinstance(requestData, dict):
        return data

    item = requestData['item']

    db, sqlCursor = databaseTools.connectToDatabase()
    query = "SELECT * FROM item WHERE itemID = %s AND userID = %s"
    values = (item, userID)
    sqlCursor.execute(query, values)
    result = sqlCursor.fetchone()
    if not result:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'No such item for user'}), 400
    else:
        query = "SELECT * FROM auction WHERE itemID = %s"
        values = (item,)
        sqlCursor.execute(query, values)
        result = sqlCursor.fetchone()
        if result is not None:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Item already in an auction'}), 400
        else:
            query = "INSERT INTO auction (userID, itemID, auctionStatus, lastingUntil, openingBid, title, r_description) VALUES (%s, %s, %s, %s, %s, %s, %s)"
            values = (userID, item, 'open', datetime.now() + timedelta(requestData["auction"]["duration"]),
                      requestData["auction"]['openingBid'], requestData["auction"]['title'], requestData["auction"]['description'])
            try:
                sqlCursor.execute(query, values)
                db.commit()
                query = "SELECT auctionID FROM auction WHERE itemID = %s"
                values = (item,)
                sqlCursor.execute(query, values)
                result = sqlCursor.fetchone()
                databaseTools.closeDatabaseConnection(db, sqlCursor)
                return jsonify({'status': 'success', 'message': 'Auction created', 'auctionID': result[0]}), 200
            except Exception as e:
                databaseTools.closeDatabaseConnection(db, sqlCursor)
                return jsonify({'status': 'error', 'message': 'Something went wrong', "error": str(e)}), 500
