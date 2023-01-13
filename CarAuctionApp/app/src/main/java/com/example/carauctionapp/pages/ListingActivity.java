package com.example.carauctionapp.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.carauctionapp.databinding.ListingPageBinding;
import com.example.carauctionapp.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListingActivity extends Activity {

    private ListingPageBinding binding;

    private static Listing fetchedItem = new Listing("Initial name", "Initial color", 15000, 0);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ListingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Initialize variables for storing fetched data
        ArrayList<Listing> listingArrayList = new ArrayList<>();

        //Fetch all listings
        fetchAllListings();

        listingArrayList.add(fetchedItem);

        //Send clicked listing data to single listing page
        ListAdapter listAdapter = new ListAdapter(ListingActivity.this, listingArrayList);

        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openCarInfoPage = new Intent(ListingActivity.this, CarInfo.class);

                Listing listingClicked = listingArrayList.get(position);

                openCarInfoPage.putExtra("name", listingClicked.name);
                openCarInfoPage.putExtra("color", listingClicked.color);
                openCarInfoPage.putExtra("currentBid", listingClicked.currentBid);
                openCarInfoPage.putExtra("bidAmount", listingClicked.bidAmount);
                openCarInfoPage.putExtra("imageId", listingClicked.imageId);

                startActivity(openCarInfoPage);
            }
        });
    }

    private void handleFetchedItemData(JSONObject response) throws JSONException {
        if (response == null) return;

        JSONObject responseObject = response.getJSONObject("item");

        String listingName = responseObject.getString("make") + " " + responseObject.getString("model")
                + " " + responseObject.getString("trim") + " " + responseObject.getInt("year");

        fetchedItem.setName(listingName);
        fetchedItem.setColor(responseObject.getString("color"));
    }

    private void fetchListingItem(Integer itemId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        SessionManagement sessionManagement = new SessionManagement(this);
        String userEmail = sessionManagement.getCurrentUserEmail();

        if (userEmail.isEmpty()) {
            return;
        }

        JsonObjectRequest fetchItemRequest = new JsonObjectRequest(Request.Method.GET, Constants.ITEM_API_URL + "?itemId=" + itemId, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        handleFetchedItemData(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

        requestQueue.add(fetchItemRequest);
    }

    private void handleListingsResponseData(JSONArray responseArray) throws JSONException {
        if (responseArray.length() <= 0) return;

        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject responseObject = responseArray.getJSONObject(i);

            fetchListingItem(responseObject.getInt("itemId"));
        }
    }

    private void fetchAllListings() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        SessionManagement sessionManagement = new SessionManagement(this);
        String userEmail = sessionManagement.getCurrentUserEmail();

        if (userEmail.isEmpty()) {
            return;
        }

        JsonObjectRequest fetchListingsRequest = new JsonObjectRequest(Request.Method.GET, Constants.AUCTIONS_API_URL, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray listingsResponseArray = new JSONArray();

                    try {
                        listingsResponseArray = response.getJSONArray("auctions");

                        handleListingsResponseData(listingsResponseArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

        requestQueue.add(fetchListingsRequest);
    }
}
