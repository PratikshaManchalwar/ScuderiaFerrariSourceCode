package com.app.scuderiaferrari.model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DriverTable {
    @SerializedName("Drivers")
    private ArrayList<Driver> drivers;

    public ArrayList<Driver> getDrivers() {
        return drivers;
    }
}
