package com.app.scuderiaferrari.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.app.scuderiaferrari.R;
import com.app.scuderiaferrari.model.Driver;

/**
 * Created by Pratiksha on 8/10/19.
 */

public class DriverDetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.driverdetail);

        TextView nationality = findViewById(R.id.nationality);
        TextView dateOfBirth = findViewById(R.id.dateOfBirth);
        TextView givenName = findViewById(R.id.givenName);
        TextView noOfRacesWon = findViewById(R.id.noOfRaceWon);
        Button urlButton = findViewById(R.id.urlButton);

        final Driver driver = (Driver) getIntent().getSerializableExtra("driver");

        //set driver's name nationality dob and number of races won to textview
        givenName.setText(driver.getGivenName());
        nationality.setText(driver.getNationality());
        dateOfBirth.setText(driver.getDateOfBirth());
        noOfRacesWon.setText(String.valueOf(driver.getNoOfRaces()));

        //button event for driver's biography
        urlButton.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(driver.getUrl()));
            startActivity(browserIntent);
        });


    }

}
