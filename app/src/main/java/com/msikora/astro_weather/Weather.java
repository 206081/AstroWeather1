package com.msikora.astro_weather;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Weather extends FragmentActivity {

    Handler handler_time;
    Sun sun;
    Moon moon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        handler_time = new Handler();

        String string = "Fragment updated every " + DataHandler.interval + " " + DataHandler.intervalMagnitude;

        if (DataHandler.intervalMagnitude.equals("seconds")) {
            DataHandler.interval = DataHandler.interval * 1000;
        } else if (DataHandler.intervalMagnitude.equals("hours")) {
            DataHandler.interval = DataHandler.interval * 1000 * 60;
        } else {
            DataHandler.interval = DataHandler.interval * 1000 * 60 * 24;
        }

        sun = new Sun();
        moon = new Moon();

        getSupportFragmentManager().beginTransaction().add(R.id.sun, sun).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.moon, moon).commit();
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();

        runnableTime.run();
    }


    private final Runnable runnableTime = new Runnable() {
        @Override
        public void run() {
            try {
                updateTime();

            } finally {
                handler_time.postDelayed(this, 10);

            }
        }
    };

    private void updateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        ZonedDateTime dateTime = ZonedDateTime.now();

        TextView time = findViewById(R.id.time);
        time.setText("Time: " + dateTime.format(formatter));
        TextView longitude = findViewById(R.id.longitude);
        longitude.setText("Longitude: \t" + DataHandler.longitude);
        TextView latitude = findViewById(R.id.latitude);
        latitude.setText("Latitude: \t\t" + DataHandler.latitude);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler_time.removeCallbacks(runnableTime);
    }
}
