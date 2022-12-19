"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle updating an auction
Version: 1.0
"""
from datetime import datetime, timedelta

from flask import jsonify

from Scripts import tools, databaseTools


def updateAuction(request):
    data = tools.verifyData(request, 'updateAuction')
    requestData, userID = data
    if not isinstance(requestData, dict):
        return data

    auctionId = requestData['auction']['auctionId']
    title = requestData['auction']['title'] if 'title' in requestData['auction'] else None
    description = requestData['auction']['description'] if 'description' in requestData['auction'] else None
    openingBid = requestData['auction']['openingBid'] if 'openingBid' in requestData['auction'] else None
    duration = requestData['auction']['duration'] if 'duration' in requestData['auction'] else None

    # Check if the auction exists and if the user is the owner of the auction
    db, sqlCursor = databaseTools.connectToDatabase()
    query = "SELECT * FROM auction WHERE auctionID = %s AND userID = %s"
    values = (auctionId, userID)
    sqlCursor.execute(query, values)
    result = sqlCursor.fetchone()
    if result is None:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'Auction does not exist or you are not the owner'}), 400
    else:
        # Update the auction with the new values and keep the old ones if no new value was provided
        query = "UPDATE auction SET title = %s, r_description = %s, openingBid = %s, lastingUntil = %s WHERE auctionID = %s"
        values = (title if title is not None else result[7], description if description is not None else result[8],
                  openingBid if openingBid is not None else result[6],
                  datetime.now() + timedelta(duration) if duration is not None else result[5], auctionId)
        try:
            sqlCursor.execute(query, values)
            db.commit()
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'success', 'message': 'Auction updated'}), 200
        except:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Something went wrong'}), 500
