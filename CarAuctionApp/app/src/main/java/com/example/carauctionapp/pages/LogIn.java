package com.example.carauctionapp.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carauctionapp.R;
import com.example.carauctionapp.classes.SessionManagement;
import com.example.carauctionapp.utilities.Constants;
import com.example.carauctionapp.utilities.Utilities;
import com.example.carauctionapp.utilities.Validators;

import org.json.JSONException;
import org.json.JSONObject;

public class LogIn extends Activity {
    private Button logInButton;

    private EditText emailInput, passwordInput;

    private boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        //EditView inputs
        emailInput = findViewById(R.id.LogInEmail);
        passwordInput = findViewById(R.id.loginPassword);

        //Log In button
        logInButton = findViewById(R.id.login_button);

        //Log In button click event handler
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateLengthOfAllFields()) {
                    //Temporary data for checks
                    String tempEmail = emailInput.getText().toString();
                    String tempPassword = passwordInput.getText().toString();

                    isAllFieldsChecked = validateAllFields(tempEmail, tempPassword);
                    Log.println(Log.INFO, "VALIDATION",String.valueOf(isAllFieldsChecked));

                    if (isAllFieldsChecked) {
                        try {
                            sendLogInPostRequest();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        LogIn.this.finish();
                    }
                }
            }
        });
    }

    private boolean validateLengthOfAllFields() {
        if (!Validators.checkIfInputFieldIsEmpty(emailInput)) Utilities.setInputError(emailInput, Constants.RequiredFieldError);
        if (!Validators.checkIfPasswordFieldMeetsLengthRequirements(passwordInput)) Utilities.setInputError(passwordInput, Constants.PasswordLengthError);

        return Validators.checkIfInputFieldIsEmpty(emailInput) &&
                Validators.checkIfPasswordFieldMeetsLengthRequirements(passwordInput);
    }

    private boolean validateEmailFieldsInput(String email) {
        if (!Validators.validateEmailFieldInputData(email)) Utilities.setInputError(emailInput, Constants.InvalidEmailError);

        return Validators.validateEmailFieldInputData(email);
    }

    private boolean validatePasswordFieldsInput(String password) {
        if (!Validators.validatePasswordFieldInputData(password)) Utilities.setInputError(passwordInput, Constants.InvalidPasswordError);

        return Validators.validatePasswordFieldInputData(password);
    }

    private boolean validateAllFields(String email, String password) {
        return  validateEmailFieldsInput(email) &&
                validatePasswordFieldsInput(password);
    }

    private void redirectToCarListingsPage() {
        //TO DO: CHANGE THIS CLASS TO CarListings instead of CarInfo
        Intent openCarInfoPage = new Intent(this, CarInfo.class);
        startActivity(openCarInfoPage);
        LogIn.this.finish();
    }

    private void instantiateLogInSession(String apiKey) {
        SessionManagement sessionManagement = new SessionManagement(LogIn.this);
        sessionManagement.saveSession(apiKey);
    }

    private void sendLogInPostRequest() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonUserObject = new JSONObject();
        jsonUserObject.put("email", emailInput.getText().toString());
        jsonUserObject.put("password", passwordInput.getText().toString());

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("user", jsonUserObject);

        final String requestBody = jsonBody.toString();

        JsonObjectRequest logInUserRequest = new JsonObjectRequest(Request.Method.POST, Constants.LOG_IN_USER_API_URL, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Context context = getApplicationContext();
                        Toast successfulLogInToast = Toast.makeText(context, "Successfully logged in!", Toast.LENGTH_SHORT);
                        successfulLogInToast.show();
                        redirectToCarListingsPage();

                        try {
                            instantiateLogInSession(response.get("apiKey").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Context context = getApplicationContext();
                        Toast errorLogInToast = Toast.makeText(context, "There was an error, please try again!", Toast.LENGTH_SHORT);
                        errorLogInToast.show();
                    }
                });

        requestQueue.add(logInUserRequest);
    }
}

