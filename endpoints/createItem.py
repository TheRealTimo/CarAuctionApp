"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle the creation of a new item
Version: 1.0
"""
from flask import jsonify

from Scripts import tools, databaseTools


def createItem(request):
    data = tools.verifyData(request, 'createItem')
    requestData, userID = data
    if not isinstance(requestData, dict):
        return data

    make = requestData["car"]['make']
    model = requestData["car"]['model']
    year = requestData["car"]['year']
    trim = requestData["car"]['trim']
    mileage = requestData["car"]['mileage']
    color = requestData["car"]['color']
    condition = requestData["car"]['condition']
    engine = requestData["car"]['engine']
    description = requestData["car"]['description']
    images = requestData["car"]['images']

    db, sqlCursor = databaseTools.connectToDatabase()
    query = "INSERT INTO item (userID, make, model, r_year, trim, mileage, color, r_condition, r_engine, r_description, images) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
    values = (userID, make, model, year, trim, mileage, color, condition, engine, description, images)
    try:
        sqlCursor.execute(query, values)
        db.commit()
        query = "SELECT itemID FROM item WHERE userID = %s AND make = %s AND model = %s AND r_year = %s AND trim = %s AND mileage = %s AND color = %s AND r_condition = %s AND r_engine = %s AND r_description = %s AND images = %s ORDER BY itemID DESC LIMIT 1"
        values = (userID, make, model, year, trim, mileage, color, condition, engine, description, images)
        sqlCursor.execute(query, values)
        result = sqlCursor.fetchone()
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'success', 'message': 'Item created', 'itemID': result[0]}), 200
    except:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'Something went wrong'}), 500
