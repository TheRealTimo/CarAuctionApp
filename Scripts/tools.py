"""
Author: Timo Hennig
Date: 05.12.2022
Description: File for general functions
Version: 1.0
"""

import re

def sanitize(input):
    return re.sub('[^0-9a-zA-Z@]+', '', input)





