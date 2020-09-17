package com.example.challengewrknprgs.holders;

import com.google.gson.annotations.SerializedName;

public class LocationList {
    @SerializedName(value = "lat",alternate = "latitude")
    private String lat;
    @SerializedName(value = "lng",alternate = "longitude")
    private String lng;
    @SerializedName("label")
    private String label;
    @SerializedName("address")
    private String address;
    @SerializedName("image")
    private String image;
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
