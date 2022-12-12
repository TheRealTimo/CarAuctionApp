package com.example.carauctionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartingScreen extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starting_screen);

        button = (Button) findViewById(R.id.redirectToSignUpButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToSignUpPage();
            }
        });
    }

    public void redirectToSignUpPage() {
        Intent openSignUpPage = new Intent(this, SignUp.class);
        startActivity(openSignUpPage);
    }
}