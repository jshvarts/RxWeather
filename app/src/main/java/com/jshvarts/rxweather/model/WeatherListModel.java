package com.jshvarts.rxweather.model;

import com.google.gson.annotations.SerializedName;

public class WeatherListModel {
    @SerializedName("list")
    private WeatherListModel weatherListModel;

    public WeatherListModel getWeatherListModel() {
        return weatherListModel;
    }
}
