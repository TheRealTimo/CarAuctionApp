package com.example.carauctionapp.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carauctionapp.R;
import com.example.carauctionapp.classes.SessionManagement;
import com.example.carauctionapp.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends Activity {
    private String firstName, lastName, email, phone, billingAddress, shippingAddress;

    private TextView firstNameView, lastNameView, emailView, phoneView, billingAddressView, shippingAddressView;
    private Button profileDeleteButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        firstNameView = findViewById(R.id.profileFirstNameData);
        lastNameView = findViewById(R.id.profileLastNameData);
        emailView = findViewById(R.id.profileEmailData);
        phoneView = findViewById(R.id.profilePhoneData);
        billingAddressView = findViewById(R.id.profileBillingAddressData);
        shippingAddressView = findViewById(R.id.profileShippingAddressData);

        profileDeleteButton = findViewById(R.id.profileDeleteButton);

        profileDeleteButton.setOnClickListener(view -> redirectToUserDeletionPage());
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            fetchUserData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayUserData(JSONObject jsonResponse) throws JSONException {
        JSONObject userResponse = (JSONObject) jsonResponse.get("user");

        firstName = (String) userResponse.get("name");
        lastName = (String) userResponse.get("surname");
        email = (String) userResponse.get("email");
        phone = (String) userResponse.get("phone");
        billingAddress = (String) userResponse.get("billingAddress");
        shippingAddress = (String) userResponse.get("shippingAddress");

        firstNameView.setText(firstName);
        lastNameView.setText(lastName);
        emailView.setText(email);
        phoneView.setText(phone);
        billingAddressView.setText(billingAddress);
        shippingAddressView.setText(shippingAddress);
    }

    private void fetchUserData() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        SessionManagement sessionManagement = new SessionManagement(this);
        String userEmail = sessionManagement.getCurrentUserEmail();

        if (userEmail.isEmpty()) {
            return;
        }

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("email", userEmail);
        
        String apiRequestUrl = Constants.USER_API_URL + Constants.EMAIL_PARAM + userEmail;

        JsonObjectRequest fetchUserDataRequest = new JsonObjectRequest(Request.Method.GET, apiRequestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            displayUserData(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.println(Log.INFO, "Response", response.toString());
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Constants.HEADER_API_KEY, sessionManagement.getCurrentUserApiKey());
                return headers;
            }
        };

        requestQueue.add(fetchUserDataRequest);
    }

    private void redirectToUserDeletionPage() {
        Intent openDeleteUserPage = new Intent(this, DeleteUser.class);
        startActivity(openDeleteUserPage);
    }
}
