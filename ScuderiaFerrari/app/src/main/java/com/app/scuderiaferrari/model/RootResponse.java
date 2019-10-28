package com.app.scuderiaferrari.model;

import com.google.gson.annotations.SerializedName;


public class RootResponse {
    public MRData getMrData() {
        return mrData;
    }

    @SerializedName("MRData")
    private MRData mrData;

}
