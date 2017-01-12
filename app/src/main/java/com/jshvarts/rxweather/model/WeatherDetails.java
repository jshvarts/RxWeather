package com.jshvarts.rxweather.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherDetails {
    private double pressure;
    private double humidity;
    private double speed;

    @SerializedName("temp")
    private TemperatureDetails temperatureDetails;

    @SerializedName("weather")
    private List<WeatherDescription> weatherDescriptions;

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public double getSpeed() {
        return speed;
    }

    public TemperatureDetails getTemperatureDetails() {
        return temperatureDetails;
    }

    public List<WeatherDescription> getWeatherDescriptions() {
        return weatherDescriptions;
    }
}
