package com.jshvarts.rxweather.model;

import com.google.gson.annotations.SerializedName;

public class WeatherDescription {
    @SerializedName("main")
    private String basicDescription;

    @SerializedName("description")
    private String detailedDescription;

    private String icon;

    public String getBasicDescription() {
        return basicDescription;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public String getIcon() {
        return icon;
    }
}
