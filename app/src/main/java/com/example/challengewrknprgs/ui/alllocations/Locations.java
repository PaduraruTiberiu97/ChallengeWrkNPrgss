package com.example.challengewrknprgs.ui.alllocations;

public class Locations {
    private String lat;
    private String lng;
    private String label;
    private String address;
    private String image;

    public Locations(String lat, String lng, String label, String address, String image) {
        this.lat = lat;
        this.lng = lng;
        this.label = label;
        this.address = address;
        this.image = image;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getLabel() {
        return label;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }
}
