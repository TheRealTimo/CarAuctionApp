"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle the deletion of an auction
Version: 1.0
"""
from flask import jsonify

from Scripts import tools, databaseTools


def deleteAuction(request):
    data = tools.verifyData(request, 'deleteAuction')
    requestData, userID = data
    if not isinstance(requestData, dict):
        return data
    auctionId = requestData['auctionId']

    db, sqlCursor = databaseTools.connectToDatabase()
    query = "SELECT * FROM auction WHERE auctionID = %s AND userID = %s"
    values = (auctionId, userID)
    sqlCursor.execute(query, values)
    result = sqlCursor.fetchone()
    if not result:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'No such auction for user'}), 400
    # SUpdate the status of the auction to deleted
    query = "UPDATE auction SET auctionStatus = 'deleted' WHERE auctionID = %s"
    values = (auctionId,)
    try:
        sqlCursor.execute(query, values)
        db.commit()
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'success', 'message': 'Auction deleted'}), 200
    except:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'Something went wrong'}), 500
