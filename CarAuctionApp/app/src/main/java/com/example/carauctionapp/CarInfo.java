package com.example.carauctionapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class CarInfo extends Activity {
    //Variables for api request
    private String make, model;
    private Integer year, mileages;

    protected TextView makeDataView, modelDataView, yearDataView, mileagesDataView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_info_page);

        //Initialize textViews to populate
        makeDataView = findViewById(R.id.makeData);
        modelDataView = findViewById(R.id.modelData);
        yearDataView = findViewById(R.id.yearData);
        mileagesDataView = findViewById(R.id.mileagesData);

        //Set car info data
        renderCarInfoDataOnPage();
    }

//    protected void getDataFromApi () {
//        RequestQueue volleyQueue = Volley.newRequestQueue(CarInfo.this);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET,
//                Utilities.CAR_API_URL,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        powerData.setText();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                    }
//                }
//        );
//
//        volleyQueue.add(jsonObjectRequest);
//    }
    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMileages(int mileages) {
        this.mileages = mileages;
    }

    public void renderCarInfoDataOnPage() {
        if(make == null || model == null || year == null || mileages == null) {
            return;
        }

        makeDataView.setText(this.make);
        modelDataView.setText(this.model);
        yearDataView.setText(this.year);
        mileagesDataView.setText(this.mileages);
    }
}
