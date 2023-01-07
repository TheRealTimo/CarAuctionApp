package com.example.carauctionapp.utilities;

import android.util.Patterns;

import java.util.regex.Pattern;

public class Constants {
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 20;

    //API URLs
    public static final String SIGN_UP_USER_API_URL = "https://api.unnamedproject.xyz/api/v1.0/user";
    public static final String LOG_IN_USER_API_URL = "https://api.unnamedproject.xyz/api/v1.0/loginUser";
    public static final String CAR_API_URL = "https://carapi.app/api";

    //Error messages
    public static final String RequiredFieldError = "*Required field";
    public static final String PasswordLengthError = "Password length must be at least " + Constants.MIN_PASSWORD_LENGTH + "and max characters " + Constants.MAX_PASSWORD_LENGTH;
    public static final String InvalidTextInputFieldError = "Digits and illegal characters are not allowed";
    public static final String InvalidEmailError = "Invalid email address";
    public static final String InvalidPasswordError = "Password must contain lower and upper case letters, digits and must NOT contain illegal characters";
    public static final String InvalidPhoneNumberError = "Invalid phone number";

    //Patterns
    public static final Pattern upperCase = Pattern.compile("[A-Z]");
    public static final Pattern lowerCase = Pattern.compile("[a-z]");
    public static final Pattern digit = Pattern.compile("[0-9]");
    public static final Pattern emailAddressPattern = Patterns.EMAIL_ADDRESS;
    public static final Pattern legalPasswordCharacters = Pattern.compile("[@_+]");
    public static final Pattern illegalNameCharacters = Pattern.compile("[<>+*@&()^!\"#$%':;=?_`{}|~]");
    public static final Pattern illegalPasswordCharacters = Pattern.compile("[<>*&()^!\"#$%':;=?`{}|~]");
    public static final Pattern illegalPhoneCharacters = Pattern.compile("[<>*@&()^!\"#$%':;=?_`{}|~]");

    //Session
    public static final String SESSION_KEY = "sessionUser";
    public static final String USER_API_KEY = "apiKey";
    public static final String USER_IS_LOGGED_IN_KEY = "isLoggedIn";
}
