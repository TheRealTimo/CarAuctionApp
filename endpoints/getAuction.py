"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle retrieval of a specific auction
Version: 1.0
"""
from flask import jsonify

from Scripts import tools, databaseTools


def getAuction(request):
    data = tools.verifyData(request, 'getAuction')
    requestData, userID = data
    if not isinstance(requestData, dict):
        return data

    if 'auctionId' in requestData:
        auctionId = requestData['auctionId']
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


    elif 'numberOfItems' in requestData:
        numberOfItems = requestData['numberOfItems']
        db, sqlCursor = databaseTools.connectToDatabase()
        query = "SELECT * FROM auction WHERE auctionStatus = 'open' ORDER BY auctionID DESC LIMIT %s "
        values = (numberOfItems,)
        try:
            sqlCursor.execute(query, values)
        except:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Invalid number of items'}), 400
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
