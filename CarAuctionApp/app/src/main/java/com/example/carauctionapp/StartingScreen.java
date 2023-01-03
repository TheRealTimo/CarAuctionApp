package com.example.carauctionapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carauctionapp.classes.SessionManagement;
import com.example.carauctionapp.pages.CarInfo;
import com.example.carauctionapp.pages.LogIn;
import com.example.carauctionapp.pages.SignUp;

public class StartingScreen extends AppCompatActivity {
    private Button signUpButton, logInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starting_screen);

        signUpButton = findViewById(R.id.redirectToSignUpButton);
        logInButton = findViewById(R.id.redirectToLogInButton);

        signUpButton.setOnClickListener(view -> redirectToSignUpPage());
        logInButton.setOnClickListener(view -> redirectToLogInPage());
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkLogInSession();
    }

    private void checkLogInSession() {
        SessionManagement sessionManagement = new SessionManagement(this);
        boolean isUserLoggedIn = sessionManagement.getIsUserLoggedIn();

        if (isUserLoggedIn) {
            redirectToCarListingsPage();
        }
    }

    public void redirectToSignUpPage() {
        Intent openSignUpPage = new Intent(this, SignUp.class);
        startActivity(openSignUpPage);
    }

    public void redirectToLogInPage() {
        Intent openCarInfoPage = new Intent(this, LogIn.class);
        startActivity(openCarInfoPage);
    }

    private void redirectToCarListingsPage() {
        //TO DO: CHANGE THIS CLASS TO CarListings instead of CarInfo
        Intent openCarInfoPage = new Intent(this, CarInfo.class);
        startActivity(openCarInfoPage);
    }
}