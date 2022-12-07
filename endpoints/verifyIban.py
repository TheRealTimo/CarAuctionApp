"""
Author: Timo Hennig
Date: 05.12.2022
Description: Validates the IBAN
Version: 1.0
"""

import configparser
import os
import requests

cd = os.path.abspath(os.path.join(os.path.dirname(os.path.abspath(__file__)), os.pardir))


def verifyIban(request):
    bankingConfig = configparser.ConfigParser()
    bankingConfig.read(cd + '/config/Banking.ini')
    if not bankingConfig.sections():
        print('Missing or empty config files for API to run')
        exit()
    apiKey = bankingConfig['Banking']['apiKey']

    url = "https://api.apilayer.com/bank_data/iban_validate"
    iban = "DE89370400440532013001"  # TODO get iban from request
    params = {'iban_number': iban}
    headers = {'apiKey': apiKey}
    req = requests.get(url, params=params, headers=headers)
    if req.json()['valid']:
        return True
    else:
        return False



