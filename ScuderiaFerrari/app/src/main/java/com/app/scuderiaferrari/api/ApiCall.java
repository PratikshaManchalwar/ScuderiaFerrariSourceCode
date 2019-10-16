package com.app.scuderiaferrari.api;

import com.app.scuderiaferrari.model.RootResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiCall {
    @GET("/api/f1/constructors/ferrari/drivers.json")
    Observable<RootResponse> getDriverData(@Query("offset") int offset, @Query("limit") int limit);

    @GET("/api/f1/drivers/{driverId}/constructors/ferrari/results/1.json")
    Observable<RootResponse> getNoOfRaces(@Path("driverId") String driverId);

    @GET("/api/f1/driverStandings/1/drivers.json?offset=0&limit=40")
    Observable<RootResponse> getChampionsData();
}

