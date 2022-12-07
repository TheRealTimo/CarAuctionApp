"""
Author: Timo Hennig
Date: 05.12.2022
Description: Retrives and returns the public key
Version: 1.0
"""
from flask import jsonify

from Scripts import encryption


def publicKey():
    key = encryption.getPublicKey()
    return jsonify({'publicKey': key})
