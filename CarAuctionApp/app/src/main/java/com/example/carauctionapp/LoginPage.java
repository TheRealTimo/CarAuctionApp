package com.example.carauctionapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import java.util.regex.Pattern;

public class LoginPage extends Activity {
    private Button logInButton;

    private String  email, password;
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
        passwordInput = findViewById(R.id.LogInPassword);

        //Log In button
        logInButton = findViewById(R.id.login_button);

        //Log in button click event handler
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateLengthOfAllFields()) {
                    //Temporary data for checks
                    String tempEmail = emailInput.getText().toString();
                    String tempPassword = passwordInput.getText().toString();

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

}
