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
    if not bidID:
        return jsonify({'status': 'error', 'message': 'Missing required parameter: bidId'}), 400

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
