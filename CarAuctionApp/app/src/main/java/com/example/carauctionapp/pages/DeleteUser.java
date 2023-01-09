package com.example.carauctionapp.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.carauctionapp.StartingScreen;
import com.example.carauctionapp.classes.SessionManagement;
import com.example.carauctionapp.utilities.Constants;
import com.example.carauctionapp.utilities.Validators;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeleteUser extends Activity {
    private EditText passwordInput;

    private Button deleteUserButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_user_page);

        passwordInput = findViewById(R.id.deleteUserPassword);
        deleteUserButton = findViewById(R.id.deleteUserButton);

        deleteUserButton.setOnClickListener(deleteAccount -> {
            try {
                if (validateUserPasswordInputData()) deleteUserAccount();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean validateUserPasswordInputData() {
        if (!Validators.checkIfPasswordFieldMeetsLengthRequirements(passwordInput)) passwordInput.setError(Constants.PasswordLengthError);
        if (!Validators.validatePasswordFieldInputData(passwordInput.getText().toString())) passwordInput.setError(Constants.InvalidPasswordError);

        return Validators.checkIfPasswordFieldMeetsLengthRequirements(passwordInput) && Validators.validatePasswordFieldInputData(passwordInput.getText().toString());
    }

    private void redirectToStartingPage() {
        Intent openStartingScreen = new Intent(this, StartingScreen.class);
        startActivity(openStartingScreen);
        finish();
    }

    private void deleteUserAccount() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        SessionManagement sessionManagement = new SessionManagement(this);
        String userEmail = sessionManagement.getCurrentUserEmail();

        if (userEmail.isEmpty()) {
            return;
        }

        JSONObject jsonUserObject = new JSONObject();
        jsonUserObject.put("email", userEmail);
        jsonUserObject.put("password", passwordInput.getText().toString());

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("user", jsonUserObject);

        String apiRequestUrl = Constants.USER_API_URL + Constants.EMAIL_PARAM + userEmail + Constants.PASSWORD_PARAM + passwordInput.getText().toString();

        JsonObjectRequest deleteUserRequest = new JsonObjectRequest(Request.Method.DELETE, apiRequestUrl, jsonBody,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    sessionManagement.endSession();
                    redirectToStartingPage();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Context currentContext = getApplicationContext();
                    Toast errorToast = Toast.makeText(currentContext, "There was an error, please try again!", Toast.LENGTH_LONG);
                    errorToast.show();
                }
            }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Constants.HEADER_API_KEY, sessionManagement.getCurrentUserApiKey());
                return headers;
            }
        };

        requestQueue.add(deleteUserRequest);
    }
}
