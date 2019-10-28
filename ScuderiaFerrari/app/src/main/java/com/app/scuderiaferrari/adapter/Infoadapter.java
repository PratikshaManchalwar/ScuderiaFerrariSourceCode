package com.app.scuderiaferrari.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.scuderiaferrari.R;
import com.app.scuderiaferrari.model.Driver;
import com.app.scuderiaferrari.view.DriverDetailActivity;
import java.util.ArrayList;

public class Infoadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Driver> drivers = new ArrayList<>();
    private final int VIEW_TYPE_ITEM = 0;
    private boolean isLoadingAdded = true;
    public Infoadapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drivername_row, parent, false);
            return new DataViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemprogressbar, parent, false);
            return new ProgressViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof DataViewHolder) {
            populateDriverData((DataViewHolder) viewHolder, i);
        }
    }
    private void populateDriverData(DataViewHolder viewHolder, int position) {
        Resources resources = viewHolder.itemView.getContext().getResources();
        Driver driver = drivers.get(position);
        viewHolder.driverName.setText(driver.getGivenName());
        viewHolder.noOfRacesWon.setText(String.valueOf(driver.getNoOfRaces()));
        viewHolder.driverLayout.setBackgroundColor(driver.isHasWonChampionship()
                ? ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
                : ResourcesCompat.getColor(resources, R.color.cardview_light_background, null));

        viewHolder.driverLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, DriverDetailActivity.class);
            intent.putExtra("driver", driver);
            context.startActivity(intent);

        });

    }
    @Override
    public int getItemCount() {
        return drivers == null ? 0 : drivers.size();
    }
    @Override
    public int getItemViewType(int position) {
        final int VIEW_TYPE_LOADING = 1;
        return (position == drivers.size() - 1 && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void updateDrivers(ArrayList<Driver> driverList)
    {
        drivers.addAll(driverList);
    }
    public void addLoadingFooter() {
        isLoadingAdded = true;
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        TextView driverName, noOfRacesWon;
        RelativeLayout driverLayout;

        private DataViewHolder(View itemView) {
            super(itemView);
            driverName = itemView.findViewById(R.id.driverName);
            noOfRacesWon = itemView.findViewById(R.id.noOfRaceWon);
            driverLayout = itemView.findViewById(R.id.driverLayout);
        }

    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        private ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }    }
}

