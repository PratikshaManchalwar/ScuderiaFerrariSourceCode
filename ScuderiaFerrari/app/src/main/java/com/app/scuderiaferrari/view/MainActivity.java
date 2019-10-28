package com.app.scuderiaferrari.view;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.scuderiaferrari.R;
import com.app.scuderiaferrari.adapter.Infoadapter;
import com.app.scuderiaferrari.model.Driver;
import com.app.scuderiaferrari.util.InternetCheck;
import com.app.scuderiaferrari.viewModel.InfoListViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Pratiksha on 8/10/19.
 */

public class MainActivity extends AppCompatActivity {
    private InfoListViewModel listViewModel;
    private Infoadapter infoadapter;

    private int offset = 0;
    private int total;
    private static final int PAGE_LIMIT = 30;
    private boolean loadMore = true;

    private ProgressBar progressBar;

    private BroadcastReceiver connectivityChangeReceiver = null;
    private boolean isInternetDialogVisible = false;
    private Dialog internetPermissionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar =  findViewById(R.id.main_progress);
        RecyclerView recyclerview = findViewById(R.id.recyclerView);

        recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerview.setHasFixedSize(true);
        infoadapter = new Infoadapter(this);
        recyclerview.setAdapter(infoadapter);

        listViewModel = ViewModelProviders.of(this).get(InfoListViewModel.class);

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_LIMIT
                            && loadMore) {
                        infoadapter.addLoadingFooter();
                        offset += PAGE_LIMIT;
                        if (offset + PAGE_LIMIT >= total) {
                            loadMore = false;
                            infoadapter.removeLoadingFooter();
                        } else {
                            loadMore = true;
                        }
                        getDriverData();
                    }
                }
            }
        });

    }

    /*Check MOBILE and WIFI connection is enable or disable*/
    public class connectivityChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (InternetCheck.getConnectivityStatusString(MainActivity.this)) {
                getDriverData();
                if (isInternetDialogVisible) {
                    internetPermissionDialog.dismiss();
                }

            } else {
                showNoInternetMessage();
            }

        }
    }
    /*get List of drivers */
    private void getDriverData() {
        listViewModel.getDriverList(offset, PAGE_LIMIT).observe(this, driverList -> {
            if (driverList != null) {
                if (driverList.first != null && driverList.second != null) {
                    this.total = driverList.second;
                    getNoOfRaces(driverList.first);
                }
            }
        });
    }
    /*get the number of races won by passing driverid and update adapter accordingly*/
    private void getNoOfRaces(final ArrayList<Driver> drivers) {
        listViewModel.getRaceWonCount(drivers).observe(this, driverList -> {
              progressBar.setVisibility(View.GONE);
              infoadapter.updateDrivers(driverList);
              infoadapter.notifyDataSetChanged();

        });
    }
    /*check internet connection is available or not*/
    private void showNoInternetMessage() {
        // dialog for internet permission
        internetPermissionDialog = new Dialog(MainActivity.this);
        internetPermissionDialog.setContentView(R.layout.internetcheck_dialog);
        internetPermissionDialog.setCancelable(false);
        TextView tvCancel = internetPermissionDialog.findViewById(R.id.cancel);
        TextView tvRetry = internetPermissionDialog.findViewById(R.id.retry);

        tvCancel.setOnClickListener(view ->
        {
            if (isInternetDialogVisible) {
                internetPermissionDialog.dismiss();
            }
        });
        tvRetry.setOnClickListener(view ->
        {
            if (isInternetDialogVisible) {
                internetPermissionDialog.dismiss();
            }
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        });

        internetPermissionDialog.show();
        isInternetDialogVisible = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*registered the receiver */
        connectivityChangeReceiver = new connectivityChangeReceiver();
        registerReceiver(connectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*unregistered the receiver */
        unregisterReceiver(connectivityChangeReceiver);
    }
    @Override
    public void onBackPressed() {
    finish();
    }

}
