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
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'success', 'message': 'Item created'}), 200
    except:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'Something went wrong'}), 500
