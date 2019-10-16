package com.app.scuderiaferrari.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Driver implements Serializable {
    @SerializedName("driverId")
    private String driverId;
    @SerializedName("givenName")
    private String givenName;
    @SerializedName("nationality")
    private String nationality;
    @SerializedName("dateOfBirth")
    private String dateOfBirth;
    @SerializedName("url")
    private String url;

    private int noOfRaces;
    private boolean hasWonChampionship;

    public String getDriverId() {
        return driverId;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNoOfRaces(int noOfRaces) {
        this.noOfRaces = noOfRaces;
    }

    public void setHasWonChampionship(boolean hasWonChampionship) {
        this.hasWonChampionship = hasWonChampionship;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getUrl() {
        return url;
    }

    public int getNoOfRaces() {
        return noOfRaces;
    }

    public boolean isHasWonChampionship() {
        return hasWonChampionship;
    }
}
