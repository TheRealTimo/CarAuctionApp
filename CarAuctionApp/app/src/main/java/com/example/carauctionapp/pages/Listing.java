package com.example.carauctionapp.pages;

public class Listing {

    //Listing data variables
    private String imageSrc, name, endDate, description;
    private Integer openingBid, auctionId;

    //Car data variables
    private String make, model;
    private Integer mileages;

    public Listing(Integer auctionId, String imageSrc, String name, String endDate, String description, Integer openingBid, String make, String model, Integer mileages) {
        //Listing data
        this.auctionId = auctionId;
        this.imageSrc = imageSrc;
        this.name = name;
        this.description = description;
        this.endDate = endDate;
        this.openingBid = openingBid;

        //Car data
        this.make = make;
        this.model = model;
        this.mileages = mileages;
    }

    public Integer getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Integer auctionId) {
        this.auctionId = auctionId;
    }

    public String getImageSrc() {
        return this.imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
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

    public String getMake() {
        return this.make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getMileages() {
        return this.mileages;
    }

    public void setMileages(Integer mileages) {
        this.mileages = mileages;
    }

    public Listing deepCopy() {
        return new Listing(this.auctionId, this.imageSrc, this.name, this.endDate, this.description, this.openingBid, "", "", 0);
    }
}
