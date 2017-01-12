package com.jshvarts.rxweather.infrastruture;

import android.app.Application;

public class RxWeatherApplication extends Application {

    public static final String BASE_WEATHER_URL = "http://api.openweathermap.org";

    public static final String WEATHER_ICON_URL_PATTERN = "http://openweathermap.org/img/w/%s.png";

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
