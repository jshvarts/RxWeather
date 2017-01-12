package com.jshvarts.rxweather.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Weather POJO.
 */
public class WeatherData implements Parcelable {
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

    protected WeatherData(Parcel in) {
        minTemp = in.readDouble();
        maxTemp = in.readDouble();
        weatherDate = in.readString();
        weatherSummary = in.readString();
        weatherDetail = in.readString();
        icon = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(minTemp);
        dest.writeDouble(maxTemp);
        dest.writeString(weatherDate);
        dest.writeString(weatherSummary);
        dest.writeString(weatherDetail);
        dest.writeString(icon);
    }

    public static final Creator<WeatherData> CREATOR = new Creator<WeatherData>() {
        @Override
        public WeatherData createFromParcel(Parcel in) {
            return new WeatherData(in);
        }

        @Override
        public WeatherData[] newArray(int size) {
            return new WeatherData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
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
