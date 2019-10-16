package com.app.scuderiaferrari.model;

import com.google.gson.annotations.SerializedName;


public class RootResponse {
    public MRData getMrData() {
        return mrData;
    }

    public void setMrData(MRData mrData) {
        this.mrData = mrData;
    }

    @SerializedName("MRData")
    private MRData mrData;

}
