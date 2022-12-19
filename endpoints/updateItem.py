"""
Author: Timo Hennig
Date: 05.12.2022
Description: API endpoint to handle updating an item
Version: 1.0
"""
from flask import jsonify

from Scripts import tools, databaseTools


def updateItem(request):
    data = tools.verifyData(request, 'updateItem')
    requestData, userID = data
    if not isinstance(requestData, dict):
        return data

    itemID = requestData['itemID']
    make = requestData['car']['make'] if 'make' in requestData['car'] else None
    model = requestData['car']['model'] if 'model' in requestData['car'] else None
    year = requestData['car']['year'] if 'year' in requestData['car'] else None
    trim = requestData['car']['trim'] if 'trim' in requestData['car'] else None
    mileage = requestData['car']['mileage'] if 'mileage' in requestData['car'] else None
    color = requestData['car']['color'] if 'color' in requestData['car'] else None
    condition = requestData['car']['condition'] if 'condition' in requestData['car'] else None
    engine = requestData['car']['engine'] if 'engine' in requestData['car'] else None
    description = requestData['car']['description'] if 'description' in requestData['car'] else None
    images = requestData['car']['images'] if 'images' in requestData['car'] else None

    db, sqlCursor = databaseTools.connectToDatabase()
    query = "SELECT * FROM item WHERE itemID = %s AND userID = %s"
    values = (itemID, userID)
    sqlCursor.execute(query, values)
    result = sqlCursor.fetchone()
    if not result:
        databaseTools.closeDatabaseConnection(db, sqlCursor)
        return jsonify({'status': 'error', 'message': 'No such item for user'}), 400
    else:
        query = "UPDATE item SET make = %s, model = %s, r_year = %s, trim = %s, mileage = %s, color = %s, r_condition = %s, r_engine = %s, r_description = %s, images = %s WHERE itemID = %s"
        values = (make if make is not None else result[2], model if model is not None else result[3],
                  year if year is not None else result[4], trim if trim is not None else result[5],
                  mileage if mileage is not None else result[6], color if color is not None else result[7],
                  condition if condition is not None else result[8], engine if engine is not None else result[9],
                  description if description is not None else result[10], images if images is not None else result[11],
                  itemID)
        try:
            sqlCursor.execute(query, values)
            db.commit()
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'success', 'message': 'Item updated'}), 200
        except:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Something went wrong'}), 500
