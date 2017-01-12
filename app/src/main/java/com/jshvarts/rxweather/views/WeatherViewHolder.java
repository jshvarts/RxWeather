package com.jshvarts.rxweather.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jshvarts.rxweather.R;
import com.jshvarts.rxweather.entities.WeatherData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.list_weather_item_date)
    protected TextView weatherDate;

    @BindView(R.id.list_weather_item_imageView)
    protected ImageView weatherImage;

    @BindView(R.id.list_weather_item_minTemp)
    protected TextView minTemp;

    @BindView(R.id.list_weather_item_maxTemp)
    protected TextView maxTemp;

    @BindView(R.id.list_weather_item_weatherDetail)
    protected TextView weatherDetail;

    public WeatherViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void populate(WeatherData weatherData, int position) {
        itemView.setTag(weatherData);

        String date;

        if (position == 0) {
            date = "Today";
        } else if (position == 1) {
            date = "Tomorrow";
        } else {
            date = weatherData.getWeatherDate();
        }

        weatherDate.setText(date);
        weatherImage.setImageResource(R.mipmap.ic_launcher);
        minTemp.setText(getRoundedWeatherTemp(weatherData.getMinTemp()));
        maxTemp.setText(getRoundedWeatherTemp(weatherData.getMaxTemp()));
        weatherDetail.setText(weatherData.getWeatherDetail());
    }

    private String getRoundedWeatherTemp(double weatherTemp) {
        final double roundedTemp = Math.round(weatherTemp);
        return Double.toString(roundedTemp);
    }
}