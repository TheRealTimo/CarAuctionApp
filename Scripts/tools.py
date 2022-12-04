import re

def sanitize(input):
    return re.sub('[^0-9a-zA-Z@]+', '', input)



