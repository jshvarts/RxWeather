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
import com.jshvarts.rxweather.model.WeatherListModel;
import com.jshvarts.rxweather.services.WeatherClient;
import com.jshvarts.rxweather.views.WeatherAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ForecastFragment extends Fragment implements WeatherAdapter.WeatherClickListener {

    private static final String LOG_TAG = ForecastFragment.class.getSimpleName();

    @BindView(R.id.fragment_forecast_recyclerView)
    RecyclerView recyclerView;

    private WeatherAdapter adapter;
    private Subscription weatherSubscription;
    private Observer<List<WeatherData>> weatherObserver = new Observer<List<WeatherData>>() {
        @Override
        public void onCompleted() {
            Log.d(LOG_TAG, "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.d(LOG_TAG, "onError " + e);
        }

        @Override
        public void onNext(List<WeatherData> weatherDataList) {
            Log.d(LOG_TAG, "onNext");
            adapter.setWeatherDataList(weatherDataList);
        }
    };

    public static ForecastFragment newInstance() {
        return new ForecastFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);
        ButterKnife.bind(this, rootView);

        adapter = new WeatherAdapter(getActivity(), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        getWeather("10036", "imperial");

        return rootView;
    }

    private void getWeather(String zip, String units) {
            weatherSubscription = WeatherClient.newInstance()
                    .getWeather(zip, units)
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<WeatherListModel, List<WeatherData>>() {
                        @Override
                        public List<WeatherData> call(WeatherListModel weatherListModel) {
                            return WeatherClient.newInstance().weatherDataConverter(weatherListModel);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(weatherObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (weatherSubscription != null && !weatherSubscription.isUnsubscribed()) {
            weatherSubscription.unsubscribe();
        }
    }

    @Override
    public void onClicked(WeatherData weatherData) {
        Log.d(LOG_TAG, "weather clicked");
    }
}
