package com.jshvarts.rxweather.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jshvarts.rxweather.R;
import com.jshvarts.rxweather.entities.WeatherData;
import com.jshvarts.rxweather.infrastruture.RxWeatherApplication;
import com.squareup.picasso.Picasso;

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

    public void populate(WeatherData weatherData, int position, boolean isMetric) {
        itemView.setTag(weatherData);

        String date;

        if (position == 0) {
            date = "Today";
        } else {
            date = weatherData.getWeatherDate();
        }

        weatherDate.setText(date);
        Picasso.with(itemView.getContext())
                .load(getImageUrl(weatherData.getIcon()))
                .into(weatherImage);

        if (isMetric) {
            maxTemp.setText(itemView.getContext().getString(R.string.format_temperature, getRoundedWeatherTemp(weatherData.getMaxTemp()), "C"));
            minTemp.setText(itemView.getContext().getString(R.string.format_temperature, getRoundedWeatherTemp(weatherData.getMinTemp()), "C"));
        } else {
            maxTemp.setText(itemView.getContext().getString(R.string.format_temperature, getRoundedWeatherTemp(weatherData.getMaxTemp()), "F"));
            minTemp.setText(itemView.getContext().getString(R.string.format_temperature, getRoundedWeatherTemp(weatherData.getMinTemp()), "F"));
        }

        weatherDetail.setText(weatherData.getWeatherDetail());
    }

    private String getRoundedWeatherTemp(double weatherTemp) {
        final long roundedTemp = Math.round(weatherTemp);
        return Long.toString(roundedTemp);
    }

    private String getImageUrl(String icon) {
        return String.format(RxWeatherApplication.WEATHER_ICON_URL_PATTERN, icon);
    }
}
