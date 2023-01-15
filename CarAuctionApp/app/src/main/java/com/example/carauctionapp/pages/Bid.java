package com.example.carauctionapp.pages;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

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
        bidAmountTextView.setText(getIntent().getStringExtra("bid_amount"));

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
                        bidAmountInput.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(bidAmountInput.getWindowToken(), 0);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Context context = getApplicationContext();
                        Toast errorbidToast = Toast.makeText(context, "Bid has to be higher than current bid and opening bid!", Toast.LENGTH_SHORT);
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

