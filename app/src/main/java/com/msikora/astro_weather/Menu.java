package com.msikora.astro_weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        spinner = findViewById(R.id.timeList);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.intervalArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void confirmButton(View view) {
        Intent intent = new Intent(this, BaseActivity.class);
        EditText latitude = findViewById(R.id.latitude);
        EditText longitude = findViewById(R.id.longitude);
        EditText interval = findViewById(R.id.interval);

        String latitude_value = latitude.getText().toString();
        String longitude_value = longitude.getText().toString();
        String interval_value = interval.getText().toString();

        double lat;
        double lng;
        double itrv;

        boolean is_empty_latitude = latitude_value.isEmpty();
        boolean is_empty_longitude = longitude_value.isEmpty();
        boolean is_empty_interval = interval_value.isEmpty();

        boolean is_correct_latitude = false;
        boolean is_correct_longitude = false;
        boolean is_correct_interval = false;

        if (is_empty_latitude)
            latitude.setError("Latitude cannot be empty");
        else {
            lat = Double.parseDouble(latitude_value);
            is_correct_latitude = -90 <= lat & lat <= 90;
            if (!is_correct_latitude)
                latitude.setError("Latitude must be between -90 and 90.");
            else
                DataHandler.latitude = lat;
        }

        if (is_empty_longitude)
            longitude.setError("Longitude cannot be empty");
        else {
            lng = Double.parseDouble(longitude_value);
            is_correct_longitude = (0 <= lng & lng <= 180);
            if (!is_correct_longitude)
                longitude.setError("Longitude must be between 0 and 180.");
            else
                DataHandler.longitude = lng;
        }

        if (is_empty_interval)
            interval.setError("Interval cannot be empty");
        else {
            itrv = Double.parseDouble(interval_value);
            is_correct_interval = itrv > 0;
            if (!is_correct_interval)
                interval.setError("Interval must be more than 0.");
            else
                DataHandler.interval = itrv;
        }

        DataHandler.intervalMagnitude = spinner.getSelectedItem().toString();
        DataHandler.intervalTmp = DataHandler.interval;

        if (!is_empty_latitude & !is_empty_longitude & !is_empty_interval
                & is_correct_interval & is_correct_latitude & is_correct_longitude)
            startActivity(intent);
    }

    public void moveToAbout(View view) {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }

    public void moveToExit(View view) {
        finishAndRemoveTask();
    }
}