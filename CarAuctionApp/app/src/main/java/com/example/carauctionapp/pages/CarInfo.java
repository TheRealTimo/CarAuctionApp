package com.example.carauctionapp.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.carauctionapp.R;

public class CarInfo extends Activity {
    private TextView carTitleView, auctionEndDateView, makeDataView, modelDataView, yearDataView, mileagesDataView;

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

        //Set car info data
        renderCarInfoDataOnPage();
    }

    private void renderCarInfoDataOnPage() {
        //Get parent Intent
        Intent intent = getIntent();

        //Get passed on data from parent Intent
        carTitleView.setText(intent.getStringExtra("name"));
        auctionEndDateView.setText(intent.getStringExtra("listingEndDate"));
        makeDataView.setText(intent.getStringExtra("make"));
        modelDataView.setText(intent.getStringExtra("model"));
        yearDataView.setText("2020");
        mileagesDataView.setText(String.valueOf(intent.getIntExtra("mileages", 0)));
    }
}
