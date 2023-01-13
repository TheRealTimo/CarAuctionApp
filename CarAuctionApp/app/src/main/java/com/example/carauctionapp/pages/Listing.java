package com.example.carauctionapp.pages;

public class Listing {

    String name, color;
    double currentBid;
    int bidAmount, imageId;

    public Listing(String name, String color, double currentBid, int imageId) {
        this.name = name;
        this.color = color;
        this.currentBid = currentBid;
        this.imageId = imageId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
