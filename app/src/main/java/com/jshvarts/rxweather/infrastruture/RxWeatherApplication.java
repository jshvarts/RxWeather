package com.jshvarts.rxweather.infrastruture;

import android.app.Application;

public class RxWeatherApplication extends Application {

    public static final String BASE_WEATHER_URL = "http://api.openweathermap.org";

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
