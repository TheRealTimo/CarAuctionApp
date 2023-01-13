package com.example.carauctionapp.pages;

import android.util.Log;

public class Listing {

    private String imageSrc, name, endDate, description;
    private Integer openingBid;

    public Listing(String imageSrc, String name, String endDate, String description, Integer openingBid) {
        this.imageSrc = imageSrc;
        this.name = name;
        this.description = description;
        this.endDate = endDate;
        this.openingBid = openingBid;
    }

    public String getImageSrc() {
        return this.imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
        Log.d("imageSrcLink", imageSrc);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getOpeningBid() {
        return this.openingBid;
    }

    public void setOpeningBid(Integer openingBid) {
        this.openingBid = openingBid;
    }

    public Listing deepCopy() {
        return new Listing(this.imageSrc, this.name, this.endDate, this.description, this.openingBid);
    }
}
