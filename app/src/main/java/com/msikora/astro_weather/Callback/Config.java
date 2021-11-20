package com.msikora.astro_weather.Callback;

import android.os.Handler;

public class Config {

    private static final Config configInstance = new Config();

    private Runnable update;
    private long timeInterval = 1000;
    final Handler handler = new Handler();

    public Config() {
    }

    public static Config getConfigInstance() {
        return configInstance;

    }
}
