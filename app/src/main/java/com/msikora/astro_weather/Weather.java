package com.msikora.astro_weather;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.shredzone.commons.suncalc.SunTimes;

import java.time.ZonedDateTime;

public class Weather extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Sun sun = new Sun();
        sun.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.sun, sun).commit();
        Moon moon = new Moon();
        moon.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.moon, moon).commit();
//        Conditions conditions = new Conditions();
//        conditions.setArguments(getIntent().getExtras());
//        getSupportFragmentManager().beginTransaction().add(R.id.conditions, conditions).commit();

    }


}
