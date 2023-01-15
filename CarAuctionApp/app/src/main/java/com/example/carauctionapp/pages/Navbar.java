package com.example.carauctionapp.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.carauctionapp.R;
import com.example.carauctionapp.StartingScreen;
import com.example.carauctionapp.classes.SessionManagement;

public class Navbar extends Activity {
    private Button closeButton, logOutButton;

    private TextView carListingsLink, profileLink, watchListLink, recentlySoldLink, createListingLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navbar);

        //Initialize button
        closeButton = findViewById(R.id.navbarCloseButton);
        logOutButton = findViewById(R.id.navbarLogOutButton);

        //Initialize links
        carListingsLink = findViewById(R.id.navbarCarListingsLink);
        profileLink = findViewById(R.id.navbarProfileLink);
        watchListLink = findViewById(R.id.navbarWatchListLink);
        recentlySoldLink = findViewById(R.id.navbarRecentlySoldLink);
        createListingLink = findViewById(R.id.navbarCreateListingLink);
    }

    @Override
    protected void onStart() {
        super.onStart();

        closeButton.setOnClickListener(closeNavPage -> closeNavPage());
        logOutButton.setOnClickListener(logOut -> logOut());

        carListingsLink.setOnClickListener(redirect -> redirectToCarListingsPage());
        profileLink.setOnClickListener(redirect -> redirectToProfilePage());
        watchListLink.setOnClickListener(redirect -> redirectToWatchListPage());
        recentlySoldLink.setOnClickListener(redirect -> redirectToRecentlySoldCarPage());
        createListingLink.setOnClickListener(redirect -> redirectToCreateListingsPage());
    }

    private void closeNavPage() {
        finish();
    }

    private void redirectToCarListingsPage() {
        Intent openListingsPage = new Intent(this, ListingActivity.class);
        startActivity(openListingsPage);
    }

    private void redirectToProfilePage() {
        Intent openProfilePage = new Intent(this, Profile.class);
        startActivity(openProfilePage);
    }

    private void redirectToWatchListPage() {
        Log.d("NAVBAR", "TODO: IMPLEMENT REDIRECTION TO WatchList IN NAVBAR");
    }

    private void redirectToRecentlySoldCarPage() {
        Log.d("NAVBAR", "TODO: IMPLEMENT REDIRECTION TO RecentlySold CarPage IN NAVBAR");
    }

    private void redirectToCreateListingsPage() {
        Intent openCarListingsPage = new Intent(this, CreateListing.class);
        startActivity(openCarListingsPage);
    }

    private void redirectToStartingPage() {
        Intent openCreateListingPage = new Intent(this, StartingScreen.class);
        startActivity(openCreateListingPage);
        finish();
    }

    private void logOut() {
        SessionManagement sessionManagement = new SessionManagement(this);
        sessionManagement.endSession();

        redirectToStartingPage();
    }
}
