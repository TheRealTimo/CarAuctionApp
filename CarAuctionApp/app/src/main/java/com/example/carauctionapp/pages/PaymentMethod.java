package com.example.carauctionapp.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.carauctionapp.utilities.Validators;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentMethod extends Activity {

    private EditText paymentMethodInput;

    private Button addPaymentMethodButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment_method_page);

        paymentMethodInput = findViewById(R.id.addPaymentMethodInput);

        addPaymentMethodButton = findViewById(R.id.addPaymentMethodButton);

        addPaymentMethodButton.setOnClickListener(verifyIban -> {
            try {
                if (validateIbanInputData()) verifyIban();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void redirectToProfilePage() {
        Intent openProfilePage = new Intent(this, Profile.class);
        startActivity(openProfilePage);
    }

    private boolean validateIbanInputData() {
        if (!Validators.checkIfInputFieldIsEmpty(paymentMethodInput)) paymentMethodInput.setError(Constants.RequiredFieldError);
        if (!Validators.validateTextFieldInputData(paymentMethodInput.getText().toString(), true)) paymentMethodInput.setError(Constants.InvalidTextInputFieldError);

        return Validators.checkIfInputFieldIsEmpty(paymentMethodInput) && Validators.validateTextFieldInputData(paymentMethodInput.getText().toString(), true);
    }

    private void verifyIban() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        SessionManagement sessionManagement = new SessionManagement(this);
        String userEmail = sessionManagement.getCurrentUserEmail();

        if (userEmail.isEmpty()) {
            return;
        }

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("iban", paymentMethodInput.getText().toString());

        JsonObjectRequest verifyIbanRequest = new JsonObjectRequest(Request.Method.POST, Constants.VERIFY_IBAN_API_URL, jsonBody,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Context context = getApplicationContext();
                    Toast successfulVerificationToast = Toast.makeText(context, "Successfully verified payment method!", Toast.LENGTH_LONG);
                    successfulVerificationToast.show();

                    redirectToProfilePage();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Context context = getApplicationContext();
                    Toast errorToast = Toast.makeText(context, "There was an error, please try again!", Toast.LENGTH_LONG);
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

        requestQueue.add(verifyIbanRequest);
    }
}
