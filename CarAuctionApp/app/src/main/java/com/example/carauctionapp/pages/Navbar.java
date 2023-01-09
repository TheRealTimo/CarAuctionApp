package com.example.carauctionapp.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.carauctionapp.R;

public class Navbar extends Activity {
    private Button closeButton;

    private TextView carListingsLink, profileLink, watchListLink, recentlySoldLink, createListingLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navbar);

        //Initialize button
        closeButton = findViewById(R.id.navbarCloseButton);

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
        Intent openCarInfoPage = new Intent(this, ListingActivity.class);
        startActivity(openCarInfoPage);
    }

    private void redirectToProfilePage() {
        Intent openCarInfoPage = new Intent(this, Profile.class);
        startActivity(openCarInfoPage);
    }

    private void redirectToWatchListPage() {
        Log.d("NAVBAR", "TODO: IMPLEMENT REDIRECTION TO WatchList IN NAVBAR");
    }

    private void redirectToRecentlySoldCarPage() {
        Log.d("NAVBAR", "TODO: IMPLEMENT REDIRECTION TO RecentlySold CarPage IN NAVBAR");
    }

    private void redirectToCreateListingsPage() {
        Intent openCarInfoPage = new Intent(this, CreateListing.class);
        startActivity(openCarInfoPage);
    }
}
