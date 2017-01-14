package com.jshvarts.rxweather.entities;

/**
 * Weather POJO.
 */
public class WeatherData {
    private double minTemp;
    private double maxTemp;
    private String weatherDate;
    private String weatherSummary;
    private String weatherDetail;
    private String icon;

    // TODO Apply builder pattern
    public WeatherData(double maxTemp,
                       double minTemp,
                       String weatherDate,
                       String weatherDetail,
                       String weatherSummary,
                       String icon) {
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.weatherDate = weatherDate;
        this.weatherDetail = weatherDetail;
        this.weatherSummary = weatherSummary;
        this.icon = icon;
    }

    public WeatherData() {
    }

    public String getIcon() {
        return icon;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public String getWeatherDate() {
        return weatherDate;
    }

    public String getWeatherDetail() {
        return weatherDetail;
    }

    public String getWeatherSummary() {
        return weatherSummary;
    }

    public void setWeatherDetail(String weatherDetail) {
        this.weatherDetail = weatherDetail;
    }

    public void setWeatherSummary(String weatherSummary) {
        this.weatherSummary = weatherSummary;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
