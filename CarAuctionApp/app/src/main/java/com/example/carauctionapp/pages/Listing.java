package com.example.carauctionapp.pages;

public class Listing {

    String name, color;
    double currentBid;
    int bidAmount, imageId;

    public Listing(String name, String color, double currentBid, int bidAmount, int imageId) {
        this.name = name;
        this.color = color;
        this.currentBid = currentBid;
        this.bidAmount = bidAmount;
        this.imageId = imageId;
    }
}
