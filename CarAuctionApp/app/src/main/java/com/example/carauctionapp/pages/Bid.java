package com.example.carauctionapp.pages;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

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
        }, 0, 120000);

        bidButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String bidAmount = bidAmountInput.getText().toString();
                if (!bidAmount.isEmpty()) {
                    placeBid(bidAmount);
                } else {
                    Context context = getApplicationContext();
                    Toast bidAmountErrorToast = Toast.makeText(context, "Please enter a valid bid amount!", Toast.LENGTH_SHORT);
                    bidAmountErrorToast.show();
                }
            }
        });
    }

    private void updateBidAmount() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("bidId", 122);

        final String requestBody = jsonBody.toString();

        JsonObjectRequest getBidRequest = new JsonObjectRequest(Request.Method.POST, Constants.BID_API_URL, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response and update TextView with new bid amount
                        try {
                            final int bidAmount = response.getInt("bidAmount");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    bidAmountTextView.setText("Bid Amount: $" + bidAmount);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Context context = getApplicationContext();
                        Toast errorbidToast = Toast.makeText(context, "There was an error, please try again!", Toast.LENGTH_SHORT);
                        errorbidToast.show();
                    }
                });

        requestQueue.add(getBidRequest);
    }
    private void placeBid(String bidAmount) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("bidAmount", bidAmount);
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Context context = getApplicationContext();
                        Toast errorbidToast = Toast.makeText(context, "There was an error, please try again!", Toast.LENGTH_SHORT);
                        errorbidToast.show();
                    }
                });

        requestQueue.add(placeBidRequest);
    }
}