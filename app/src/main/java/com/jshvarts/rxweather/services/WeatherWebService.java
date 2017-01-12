package com.jshvarts.rxweather.services;

import com.jshvarts.rxweather.model.WeatherListModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Web service providing weather data
 */
public interface WeatherWebService {
    @GET("/data/2.5/forecast/daily")
    Observable<WeatherListModel> getWeather(
            @Query("zip") String zip,
            @Query("mode") String mode,
            @Query("units") String units,
            @Query("cnt") String count,
            @Query("APPID") String apiKey
    );
}