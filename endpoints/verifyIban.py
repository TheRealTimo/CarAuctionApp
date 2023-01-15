"""
Author: Timo Hennig
Date: 05.12.2022
Description: Validates the IBAN
Version: 1.0
"""

import configparser
import os

import requests
from flask import jsonify

from Scripts import tools, databaseTools

cd = os.path.abspath(os.path.join(os.path.dirname(os.path.abspath(__file__)), os.pardir))


def verifyIban(request):
    data = tools.verifyData(request, 'verifyIban')
    requestData, userID = data

    if not isinstance(requestData, dict):
        return data

    bankingConfig = configparser.ConfigParser()
    bankingConfig.read(cd + '/config/Banking.ini')
    if not bankingConfig.sections():
        print('Missing or empty config files for API to run')
        return jsonify({'status': 'error', 'message': 'Missing or empty config files for API to run'}), 500
    apiKey = bankingConfig['Banking']['apiKey']

    iban = tools.sanitize(requestData['iban'])
    url = "https://api.apilayer.com/bank_data/iban_validate"
    params = {'iban_number': iban}
    headers = {'apiKey': apiKey}
    req = requests.get(url, params=params, headers=headers)
    if req.json()['valid']:
        db, sqlCursor = databaseTools.connectToDatabase()
        # Update the users table with the new IBAN
        sql = "UPDATE user SET paymentOption = %s WHERE userID = %s"
        val = ("verified", userID)
        try:
            sqlCursor.execute(sql, val)
            db.commit()
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'success', 'message': 'IBAN verified'}), 200
        except Exception as e:
            databaseTools.closeDatabaseConnection(db, sqlCursor)
            return jsonify({'status': 'error', 'message': 'Something went wrong', 'error': str(e)}), 500
    else:
        return jsonify({'status': 'error', 'message': 'Invalid IBAN'}), 400
