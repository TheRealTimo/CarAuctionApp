package com.example.carauctionapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import java.util.regex.Pattern;

public class LoginPage extends Activity {
    private Button logInButton;

    private String email, password;
    private EditText emailInput, passwordInput;
    private boolean isAllFieldsChecked = false;

    private Pattern digit = Pattern.compile("[0-9]");
    private Pattern illegalCharacters = Pattern.compile("[<>+*@&()^!\"#$%':;=?_`{}|~]");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        //EditView inputs
        emailInput = findViewById(R.id.LogInEmail);
        passwordInput = findViewById(R.id.loginPassword);

        //Log In button
        logInButton = findViewById(R.id.login_button);

        //Sign Up button click event handler
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateLengthOfAllFields()) {
                    //Temporary data for checks
                    String tempEmail = emailInput.getText().toString();
                    String tempPassword = passwordInput.getText().toString();

                    isAllFieldsChecked = validateAllFields(tempEmail, tempPassword);

                    if (isAllFieldsChecked) {
                        LoginPage.this.finish();
                    }
                }
            }
        });
    }

    private boolean validateLengthOfAllFields() {
        if (emailInput.length() == 0) {
            emailInput.setError("*Required field");
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

        return true;
    }

    private boolean validateEmailFieldsInput(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Invalid email address!");
            return false;
        }

        return true;
    }

    private boolean validatePasswordFieldsInput(String password) {
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
        if(illegalCharacters.matcher(password).find()) {
            passwordInput.setError("Illegal characters detected!");
            return false;
        }

        return true;
    }

    private boolean validateAllFields(String email, String password) {
        return  validateEmailFieldsInput(email) && validatePasswordFieldsInput(password);
    }

}

