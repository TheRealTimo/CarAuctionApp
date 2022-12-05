package com.example.carauctionapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import java.util.regex.Pattern;

public class SignUp extends Activity {
    private Button signUpButton;

    private String firstName, lastName, email, emailConfirmation, password, passwordConfirmation;
    private EditText firstNameInput, lastNameInput, emailInput, emailConfirmationInput, passwordInput, passwordConfirmationInput;
    private boolean isAllFieldsChecked = false;

    private Pattern digit = Pattern.compile("[0-9]");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);

        //EditView inputs
        firstNameInput = findViewById(R.id.signUpFirstName);
        lastNameInput = findViewById(R.id.signUpLastName);
        emailInput = findViewById(R.id.signUpEmail);
        emailConfirmationInput = findViewById(R.id.signUpEmailConfirmation);
        passwordInput = findViewById(R.id.signUpPassword);
        passwordConfirmationInput = findViewById(R.id.signUpPasswordConfirmation);

        //Sign Up button
        signUpButton = findViewById(R.id.signUpButton);

        //Sign Up button click event handler
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateLengthOfAllFields()) {
                    //Temporary data for checks
                    String tempFirstName = firstNameInput.getText().toString();
                    String tempLastName = lastNameInput.getText().toString();
                    String tempEmail = emailInput.getText().toString();
                    String tempEmailConfirmation = emailConfirmationInput.getText().toString();
                    String tempPassword = passwordInput.getText().toString();
                    String tempPasswordConfirmation = passwordConfirmationInput.getText().toString();

                    isAllFieldsChecked = validateAllFields(tempFirstName, tempLastName, tempEmail, tempEmailConfirmation, tempPassword, tempPasswordConfirmation);

                    if (isAllFieldsChecked) {
                        storeUserSignUpData();
                        SignUp.this.finish();
                    }
                }
            }
        });
    }

    private boolean validateLengthOfAllFields() {
        if (firstNameInput.length() == 0) {
            firstNameInput.setError("*Required field");
            return false;
        }

        if (lastNameInput.length() == 0) {
            lastNameInput.setError("*Required field");
            return false;
        }

        if (emailInput.length() == 0) {
            emailInput.setError("*Required field");
            return false;
        }

        if (emailConfirmationInput.length() == 0) {
            emailConfirmationInput.setError("*Required field");
            return false;
        }

        if (passwordInput.length() < Utilities.MIN_PASSWORD_LENGTH) {
            passwordInput.setError("Password length must be at least: " + Utilities.MIN_PASSWORD_LENGTH);
            return false;
        }
        if (passwordInput.length() > Utilities.MAX_PASSWORD_LENGTH) {
            passwordInput.setError("Password is too long, max characters: " + Utilities.MAX_PASSWORD_LENGTH);
            return false;
        }

        if (passwordConfirmationInput.length() < Utilities.MIN_PASSWORD_LENGTH) {
            passwordConfirmationInput.setError("Password length must be at least: " + Utilities.MIN_PASSWORD_LENGTH);
            return false;
        }
        if (passwordConfirmationInput.length() > Utilities.MAX_PASSWORD_LENGTH) {
            passwordConfirmationInput.setError("Password is too long, max characters: " + Utilities.MAX_PASSWORD_LENGTH);
            return false;
        }

        return true;
    }

    private boolean validateNameFieldsInput(String firstName, String lastName) {
        if (digit.matcher(firstName).find()) {
            firstNameInput.setError("Digits are not allowed for the First Name");
            return false;
        }

        if (digit.matcher(lastName).find()) {
            lastNameInput.setError("Digits are not allowed for the Last Name");
            return false;
        }

        return true;
    }

    private boolean validateEmailFieldsInput(String email, String emailConfirmation) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Invalid email address!");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailConfirmation).matches()) {
            emailConfirmationInput.setError("Invalid email address!");
            return false;
        }

        return true;
    }

    private boolean validatePasswordFieldsInput(String password, String passwordConfirmation) {
        //PASSWORD
        //Patterns password must contain
        Pattern upperCase = Pattern.compile("[A-Z]");
        Pattern lowerCase = Pattern.compile("[a-z]");

        if (!upperCase.matcher(password).find()) {
            passwordInput.setError("Password must contain upper case characters");
            return false;
        }
        if (!lowerCase.matcher(password).find()) {
            passwordInput.setError("Password must contain lower case characters");
            return false;
        }
        if (!digit.matcher(password).find()) {
            passwordInput.setError("Password must contain digits");
            return false;
        }

        //Patterns password must not contain
        Pattern illegalCharacters = Pattern.compile("[<>+*@&()^!\"#$%':;=?_`{}|~]");

        if(illegalCharacters.matcher(password).find()) {
            passwordInput.setError("Illegal characters detected!");
            return false;
        }

        //PASSWORD CONFIRMATION
        //Patterns password must contain
        if (!upperCase.matcher(passwordConfirmation).find()) {
            passwordConfirmationInput.setError("Password must contain upper case characters");
            return false;
        }
        if (!lowerCase.matcher(passwordConfirmation).find()) {
            passwordConfirmationInput.setError("Password must contain lower case characters");
            return false;
        }
        if (!digit.matcher(passwordConfirmation).find()) {
            passwordConfirmationInput.setError("Password must contain digits");
            return false;
        }

        //Patterns password must not contain
        if(illegalCharacters.matcher(passwordConfirmation).find()) {
            passwordConfirmationInput.setError("Illegal characters detected!");
            return false;
        }

        return true;
    }

    public boolean validateMatchingEmailsAndPasswords (String email, String emailConfirmation, String password, String passwordConfirmation) {
        if (!email.equals(emailConfirmation)) {
            emailInput.setError("Emails must match");
            emailConfirmationInput.setError("Emails must match");
            return false;
        }

        if (!password.equals(passwordConfirmation)) {
            passwordInput.setError("Passwords must match");
            passwordConfirmationInput.setError("Passwords must match");
            return false;
        }

        return true;
    }

    private boolean validateAllFields(String firstName, String lastName, String email, String emailConfirmation, String password, String passwordConfirmation) {
        return validateNameFieldsInput(firstName, lastName) && validateEmailFieldsInput(email, emailConfirmation) && validatePasswordFieldsInput(password, passwordConfirmation) && validateMatchingEmailsAndPasswords(email, emailConfirmation, password, passwordConfirmation);
    }

    private void storeUserSignUpData() {
        firstName = firstNameInput.getText().toString();
        lastName = lastNameInput.getText().toString();
        email = emailInput.getText().toString();
        emailConfirmation = emailConfirmationInput.getText().toString();
        password = passwordInput.getText().toString();
        passwordConfirmation = passwordConfirmationInput.getText().toString();
    }
}
