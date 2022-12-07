"""
Author: Timo Hennig
Date: 05.12.2022
Description: File for general functions
Version: 1.0
"""

import re
from Scripts import databaseTools, encryption

def sanitize(input):
    return re.sub('[^0-9a-zA-Z@]+', '', input)

def verifyApiKey(apiKey):
    keyHash = encryption.generateApiKeyHash(apiKey)
    db, sqlCursor = databaseTools.connectToDatabase()
    sqlCursor.execute("SELECT userID FROM User WHERE apiKey = %s", (keyHash,))
    userID = sqlCursor.fetchone()
    if userID is None:
        return False
    print (userID)
    return userID[0]


print (verifyApiKey("test"))




