package com.jshvarts.rxweather.infrastruture;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class RxWeatherApplication extends Application {

    public static final String BASE_WEATHER_URL = "http://api.openweathermap.org";
    public static final String WEATHER_ICON_URL_PATTERN = "http://openweathermap.org/img/w/%s.png";

    public static final String PREFERENCE_AUTO_LOCATION = "PREFERENCE_AUTO_LOCATION";
    public static final String PREFERENCE_LOCATION = "PREFERENCE_LOCATION";
    public static final String PREFERENCE_UNITS = "PREFERENCE_UNITS";

    public static final String BASE_FIREBASE_URL = "https://rxweather-43817.firebaseio.com/data/";

    public static final String APP_ID_PREFERENCE = "APP_ID_PREFERENCE";
    public static final String APP_ID = "APP_ID";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
