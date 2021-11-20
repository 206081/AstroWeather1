package com.msikora.astro_weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.shredzone.commons.suncalc.SunPosition;
import org.shredzone.commons.suncalc.SunTimes;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class Sun extends Fragment {

    TextView sun_rise;
    TextView sun_rise_azimuth;
    TextView sun_set;
    TextView sun_set_azimuth;
    TextView sun_twilight_civil;
    TextView sun_dawn_civil;
    Handler handler_time;

    public Sun() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sun, container, false);

        handler_time = new Handler();
        sun_rise = view.findViewById(R.id.sun_rise);
        sun_rise_azimuth = view.findViewById(R.id.sun_rise_azimuth);
        sun_set = view.findViewById(R.id.sun_set);
        sun_set_azimuth = view.findViewById(R.id.sun_set_azimuth);
        sun_twilight_civil = view.findViewById(R.id.sun_twilight_civil);
        sun_dawn_civil = view.findViewById(R.id.sun_dawn_civil);

        fragmentRefresh.run();
        return view;
    }

    private void makeCalc() {
        ZonedDateTime dateTime = ZonedDateTime.now();

        SunTimes times = SunTimes.compute()
                .on(dateTime)
                .at(DataHandler.latitude, DataHandler.longitude)
                .execute();

        SunTimes civilTimes = SunTimes.compute().twilight(SunTimes.Twilight.CIVIL)
                .on(dateTime)
                .at(DataHandler.latitude, DataHandler.longitude)
                .execute();

        SunPosition rise_azimuth = SunPosition.compute().on(times.getRise())
                .at(DataHandler.latitude, DataHandler.longitude)
                .execute();

        SunPosition set_azimuth = SunPosition.compute().on(times.getSet())
                .at(DataHandler.latitude, DataHandler.longitude)
                .execute();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        sun_rise.setText("Sunrise: \t\t\t\t\t\t\t\t\t" + times.getRise().format(formatter));
        sun_rise_azimuth.setText(String.format(Locale.US, "Sunrise azimuth: \t\t%.2f°", rise_azimuth.getAzimuth()));
        sun_set.setText("Sunset: \t\t\t\t\t\t\t\t\t" + times.getSet().format(formatter));
        sun_set_azimuth.setText(String.format(Locale.US, "Sunset azimuth: \t\t%.2f°", set_azimuth.getAzimuth()));
        sun_twilight_civil.setText("Sunrise civil: \t\t\t\t\t" + civilTimes.getRise().format(formatter));
        sun_dawn_civil.setText("Sunset civil: \t\t\t\t\t\t" + civilTimes.getSet().format(formatter));

    }

    private final Runnable fragmentRefresh = new Runnable() {
        @Override
        public void run() {
            try {
                makeCalc();
            } finally {
                handler_time.postDelayed(this, (long) DataHandler.interval);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler_time.removeCallbacks(fragmentRefresh);
    }

}