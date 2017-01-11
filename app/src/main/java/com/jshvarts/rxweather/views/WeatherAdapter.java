package com.jshvarts.rxweather.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jshvarts.rxweather.R;
import com.jshvarts.rxweather.entities.WeatherData;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> implements View.OnClickListener {

    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_BODY = 2;
    private final List<WeatherData> weatherDataList;
    private final LayoutInflater inflater;
    private final Context context;
    private final WeatherClickListener listener;

    public WeatherAdapter(Context context, WeatherClickListener listener) {
        this.context = context;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
        weatherDataList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_BODY;
    }

    public void setWeatherDataList(List<WeatherData> weatherDataList) {
        // TODO advantages over this.weatherDataList = weatherDataList?
        this.weatherDataList.clear();
        this.weatherDataList.addAll(weatherDataList);
        notifyDataSetChanged();
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View headerView = inflater.inflate(R.layout.list_weather_today, parent, false);
        View bodyView = inflater.inflate(R.layout.list_weather_item, parent, false);
        if (viewType == VIEW_TYPE_HEADER) {
            headerView.setOnClickListener(this);
            return new WeatherViewHolder(headerView);
        } else {
            bodyView.setOnClickListener(this);
            return new WeatherViewHolder(bodyView);
        }
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        holder.populate(weatherDataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return weatherDataList.size();
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof WeatherData) {
            listener.onClicked((WeatherData) view.getTag());
        }
    }

    public interface WeatherClickListener {
        void onClicked(WeatherData weatherData);
    }
}
