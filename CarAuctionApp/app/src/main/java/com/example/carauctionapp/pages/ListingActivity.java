package com.example.carauctionapp.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;

import com.example.carauctionapp.R;
import com.example.carauctionapp.databinding.ListingPageBinding;

import java.util.ArrayList;

public class ListingActivity extends Activity {

    private ListingPageBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ListingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        int[] imageId = {};
        String[] name = {};
        String[] color = {};
        double[] currentBid = {};
        int[] bidAmount = {};

        ArrayList<Listing> listingArrayList = new ArrayList<>();

        for(int i = 0; i < imageId.length; i++){
            Listing listing = new Listing(name[i], color[i], currentBid[i], bidAmount[i], imageId[i]);
            listingArrayList.add(listing);
        }

        ListAdapter listAdapter = new ListAdapter(ListingActivity.this, listingArrayList);

        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> paremt, View view, int position, long id) {
                Intent i = new Intent(ListingActivity.this, CarInfo.class);
                i.putExtra("name", name[position]);
                i.putExtra("color", color[position]);
                i.putExtra("currentBid", currentBid[position]);
                i.putExtra("bidAmount", bidAmount[position]);
                i.putExtra("imageId", imageId[position]);
                startActivity(i);
            }
        });

    }
}
