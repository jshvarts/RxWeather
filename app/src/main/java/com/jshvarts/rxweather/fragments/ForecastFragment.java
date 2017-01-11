package com.jshvarts.rxweather.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jshvarts.rxweather.R;
import com.jshvarts.rxweather.entities.WeatherData;
import com.jshvarts.rxweather.views.WeatherAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForecastFragment extends Fragment implements WeatherAdapter.WeatherClickListener {

    private static final String LOG_TAG = ForecastFragment.class.getSimpleName();

    @BindView(R.id.fragment_forecast_recyclerView)
    RecyclerView recyclerView;

    public static ForecastFragment newInstance() {
        return new ForecastFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);
        ButterKnife.bind(this, rootView);

        WeatherAdapter adapter = new WeatherAdapter(getActivity(), this);
        adapter.setWeatherDataList(getWeather());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private List<WeatherData> getWeather() {
        List<WeatherData> weatherDataList = new ArrayList<>();
        WeatherData weatherData;
        for (int i = 0; i < 7; i++) {
            weatherData = new WeatherData(20.0, 50.0, 40.0, 25.5, getDate(i), "clear", "very clear");
            weatherDataList.add(weatherData);
        }
        return weatherDataList;
    }

    private String getDate(int index) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.add(GregorianCalendar.DATE, index);
        Date date = gregorianCalendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE MM dd");
        return simpleDateFormat.format(date);
    }

    @Override
    public void onClicked(WeatherData weatherData) {
        Log.d(LOG_TAG, "weather clicked");
    }
}
