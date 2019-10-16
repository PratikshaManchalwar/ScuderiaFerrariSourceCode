package com.app.scuderiaferrari.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.app.scuderiaferrari.R;
import com.app.scuderiaferrari.adapter.Infoadapter;
import com.app.scuderiaferrari.model.Driver;
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
    private boolean isLastPage;
    private static final int PAGE_LIMIT = 30;
    private ProgressBar progressBar;
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
                            && !isLastPage) {
                        infoadapter.addLoadingFooter();
                        offset += PAGE_LIMIT;
                        if (offset >= total) {
                            isLastPage = true;
                            infoadapter.removeLoadingFooter();
                        }
                        getDriverData();
                    }
                }
            }
        });
        listViewModel = ViewModelProviders.of(this).get(InfoListViewModel.class);
        getDriverData();
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
    @Override
    public void onBackPressed() {
    finish();
    }

}
