package com.example.carauctionapp.pages;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Bid extends Activity {
    private TextView bidAmountTextView;
    private EditText bidAmountInput;
    private Button bidButton;
    private Timer timer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid);

        bidAmountTextView = findViewById(R.id.currentBid);
        bidAmountInput = findViewById(R.id.setbid);
        bidButton = findViewById(R.id.bidButton);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    updateBidAmount();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 60000);

        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bidAmount = bidAmountInput.getText().toString();
                if (!bidAmount.isEmpty()) {
                    placeBid(Double.parseDouble(bidAmount));
                } else {
                    Context context = getApplicationContext();
                    Toast bidAmountErrorToast = Toast.makeText(context, "Please enter a valid bid amount!", Toast.LENGTH_SHORT);
                    bidAmountErrorToast.show();
                }
            }
        });
    }

    private void displayBidData(JSONObject jsonResponse) throws JSONException {
        JSONArray bidArray = jsonResponse.getJSONArray("bid");
        double bidAmount = bidArray.getDouble(0);

        bidAmountTextView.setText("Bid Amount: " + bidAmount);
    }

    private void updateBidAmount() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        SessionManagement sessionManagement = new SessionManagement(this);
        String auctionId = String.valueOf(getIntent().getIntExtra("auctionId", -1));
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("auctionId", auctionId);

        String apiKey = sessionManagement.getCurrentUserApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            Context context = getApplicationContext();
            // handle error case where apiKey is not present or invalid
            Toast errorToast = Toast.makeText(context, "API Key is invalid, please check and try again!", Toast.LENGTH_SHORT);
            errorToast.show();
            return;
        }

        Log.d("APIKEY", apiKey);

        final String requestBody = jsonBody.toString();

        String apiRequestUrl = Constants.BID_API_URL + Constants.BID_PARAM + auctionId;

        JsonObjectRequest getBidRequest = new JsonObjectRequest(Request.Method.GET, apiRequestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response and update TextView with new bid amount
                        try {
                            displayBidData(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.println(Log.INFO, "Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Context context = getApplicationContext();
                        Toast errorbidToast = Toast.makeText(context, "There was an error, please try again!", Toast.LENGTH_SHORT);
                        errorbidToast.show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put(Constants.HEADER_API_KEY, apiKey);
                return headers;
            }
        };
        requestQueue.add(getBidRequest);
    }

    private void placeBid(double bidAmount) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        SessionManagement sessionManagement = new SessionManagement(this);
        int auctionId = getIntent().getIntExtra("auctionId", -1);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("auctionId", auctionId);
            jsonBody.put("bid", bidAmount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();

        JsonObjectRequest placeBidRequest = new JsonObjectRequest(Request.Method.POST, Constants.BID_API_URL, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Context context = getApplicationContext();
                        Toast bidPlacedToast = Toast.makeText(context, "Bid placed successfully!",Toast.LENGTH_SHORT);
                        bidPlacedToast.show();
                        try {
                            updateBidAmount();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bidAmountInput.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(bidAmountInput.getWindowToken(), 0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Context context = getApplicationContext();
                        Toast errorbidToast = Toast.makeText(context, "There was an error, please try again!", Toast.LENGTH_SHORT);
                        errorbidToast.show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Constants.HEADER_API_KEY, sessionManagement.getCurrentUserApiKey());
                return headers;
            }
        };
        requestQueue.add(placeBidRequest);
    }
}

