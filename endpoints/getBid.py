"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle retrieving a bid
Version: 1.0
"""
from flask import jsonify

from Scripts import tools, databaseTools


def getBid(request):
    userID = tools.verifyApiKey(request)
    if userID == False:
        return jsonify({'status': 'error', 'message': 'Invalid API key'}), 401

    bidID = request.args.get('bidId')
    auctionId = request.args.get('auctionId')
    if not bidID and not auctionId:
        return jsonify({'status': 'error', 'message': 'Missing parameter'}), 400

    if bidID is not None:
        db, sqlCursor = databaseTools.connectToDatabase()
        query = "SELECT * FROM bid WHERE bidID = %s"
        values = (bidID,)
        try:
            sqlCursor.execute(query, values)
            result = sqlCursor.fetchone()
        except:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Problems communicating with database'}), 400
        if not result:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'No such bid'}), 400
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'success', 'message': 'Bid retrieved', 'bid': result}), 200

    if auctionId is not None:
        db, sqlCursor = databaseTools.connectToDatabase()
        query = "SELECT bidAmount FROM bid WHERE auctionID = %s ORDER BY bidAmount DESC LIMIT 1"
        values = (auctionId,)
        try:
            sqlCursor.execute(query, values)
            result = sqlCursor.fetchall()
        except:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Problems communicating with database'}), 500
        if not result:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'No bids for this auction'}), 400
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'success', 'message': 'Bid retrieved', 'bid': result[0]}), 200

