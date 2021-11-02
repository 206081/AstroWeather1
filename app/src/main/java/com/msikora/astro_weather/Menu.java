package com.msikora.astro_weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

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
        Intent intent = new Intent(this, Weather.class);
        EditText latitude = findViewById(R.id.latitude);
        EditText longitude = findViewById(R.id.longitude);
        EditText interval = findViewById(R.id.interval);

        String latitude_value = latitude.getText().toString();
        String longitude_value = longitude.getText().toString();
        String interval_value = interval.getText().toString();

        if (latitude_value.isEmpty())
            latitude.setError("Latitude cannot be empty");
        else
            intent.putExtra("latitude", latitude_value);

        if (longitude_value.isEmpty())
            longitude.setError("Longitude cannot be empty");
        else
            intent.putExtra("longitude", longitude_value);

        if (interval_value.isEmpty())
            interval.setError("Time interval cannot be empty");
        else
            intent.putExtra("interval", interval_value);

        intent.putExtra("time", spinner.getSelectedItem().toString());

        if (!latitude_value.isEmpty() & !longitude_value.isEmpty() & !interval_value.isEmpty())
            startActivity(intent);
    }

}