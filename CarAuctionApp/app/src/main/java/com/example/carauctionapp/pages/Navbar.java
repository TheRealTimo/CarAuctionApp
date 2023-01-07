package com.example.carauctionapp.pages;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.carauctionapp.R;

public class Navbar extends Activity {
    private TextView carListingsLink, profileLink, watchListLink, recentlySoldLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navbar);

        //Initialize links
        carListingsLink = findViewById(R.id.navbarCarListingsLink);
        profileLink = findViewById(R.id.navbarProfileLink);
        watchListLink = findViewById(R.id.navbarWatchListLink);
        recentlySoldLink = findViewById(R.id.navbarRecentlySoldLink);
    }

    private void redirectToCarListingsPage() {
        carListingsLink.setOnClickListener(redirect -> {
            Log.d("NAVBAR", "TODO: IMPLEMENT REDIRECTION TO CarListings PAGE IN NAVBAR");
        });
    }

    private void redirectToProfilePage() {
        profileLink.setOnClickListener(redirect -> {
            Log.d("NAVBAR", "TODO: IMPLEMENT REDIRECTION TO Profile PAGE IN NAVBAR");
        });
    }

    private void redirectToWatchListPage() {
        watchListLink.setOnClickListener(redirect -> {
            Log.d("NAVBAR", "TODO: IMPLEMENT REDIRECTION TO WatchList IN NAVBAR");
        });
    }

    private void redirectToRecentlySoldCarPage() {
        recentlySoldLink.setOnClickListener(redirect -> {
            Log.d("NAVBAR", "TODO: IMPLEMENT REDIRECTION TO RecentlySold CarPage IN NAVBAR");
        });
    }
}
