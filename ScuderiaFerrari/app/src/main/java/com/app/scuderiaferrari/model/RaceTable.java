package com.app.scuderiaferrari.model;

import com.google.gson.annotations.SerializedName;


public class RaceTable {
    public String getDriverID() {
        return driverId;
    }

    public void setDriverID(String driverId) {
        this.driverId = driverId;
    }

    @SerializedName("driverId")
    private String driverId;
}
