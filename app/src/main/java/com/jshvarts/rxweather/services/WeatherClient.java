package com.jshvarts.rxweather.services;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jshvarts.rxweather.BuildConfig;
import com.jshvarts.rxweather.entities.WeatherData;
import com.jshvarts.rxweather.infrastruture.RxWeatherApplication;
import com.jshvarts.rxweather.model.WeatherDescription;
import com.jshvarts.rxweather.model.WeatherDetails;
import com.jshvarts.rxweather.model.WeatherListModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class WeatherClient {
    private static String MODE = "json";
    private static String INCLUDE_DAYS = "14";
    private static WeatherClient instance;
    private final WeatherWebService weatherWebService;
    private DatabaseReference dbRef = null;

    private WeatherClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(RxWeatherApplication.BASE_WEATHER_URL)
                .client(httpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherWebService = retrofit.create(WeatherWebService.class);

        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    public static WeatherClient newInstance() {
        if (instance == null) {
            instance = new WeatherClient();
        }
        return instance;
    }

    public Observable<WeatherListModel> getWeather(String zip, String units) {
        return weatherWebService.getWeather(zip,
                MODE,
                units,
                INCLUDE_DAYS,
                BuildConfig.WEATHER_API_KEY);
    }

    public List<WeatherData> weatherDataConverter(WeatherListModel weatherListModel) {
        final List<WeatherData> weatherDataList = new ArrayList<>();
        int position = 0;
        for (WeatherDetails weatherDetails : weatherListModel.weatherDetailsList) {
            WeatherData weatherData = new WeatherData(
                    weatherDetails.getTemperatureDetails().getMaxTemp(),
                    weatherDetails.getTemperatureDetails().getMinTemp(),
                    getDate(position),
                    null,
                    null,
                    null);

            // now update weather descriptions
            for (WeatherDescription weatherDescription : weatherDetails.getWeatherDescriptions()) {
                weatherData.setWeatherSummary(weatherDescription.getBasicDescription());
                weatherData.setWeatherDetail(weatherDescription.getDetailedDescription());
                weatherData.setIcon(weatherDescription.getIcon());
            }

            weatherDataList.add(weatherData);

            position++;
        }

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    int index = 0;
                    for (WeatherData weatherData : weatherDataList) {
                        dbRef.child(Integer.toString(index)).setValue(weatherData);
                        index++;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return weatherDataList;
    }

    private String getDate(int position) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.add(GregorianCalendar.DATE, position);
        return new SimpleDateFormat("EE, MMM d").format(gregorianCalendar.getTime());
    }
}
