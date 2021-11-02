package com.msikora.astro_weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.shredzone.commons.suncalc.SunPosition;
import org.shredzone.commons.suncalc.SunTimes;
import org.w3c.dom.Text;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class Sun extends Fragment {

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
        makeCalc(view);
        return view;
    }

    private void makeCalc(View view) {

        TextView sun_rise = view.findViewById(R.id.sun_rise);
        TextView sun_rise_azimuth = view.findViewById(R.id.sun_rise_azimuth);
        TextView sun_set = view.findViewById(R.id.sun_set);
        TextView sun_set_azimuth = view.findViewById(R.id.sun_set_azimuth);
        TextView sun_twilight_civil = view.findViewById(R.id.sun_twilight_civil);
        TextView sun_dawn_civil = view.findViewById(R.id.sun_dawn_civil);

        Bundle bundle = requireArguments();
        ZonedDateTime dateTime = ZonedDateTime.now();
        double latitude = Double.parseDouble(bundle.getString("latitude"));
        double longitude = Double.parseDouble(bundle.getString("longitude"));
        SunTimes times = SunTimes.compute()
                .on(dateTime)   // set a date
                .at(latitude, longitude)   // set a location
                .execute();     // get the results
        SunTimes civilTimes = SunTimes.compute().twilight(SunTimes.Twilight.CIVIL)
                .on(dateTime)   // set a date
                .at(latitude, longitude)   // set a location
                .execute();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        sun_rise.setText("Sunrise: \t\t\t\t\t\t\t\t\t" + times.getRise().format(formatter));
        sun_rise_azimuth.setText(String.format(
                Locale.US,
                "Sunrise azimuth: \t\t%.2f°",
                SunPosition.compute().on(times.getRise())
                        .at(latitude, longitude)
                        .execute()
                        .getAzimuth()
                )
        );
        sun_set.setText("Sunset: \t\t\t\t\t\t\t\t\t" + times.getSet().format(formatter));
        sun_set_azimuth.setText(String.format(
                Locale.US,
                "Sunset azimuth: \t\t%.2f°",
                SunPosition.compute().on(times.getSet())
                        .at(latitude, longitude)
                        .execute()
                        .getAzimuth()
                )
        );
        sun_twilight_civil.setText("Sunrise civil: \t\t\t\t\t" + civilTimes.getRise().format(formatter));
        sun_dawn_civil.setText("Sunset civil: \t\t\t\t\t\t" + civilTimes.getSet().format(formatter));
    }

}