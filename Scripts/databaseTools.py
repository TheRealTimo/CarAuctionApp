import configparser
import os
import mysql.connector
from flask import jsonify

cd = os.path.abspath(os.path.join(os.path.dirname(os.path.abspath(__file__)), os.pardir))

def connectToDatabase():
    dbConfig = configparser.ConfigParser()
    dbConfig.read(cd + '/Database.ini')

    if not dbConfig.sections():
        print ('Missing or empty config files for API to run')
        exit()

    try:
        db = mysql.connector.connect(
            host=dbConfig['mysqlDB']['host'],
            user=dbConfig['mysqlDB']['user'],
            password=dbConfig['mysqlDB']['password'],
            database=dbConfig['mysqlDB']['database'],
            auth_plugin=dbConfig['mysqlDB']['auth_plugin']
        )
        sqlCursor = db.cursor()
        return db, sqlCursor

    except mysql.connector.Error as err:
        print("Database connection error" + str(err))
        return False
