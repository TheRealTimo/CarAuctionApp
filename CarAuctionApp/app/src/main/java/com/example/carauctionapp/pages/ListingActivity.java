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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carauctionapp.R;
import com.example.carauctionapp.databinding.ListingPageBinding;
import com.example.carauctionapp.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListingActivity extends Activity {

    private ListingPageBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ListingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();

        ArrayList<Listing> listingArrayList = fetchAllListings();

        //Send clicked listing data to single listing page
        ListAdapter listAdapter = new ListAdapter(ListingActivity.this, listingArrayList);

        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
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

    private void fetchListing(Integer auctionId) throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("auctionId", auctionId);

        JsonObjectRequest fetchListingRequest = new JsonObjectRequest(Request.Method.GET, Constants.AUCTION_API_URL, jsonBody,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("SINGLE_LISTING", response.toString());
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Context currentContext = getApplicationContext();
                    Toast errorToast = Toast.makeText(currentContext, "There was an error, please try again!", Toast.LENGTH_LONG);
                    errorToast.show();
                }
            });
    }

    private ArrayList<Listing> fetchAllListings() {
        ArrayList<Listing> listings = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest fetchListingsRequest = new JsonObjectRequest(Request.Method.GET, Constants.AUCTION_API_URL, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("LISTINGS_RESPONSE", response.toString());

                    //TO DO IMPLEMENT ADDING ARRAY TO LISTINGS ARRAY LIST
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
        );

        return listings;
    }
}
