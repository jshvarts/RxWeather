package com.jshvarts.rxweather.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherListModel {
    @SerializedName("list")
    public List<WeatherDetails> weatherDetailsList;
}
