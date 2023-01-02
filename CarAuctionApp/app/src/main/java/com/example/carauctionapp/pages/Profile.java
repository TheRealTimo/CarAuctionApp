package com.example.carauctionapp.pages;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.carauctionapp.R;

public class Profile extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
    }
}
