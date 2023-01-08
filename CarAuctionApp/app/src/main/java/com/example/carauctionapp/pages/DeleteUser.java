package com.example.carauctionapp.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeleteUser extends Activity {
    private EditText passwordView;
    private Button deleteUserButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_user_page);

        passwordView = findViewById(R.id.deleteUserPassword);
        deleteUserButton = findViewById(R.id.deleteUserButton);

        deleteUserButton.setOnClickListener(deleteAccount -> {
            try {
                deleteUserAccount();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
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
        jsonUserObject.put("password", passwordView.getText().toString());

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("user", jsonUserObject);

        JsonObjectRequest deleteUserRequest = new JsonObjectRequest(Request.Method.DELETE, Constants.USER_API_URL, jsonBody,
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
                    Log.d("DeleteError", "Something went wrong" + error.toString());
                }
            }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Constants.HEADER_API_KEY, sessionManagement.getCurrentUserApiKey());
                headers.put(Constants.HEADER_CONTENT_TYPE_KEY, Constants.HEADER_CONTENT_TYPE_JSON);
                Log.d("Headers", headers.toString());
                return headers;
            }
        };

        requestQueue.add(deleteUserRequest);
    }
}
