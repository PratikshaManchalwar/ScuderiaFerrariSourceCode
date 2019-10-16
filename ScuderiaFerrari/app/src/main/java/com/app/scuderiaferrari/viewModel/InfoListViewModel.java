package com.app.scuderiaferrari.viewModel;
import android.util.Log;

import com.app.scuderiaferrari.api.ApiCall;
import com.app.scuderiaferrari.api.RetrofitClient;
import com.app.scuderiaferrari.model.Driver;
import com.app.scuderiaferrari.model.RootResponse;
import java.util.ArrayList;
import java.util.List;
import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Pratiksha on 8/10/19.
 */
/*
 ** This Class as ViewModel to exposes streams of data relevant to the View (MainActivity).
 */

public class InfoListViewModel extends ViewModel{
    private ApiCall api = RetrofitClient.getInstance().getApi();
    private CompositeDisposable disposable = new CompositeDisposable();
    //call to get list of all ferrari drivers
    public MutableLiveData<Pair<ArrayList<Driver>, Integer>> getDriverList(int offset, int limit) {
        MutableLiveData<Pair<ArrayList<Driver>, Integer>> mutableLiveDataDriverPair = new MutableLiveData<>();
        disposable.add(api.getDriverData(offset, limit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rootResponse -> {
                    ArrayList<Driver> driverList = rootResponse.getMrData().getDriverTable().getDrivers();
                    int total = rootResponse.getMrData().getTotal();
                    mutableLiveDataDriverPair.setValue(new Pair<>(driverList, total));
                }, error -> Log.e("call_failed", "reason" + error)));


        return mutableLiveDataDriverPair;
    }

    //call to get the  no of races won by perticular driver
    public MutableLiveData<ArrayList<Driver>> getRaceWonCount(final ArrayList<Driver> driverList) {
        MutableLiveData<ArrayList<Driver>> driversMutableLiveData = new MutableLiveData<>();
        List<Observable<RootResponse>> racesWonObservableList = new ArrayList<>();

        for (Driver driver : driverList) {
            racesWonObservableList.add(api.getNoOfRaces(driver.getDriverId()));
        }

        disposable.add(Observable.zip(racesWonObservableList, objects -> {
            for (int i = 0; i < objects.length; i++) {
                RootResponse rootResponse = (RootResponse) objects[i];
                driverList.get(i).setNoOfRaces(rootResponse.getMrData().getTotal());
            }
            return driverList;
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(drivers -> updateChampionshipData(driverList, driversMutableLiveData),
                        error -> Log.e("call_failed", "reason" + error)));

        return driversMutableLiveData;
    }

    //call to get list of drivers who has became world champions
    private void updateChampionshipData(final ArrayList<Driver> updatedDrivers,
                                        MutableLiveData<ArrayList<Driver>> driversMutableLiveData) {
        disposable.add(api.getChampionsData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<Driver> drivers = response.getMrData().getDriverTable().getDrivers();
                    List<String> championDriverIdList = new ArrayList<>();
                    for (Driver championDriver : drivers) {
                        championDriverIdList.add(championDriver.getDriverId());
                    }

                    for (Driver driver : updatedDrivers) {
                        driver.setHasWonChampionship(championDriverIdList.contains(driver.getDriverId()));
                    }


                    driversMutableLiveData.setValue(updatedDrivers);
                }, error -> Log.e("call_failed", "reason" + error.getMessage())));
    }
}

