package com.jshvarts.rxweather.model;

import com.google.gson.annotations.SerializedName;

public class TemperatureDetails {
    @SerializedName("min")
    private String minTemp;

    @SerializedName("max")
    private String maxTemp;

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }
}
