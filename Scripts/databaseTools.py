"""
Author: Timo Hennig
Date: 05.12.2022
Description: File handles database connections
Version: 1.0
"""

import configparser
import os

import mysql.connector

cd = os.path.abspath(os.path.join(os.path.dirname(os.path.abspath(__file__)), os.pardir))

def connectToDatabase():
    dbConfig = configparser.ConfigParser()
    dbConfig.read(cd + '/config/Database.ini')

    if not dbConfig.sections():
        print ('Missing or empty config files for Database to run')
        exit()

    try:
        db = mysql.connector.connect(
            host=dbConfig['mysqlDB']['host'],
            user=dbConfig['mysqlDB']['user'],
            port = dbConfig['mysqlDB']['port'],
            password=dbConfig['mysqlDB']['password'],
            database=dbConfig['mysqlDB']['database'],
            auth_plugin=dbConfig['mysqlDB']['auth_plugin']
        )
        sqlCursor = db.cursor()
        return db, sqlCursor

    except mysql.connector.Error as err:
        print("Database connection error " + str(err))
        return False

print (connectToDatabase())
