package com.example.carauctionapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carauctionapp.pages.CarInfo;
import com.example.carauctionapp.pages.SignUp;

public class StartingScreen extends AppCompatActivity {
    private Button signUpButton, carInfoPageButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starting_screen);

        signUpButton = findViewById(R.id.redirectToSignUpButton);
        carInfoPageButton = findViewById(R.id.redirectToCarInfoPageButton);

        signUpButton.setOnClickListener(view -> redirectToSignUpPage());

        carInfoPageButton.setOnClickListener(view -> redirectToCarInfoPage());
    }

    public void redirectToSignUpPage() {
        Intent openSignUpPage = new Intent(this, SignUp.class);
        startActivity(openSignUpPage);
    }

    public void redirectToCarInfoPage() {
        Intent openCarInfoPage = new Intent(this, CarInfo.class);
        startActivity(openCarInfoPage);
    }
}