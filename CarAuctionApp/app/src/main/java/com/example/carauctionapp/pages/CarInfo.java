package com.example.carauctionapp.pages;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.carauctionapp.R;

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
