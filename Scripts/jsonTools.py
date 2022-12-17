"""
Author: Timo Hennig
Date: 05.12.2022
Description: File handles JSON validation
Version: 1.0
"""

import json
import os

import jsonschema
from flask import jsonify
from jsonschema import validate


def getJsonSchema(fileToLoad):
    cd = os.path.abspath(os.path.join(os.path.dirname(os.path.abspath(__file__)), os.pardir))
    try:
        with open(cd + '/Schemas/' + fileToLoad + '.json') as file:
            schema = json.load(file)
    except FileNotFoundError:
        return False
    return schema


def validateJson(jsonData, jsonSchema):  # Validates the JSON data against the JSON schema
    execute_api_schema = getJsonSchema(jsonSchema)
    if execute_api_schema is False:
        return jsonify({'status': 'error', 'message': 'JSON schema not found'}), 500
    else:
        try:
            validate(instance=jsonData, schema=execute_api_schema)
        except jsonschema.exceptions.ValidationError as err:
            return jsonify({'status': 'error', 'message': err.message}), 400
        except jsonschema.exceptions.SchemaError as err:
            return jsonify({'status': 'error', 'message': err.message}), 500
        return True
