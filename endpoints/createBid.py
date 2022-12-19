"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle submitting a new bid
Version: 1.0
"""

from flask import jsonify

from Scripts import tools, databaseTools


def createBid(request):
    data = tools.verifyData(request, 'createBid')
    requestData, userID = data
    if not isinstance(requestData, dict):
        return data
    auctionID = requestData['auctionId']
    bid = requestData['bid']

    db, sqlCursor = databaseTools.connectToDatabase()
    query = "SELECT * FROM auction WHERE auctionID = %s"
    values = (auctionID,)
    sqlCursor.execute(query, values)
    result = sqlCursor.fetchone()
    print(result)
    if not result:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'No such auction'}), 400
    if result[3] != 'open':
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'Auction is not open'}), 400
    if bid < result[6]:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'Bid is too low'}), 400
    if result[1] == userID:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'User is the owner of the auction'}), 400

    query = "SELECT MAX(bidAmount) FROM bid WHERE auctionID = %s"
    values = (auctionID,)
    sqlCursor.execute(query, values)
    result = sqlCursor.fetchone()

    if result[0] is not None and bid <= result[0]:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'Bid is too low'}), 400
    else:
        query = "INSERT INTO bid (userID, auctionID, bidAmount) VALUES (%s, %s, %s)"
        values = (userID, auctionID, bid)
        try:
            sqlCursor.execute(query, values)
            db.commit()
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'success', 'message': 'Bid created'}), 200
        except:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Something went wrong'}), 500
