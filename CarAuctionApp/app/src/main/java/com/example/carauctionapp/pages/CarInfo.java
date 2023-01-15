package com.example.carauctionapp.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CarInfo extends Activity {
    private TextView carTitleView, auctionEndDateView, makeDataView, modelDataView, yearDataView, mileagesDataView, currentBidView;
    private int auctionId;
    private Button makeABidButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_info_page);

        //Initialize textViews to populate
        carTitleView = findViewById(R.id.carTitle);
        auctionEndDateView = findViewById(R.id.auctionEndDate);
        makeDataView = findViewById(R.id.makeData);
        modelDataView = findViewById(R.id.modelData);
        yearDataView = findViewById(R.id.yearData);
        mileagesDataView = findViewById(R.id.mileagesData);
        currentBidView = findViewById(R.id.carInfoBidAmount);


        makeABidButton = findViewById(R.id.makeABidButton);

        makeABidButton.setOnClickListener(view -> redirectToBid());

        //Set car info data
        renderCarInfoDataOnPage();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    updateBidAmount();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 20000);

    }

    private void renderCarInfoDataOnPage() {
        //Get parent Intent
        Intent intent = getIntent();

        auctionId = intent.getIntExtra("auctionId", -1);
        Log.d("a", String.valueOf(auctionId));

        //Get passed on data from parent Intent
        carTitleView.setText(intent.getStringExtra("name"));
        auctionEndDateView.setText(intent.getStringExtra("listingEndDate"));
        makeDataView.setText(intent.getStringExtra("make"));
        modelDataView.setText(intent.getStringExtra("model"));
        yearDataView.setText("2020");
        mileagesDataView.setText(String.valueOf(intent.getIntExtra("mileages", 0)));
        double bidAmount = intent.getDoubleExtra("bid_amount", 0);

        Log.d("id", String.valueOf(getAuctionId()));
    }
    private void displayBidData(JSONObject jsonResponse) throws JSONException {
        JSONArray bidArray = jsonResponse.getJSONArray("bid");
        double bidAmount = bidArray.getDouble(0);

        currentBidView.setText("Bid Amount: " + bidAmount);
    }

    private void updateBidAmount() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        SessionManagement sessionManagement = new SessionManagement(this);
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

        String apiRequestUrl = Constants.BID_API_URL + Constants.BID_PARAM + String.valueOf(auctionId);

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

    public int getAuctionId() {
        return auctionId;
    }

    public void redirectToBid() {
        Intent openBidPage = new Intent(this, Bid.class);
        openBidPage.putExtra("bid_amount", currentBidView.getText().toString());
        openBidPage.putExtra("auctionId", auctionId);
        startActivityForResult(openBidPage, 1);
    }
}
