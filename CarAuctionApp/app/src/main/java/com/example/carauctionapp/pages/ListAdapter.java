package com.example.carauctionapp.pages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.carauctionapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Listing> {

    public ListAdapter(Context context, ArrayList<Listing> listingArrayList){
        super(context, R.layout.list_item, listingArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Listing listing = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ImageView listingImage = convertView.findViewById(R.id.listingImage);
        TextView topBid = convertView.findViewById(R.id.topBid);
        TextView auctionName = convertView.findViewById(R.id.auctionName);
        TextView bidAmount = convertView.findViewById(R.id.currentBid);

        listingImage.setImageResource(listing.imageId);
        topBid.setText((int) listing.currentBid);
        auctionName.setText(listing.name);
        bidAmount.setText(listing.bidAmount);

        Picasso.get().load(listing.getImageSrc()).placeholder(R.drawable.rolls_royce_mock_image).into(listingImage);
        listingOpeningBid.setText(listing.getOpeningBid().toString() + " €");
        listingName.setText(listing.getName());
        listingDescription.setText(listing.getDescription());
        listingEndDate.setText(listing.getEndDate());

        return convertView;
    }
}
