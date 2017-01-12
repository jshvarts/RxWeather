package com.jshvarts.rxweather.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Weather POJO.
 */
public class WeatherData implements Parcelable {
    private double minTemp;
    private double maxTemp;
    private double pressure;
    private double humidity;
    private String weatherSummary;
    private String weatherDetail;
    private String weatherDate;

    public WeatherData(double humidity, double maxTemp, double minTemp, double pressure, String weatherDate, String weatherDetail, String weatherSummary) {
        this.humidity = humidity;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.pressure = pressure;
        this.weatherDate = weatherDate;
        this.weatherDetail = weatherDetail;
        this.weatherSummary = weatherSummary;
    }

    protected WeatherData(Parcel in) {
        minTemp = in.readDouble();
        maxTemp = in.readDouble();
        pressure = in.readDouble();
        humidity = in.readDouble();
        weatherSummary = in.readString();
        weatherDetail = in.readString();
        weatherDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(minTemp);
        dest.writeDouble(maxTemp);
        dest.writeDouble(pressure);
        dest.writeDouble(humidity);
        dest.writeString(weatherSummary);
        dest.writeString(weatherDetail);
        dest.writeString(weatherDate);
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

    public double getHumidity() {
        return humidity;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getPressure() {
        return pressure;
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
}
