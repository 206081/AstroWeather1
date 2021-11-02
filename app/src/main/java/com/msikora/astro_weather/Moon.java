package com.msikora.astro_weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.shredzone.commons.suncalc.MoonIllumination;
import org.shredzone.commons.suncalc.MoonPhase;
import org.shredzone.commons.suncalc.MoonTimes;
import org.shredzone.commons.suncalc.SunPosition;
import org.shredzone.commons.suncalc.SunTimes;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Moon extends Fragment {

    public Moon() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moon, container, false);
        makeCalc(view);
        return view;
    }

    private void makeCalc(View view) {

        TextView moon_rise = view.findViewById(R.id.moon_rise);
        TextView moon_set = view.findViewById(R.id.moon_set);
        TextView moon_new = view.findViewById(R.id.moon_new);
        TextView moon_full = view.findViewById(R.id.moon_full);
        TextView moon_phase = view.findViewById(R.id.moon_phase);
        TextView moon_synodic = view.findViewById(R.id.moon_synodic);

        Bundle bundle = requireArguments();
        ZonedDateTime dateTime = ZonedDateTime.now();
        double latitude = Double.parseDouble(bundle.getString("latitude"));
        double longitude = Double.parseDouble(bundle.getString("longitude"));

        MoonTimes times = MoonTimes.compute()
                .on(dateTime)   // set a date
                .at(latitude, longitude)   // set a location
                .execute();     // get the results

        MoonPhase newMoon = MoonPhase.compute().phase(MoonPhase.Phase.NEW_MOON)
                .on(dateTime)   // set a date
                .execute();
        MoonPhase fullMoon = MoonPhase.compute().phase(MoonPhase.Phase.FULL_MOON)
                .on(dateTime)   // set a date
                .execute();

        MoonIllumination percent = MoonIllumination.compute().on(dateTime).execute();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter phaseFormat = DateTimeFormatter.ofPattern("dd-LLLL-yyyy HH:mm:ss");

        moon_rise.setText("Moonrise: \t\t\t\t\t\t\t" + times.getRise().format(formatter));
        moon_set.setText("Moonset: \t\t\t\t\t\t\t\t" + times.getSet().format(formatter));
        moon_new.setText("Moon new: \t\t\t\t\t\t" + newMoon.getTime().format(phaseFormat));
        moon_full.setText("Moon full: \t\t\t\t\t\t\t" + fullMoon.getTime().format(phaseFormat));
        moon_phase.setText(String.format("Moon phase: \t\t\t\t\t%.0f%s", percent.getFraction() * 100, "%"));
        moon_synodic.setText("Moon synodic day: " +
                (29 - ChronoUnit.DAYS.between(dateTime, newMoon.getTime())));
    }
}