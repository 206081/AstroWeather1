package com.msikora.astro_weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.shredzone.commons.suncalc.MoonIllumination;
import org.shredzone.commons.suncalc.MoonPhase;
import org.shredzone.commons.suncalc.MoonTimes;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Moon extends Fragment {

    TextView moon_rise;
    TextView moon_set;
    TextView moon_new;
    TextView moon_full;
    TextView moon_phase;
    TextView moon_synodic;
    Handler handler_time;

    public Moon() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moon, container, false);
        moon_rise = view.findViewById(R.id.moon_rise);
        moon_set = view.findViewById(R.id.moon_set);
        moon_new = view.findViewById(R.id.moon_new);
        moon_full = view.findViewById(R.id.moon_full);
        moon_phase = view.findViewById(R.id.moon_phase);
        moon_synodic = view.findViewById(R.id.moon_synodic);
        handler_time = new Handler();
        fragmentRefresh.run();
        return view;
    }

    private void makeCalc() {
        ZonedDateTime dateTime = ZonedDateTime.now();

        MoonTimes times = MoonTimes.compute()
                .on(dateTime)
                .at(DataHandler.latitude, DataHandler.longitude)
                .execute();

        MoonPhase newMoon = MoonPhase.compute().phase(MoonPhase.Phase.NEW_MOON)
                .on(dateTime)
                .execute();

        MoonPhase fullMoon = MoonPhase.compute().phase(MoonPhase.Phase.FULL_MOON)
                .on(dateTime)
                .execute();

        MoonIllumination percent = MoonIllumination.compute().on(dateTime).execute();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter phaseFormat = DateTimeFormatter.ofPattern("dd-LLLL-yyyy HH:mm:ss");

        moon_rise.setText("Moonrise: \t\t\t\t\t\t\t" + times.getRise().format(formatter));
        moon_set.setText("Moonset: \t\t\t\t\t\t\t\t" + times.getSet().format(formatter));
        moon_new.setText("Moon new: \t\t\t\t\t\t" + newMoon.getTime().format(phaseFormat));
        moon_full.setText("Moon full: \t\t\t\t\t\t\t" + fullMoon.getTime().format(phaseFormat));
        moon_phase.setText(String.format("Moon phase: \t\t\t\t\t%.0f%s", percent.getFraction() * 100, "%"));
        moon_synodic.setText("Moon synodic day: " + (29 - ChronoUnit.DAYS.between(dateTime, newMoon.getTime())));
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