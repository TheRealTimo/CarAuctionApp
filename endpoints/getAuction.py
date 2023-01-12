"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle retrieval of a specific auction
Version: 1.0
"""
from flask import jsonify

from Scripts import tools, databaseTools


def getAuction(request):
    userID = tools.verifyApiKey(request)
    if userID == False:
        return jsonify({'status': 'error', 'message': 'Invalid API key'}), 401

    auctionId = request.args.get('auctionId')
    numberOfItems = request.args.get('numberOfItems')

    if not auctionId and not numberOfItems:
        return jsonify({'status': 'error', 'message': 'Missing required parameter: auctionId or numberOfItems'}), 400

    if auctionId is not None:
        db, sqlCursor = databaseTools.connectToDatabase()
        query = "SELECT * FROM auction WHERE auctionID = %s AND auctionStatus = 'open'"
        values = (auctionId,)
        sqlCursor.execute(query, values)
        result = sqlCursor.fetchone()
        if not result:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'No such auction'}), 400
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        print(result)
        return jsonify({'status': 'success', 'message': 'Auction found',
                        'auction': {'auctionId': result[0], 'userId': result[1], 'itemId': result[2],
                                    'auctionStatus': result[3], 'created': result[4], 'lastingUntil': result[5],
                                    'openingBid': result[6]}}), 200


    elif numberOfItems is not None:
        db, sqlCursor = databaseTools.connectToDatabase()
        query = "SELECT * FROM auction WHERE auctionStatus = 'open' ORDER BY auctionID DESC LIMIT %s"
        values = (int(numberOfItems),)
        try:
            sqlCursor.execute(query, values)
        except Exception as e:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': str(e)}), 400
        result = sqlCursor.fetchall()
        if not result:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'No auctions found'}), 400
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        auctions = []
        for auction in result:
            auctions.append(
                {'auctionId': auction[0], 'userId': auction[1], 'itemId': auction[2], 'auctionStatus': auction[3],
                 'created': auction[4], 'lastingUntil': auction[5], 'openingBid': auction[6]})
        return jsonify({'status': 'success', 'message': 'Auctions found', 'auctions': auctions}), 200

