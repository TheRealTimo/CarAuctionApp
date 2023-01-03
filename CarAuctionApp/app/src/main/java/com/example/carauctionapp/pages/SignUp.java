package com.example.carauctionapp.pages;

import android.app.Activity;
import android.content.Context;
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
import com.example.carauctionapp.utilities.Constants;
import com.example.carauctionapp.utilities.Utilities;
import com.example.carauctionapp.utilities.Validators;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends Activity {
    private Button signUpButton;
    private EditText firstNameInput, lastNameInput, emailInput, passwordInput, phoneInput, billingAddressInput, shippingAddressInput;

    private boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);

        //EditView inputs
        firstNameInput = findViewById(R.id.signUpFirstName);
        lastNameInput = findViewById(R.id.signUpLastName);
        emailInput = findViewById(R.id.signUpEmail);
        passwordInput = findViewById(R.id.signUpPassword);
        phoneInput = findViewById(R.id.signUpPhone);
        billingAddressInput = findViewById(R.id.signUpBillingAddress);
        shippingAddressInput = findViewById(R.id.signUpShippingAddress);

        //Sign Up button
        signUpButton = findViewById(R.id.signUpButton);

        //Sign Up button click event handler
        signUpButton.setOnClickListener(view -> {
            if (validateLengthOfAllFields()) {
                //Temporary data for checks
                String tempFirstName = firstNameInput.getText().toString();
                String tempLastName = lastNameInput.getText().toString();
                String tempEmail = emailInput.getText().toString();
                String tempPassword = passwordInput.getText().toString();
                String tempPhoneNumber = phoneInput.getText().toString();
                String tempBillingAddress = billingAddressInput.getText().toString();
                String tempShippingAddress = shippingAddressInput.getText().toString();

                isAllFieldsChecked = validateAllFields(tempFirstName, tempLastName, tempEmail, tempPassword, tempPhoneNumber, tempBillingAddress, tempShippingAddress);

                if (isAllFieldsChecked) {
                    try {
                        sendSignUpPostRequest();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean validateLengthOfAllFields() {
        if (!Validators.checkIfInputFieldIsEmpty(firstNameInput)) Utilities.setInputError(firstNameInput, Constants.RequiredFieldError);
        if (!Validators.checkIfInputFieldIsEmpty(lastNameInput)) Utilities.setInputError(lastNameInput, Constants.RequiredFieldError);
        if (!Validators.checkIfInputFieldIsEmpty(emailInput)) Utilities.setInputError(emailInput, Constants.RequiredFieldError);
        if (!Validators.checkIfInputFieldIsEmpty(phoneInput)) Utilities.setInputError(phoneInput, Constants.RequiredFieldError);
        if (!Validators.checkIfInputFieldIsEmpty(billingAddressInput)) Utilities.setInputError(billingAddressInput, Constants.RequiredFieldError);
        if (!Validators.checkIfInputFieldIsEmpty(shippingAddressInput)) Utilities.setInputError(shippingAddressInput, Constants.RequiredFieldError);

        if (!Validators.checkIfPasswordFieldMeetsLengthRequirements(passwordInput)) Utilities.setInputError(passwordInput, Constants.PasswordLengthError);

        return Validators.checkIfInputFieldIsEmpty(firstNameInput) &&
                Validators.checkIfInputFieldIsEmpty(lastNameInput) &&
                Validators.checkIfInputFieldIsEmpty(emailInput) &&
                Validators.checkIfInputFieldIsEmpty(phoneInput) &&
                Validators.checkIfInputFieldIsEmpty(billingAddressInput) &&
                Validators.checkIfInputFieldIsEmpty(shippingAddressInput) &&
                Validators.checkIfPasswordFieldMeetsLengthRequirements(passwordInput);
    }

    private boolean validateNameFieldsInput(String firstName, String lastName) {
        if (!Validators.validateTextFieldInputData(firstName, false)) Utilities.setInputError(firstNameInput, Constants.InvalidTextInputFieldError);
        if (!Validators.validateTextFieldInputData(lastName, false)) Utilities.setInputError(lastNameInput, Constants.InvalidTextInputFieldError);

        return Validators.validateTextFieldInputData(firstName, false) &&
                Validators.validateTextFieldInputData(lastName, false);
    }

    private boolean validateEmailFieldInput(String email) {
        if (!Validators.validateEmailFieldInputData(email)) Utilities.setInputError(emailInput, Constants.InvalidEmailError);

        return Validators.validateEmailFieldInputData(email);
    }

    private boolean validatePasswordFieldInput(String password) {
        if (!Validators.validatePasswordFieldInputData(password)) Utilities.setInputError(passwordInput, Constants.InvalidPasswordError);

        return Validators.validatePasswordFieldInputData(password);
    }

    private boolean validatePhoneFieldInput(String phoneNumber) {
        if (!Validators.validatePhoneFieldInputData(phoneNumber)) Utilities.setInputError(phoneInput, Constants.InvalidPhoneNumberError);

        return Validators.validatePhoneFieldInputData(phoneNumber);
    }

    private boolean validateBillingAndShippingAddressFieldsInput(String billingAddress, String shippingAddress) {
        if (!Validators.validateTextFieldInputData(billingAddress, true)) Utilities.setInputError(billingAddressInput, Constants.InvalidTextInputFieldError);
        if (!Validators.validateTextFieldInputData(shippingAddress, true)) Utilities.setInputError(shippingAddressInput, Constants.InvalidTextInputFieldError);

        return Validators.validateTextFieldInputData(billingAddress, true) &&
                Validators.validateTextFieldInputData(shippingAddress, true);
    }

    private boolean validateAllFields(String firstName, String lastName, String email, String password, String phoneNumber, String billingAddress, String shippingAddress) {
        return validateNameFieldsInput(firstName, lastName) &&
                validateEmailFieldInput(email) &&
                validatePasswordFieldInput(password) &&
                validatePhoneFieldInput(phoneNumber) &&
                validateBillingAndShippingAddressFieldsInput(billingAddress, shippingAddress);
    }

    private void sendSignUpPostRequest() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonUserObject = new JSONObject();
        jsonUserObject.put("email", emailInput.getText().toString());
        jsonUserObject.put("password", passwordInput.getText().toString());
        jsonUserObject.put("firstName", firstNameInput.getText().toString());
        jsonUserObject.put("lastName", lastNameInput.getText().toString());
        jsonUserObject.put("phone", phoneInput.getText().toString());
        jsonUserObject.put("billingAddress", billingAddressInput.getText().toString());
        jsonUserObject.put("shippingAddress", shippingAddressInput.getText().toString());

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("user", jsonUserObject);

        final String requestBody = jsonBody.toString();

        JsonObjectRequest signUpUserRequest = new JsonObjectRequest(Request.Method.POST, Constants.SIGN_UP_USER_API_URL, jsonBody,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Context context = getApplicationContext();
                    Toast successfulSignUpToast = Toast.makeText(context, "Successfully signed up!", Toast.LENGTH_SHORT);
                    successfulSignUpToast.show();
                    SignUp.this.finish();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Context context = getApplicationContext();
                    Toast errorSignUpToast = Toast.makeText(context, "There was an error, please try again!", Toast.LENGTH_SHORT);
                    errorSignUpToast.show();
                }
            });

        requestQueue.add(signUpUserRequest);
    }
}
