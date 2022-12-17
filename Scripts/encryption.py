"""
Author: Timo Hennig
Date: 05.12.2022
Description: Handling of encryption and decryption as well as key generation
Version: 1.0
"""
import hashlib
import os

import bcrypt
import rsa

cd = os.path.abspath(os.path.join(os.path.dirname(os.path.abspath(__file__)), os.pardir))


def encrypt(message):
    with open(cd + '/RSA/public.pem', 'r') as f:
        publicKey = rsa.PublicKey.load_pkcs1(f.read().encode('utf8'))
    encryptedMessage = rsa.encrypt(message.encode(), publicKey)
    return encryptedMessage


def decrypt(message):
    with open(cd + '/RSA/private.pem', 'r') as f:
        privateKey = rsa.PrivateKey.load_pkcs1(f.read().encode('utf8'))
    decryptedMessage = rsa.decrypt(message, privateKey).decode()
    return decryptedMessage


def generateNewKeys():
    publicKey, privateKey = rsa.newkeys(4096)
    with open(cd + '/RSA/private.pem', 'w') as f:
        f.write(privateKey.save_pkcs1().decode('utf8'))
    with open(cd + '/RSA/public.pem', 'w') as f:
        f.write(publicKey.save_pkcs1().decode('utf8'))


def getPublicKey():
    with open(cd + '/RSA/public.pem', 'r') as f:
        publicKey = rsa.PublicKey.load_pkcs1(f.read().encode('utf8'))

    publicKeyString = publicKey.save_pkcs1().decode('utf8')
    return publicKeyString


def bcryptHash(password):
    return bcrypt.hashpw(password.encode('utf8'), bcrypt.gensalt())


def verifyBcryptHash(password, hash):
    return bcrypt.checkpw(password.encode('utf8'), hash.encode('utf8'))


def generateApiKeyHash(apiKey):
    return hashlib.sha256(apiKey.encode('utf8')).hexdigest()
