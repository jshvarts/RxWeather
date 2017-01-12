package com.jshvarts.rxweather.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherDetails {
    @SerializedName("temp")
    private TemperatureDetails temperatureDetails;

    @SerializedName("weather")
    private List<WeatherDescription> weatherDescriptions;

    public TemperatureDetails getTemperatureDetails() {
        return temperatureDetails;
    }

    public List<WeatherDescription> getWeatherDescriptions() {
        return weatherDescriptions;
    }
}
