package com.example.challengewrknprgs.holders;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Locations {
    @SerializedName("locations")
    private List<LocationList> locationList;

    public List<LocationList> getLocationList() {
        return locationList;
    }

    public int getSize(){
        return locationList.size();
    }
}
