package com.jshvarts.rxweather.services;

import com.jshvarts.rxweather.BuildConfig;
import com.jshvarts.rxweather.infrastruture.RxWeatherApplication;
import com.jshvarts.rxweather.model.WeatherListModel;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class WeatherClient {
    private static WeatherClient instance;
    private WeatherWebService weatherWebService;

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
    }

    public static WeatherClient newInstance() {
        if (instance == null) {
            instance = new WeatherClient();
        }
        return instance;
    }

    public Observable<WeatherListModel> getWeather(String zip, String units) {
        return weatherWebService.getWeather(zip, "json", units, "7", BuildConfig.WEATHER_API_KEY);
    }
}
