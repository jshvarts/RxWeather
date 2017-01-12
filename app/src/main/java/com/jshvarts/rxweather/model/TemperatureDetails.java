package com.jshvarts.rxweather.model;

import com.google.gson.annotations.SerializedName;

public class TemperatureDetails {
    @SerializedName("min")
    private double minTemp;

    @SerializedName("max")
    private double maxTemp;

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }
}
