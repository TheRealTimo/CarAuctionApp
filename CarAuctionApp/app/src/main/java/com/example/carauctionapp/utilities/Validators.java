package com.example.carauctionapp.utilities;

import android.widget.EditText;

public class Validators {


    public static boolean checkIfInputFieldIsEmpty(EditText inputField) {
        if (inputField.length() == 0) {
            return false;
        }

        return true;
    }

    public static boolean checkIfPasswordFieldMeetsLengthRequirements(EditText passwordField) {
        if (passwordField.length() < Constants.MIN_PASSWORD_LENGTH) {
            return false;
        }

        if (passwordField.length() > Constants.MAX_PASSWORD_LENGTH) {
            return false;
        }

        return true;
    }

    public static boolean validateTextFieldInputData(String name, boolean allowDigits) {
        if (!allowDigits && Constants.digit.matcher(name).find()) {
            return false;
        }

        if (Constants.illegalNameCharacters.matcher(name).find()) {
            return false;
        }

        return true;
    }

    public static boolean validateEmailFieldInputData(String email) {
        if (!Constants.emailAddressPattern.matcher(email).matches()) {
            return false;
        }

        return true;
    }

    public static boolean validatePasswordFieldInputData(String password) {
        if (!Constants.upperCase.matcher(password).find()) {
            return false;
        }

        if (!Constants.lowerCase.matcher(password).find()) {
            return false;
        }

        if (!Constants.digit.matcher(password).find()) {
            return false;
        }

        if(!Constants.legalPasswordCharacters.matcher(password).find()) {
            return false;
        }

        if(Constants.illegalPasswordCharacters.matcher(password).find()) {
            return false;
        }

        return true;
    }

    public static boolean validatePhoneFieldInputData(String phoneNumber) {
        if (Constants.upperCase.matcher(phoneNumber).find()) {
           return false;
        }

        if (Constants.lowerCase.matcher(phoneNumber).find()) {
            return false;
        }

        if (Constants.illegalPhoneCharacters.matcher(phoneNumber).find()) {
            return false;
        }

        return true;
    }
}
