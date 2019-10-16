package com.app.scuderiaferrari.model;

import com.google.gson.annotations.SerializedName;

public class MRData {
    @SerializedName("DriverTable")
    private DriverTable driverTable = null;

    @SerializedName("total")
    private int total;

    public DriverTable getDriverTable() {
        return driverTable;
    }


    public int getTotal() {
        return total;
    }
}
