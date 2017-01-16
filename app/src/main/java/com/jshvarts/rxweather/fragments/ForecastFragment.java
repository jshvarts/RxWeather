package com.jshvarts.rxweather.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jshvarts.rxweather.R;
import com.jshvarts.rxweather.entities.WeatherData;
import com.jshvarts.rxweather.infrastruture.RxWeatherApplication;
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

    private DatabaseReference dataRef = FirebaseDatabase.getInstance().getReferenceFromUrl(RxWeatherApplication.BASE_FIREBASE_URL);

    @BindView(R.id.swipe_refresh_container)
    protected SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fragment_forecast_recyclerView)
    protected RecyclerView recyclerView;

    private WeatherAdapter adapter;
    private Subscription weatherSubscription;
    private String appId;
    private ValueEventListener valueEventListener;
    private String location;
    private String units;

    private Observer<List<WeatherData>> retrofitWeatherObserver = new Observer<List<WeatherData>>() {
        @Override
        public void onCompleted() {
            Log.d(LOG_TAG, "onCompleted");
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {
            Log.d(LOG_TAG, "onError " + e);
            swipeRefreshLayout.setRefreshing(false);

            Toast.makeText(getActivity(), R.string.error_message_generic, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNext(List<WeatherData> weatherDataList) {
            Log.d(LOG_TAG, "onNext");
            adapter.setWeatherDataList(weatherDataList);
        }
    };

    private Observer<List<WeatherData>> firebaseWeatherObserver = new Observer<List<WeatherData>>() {
        @Override
        public void onCompleted() {
            Log.d(LOG_TAG, "onCompleted");
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {
            Log.d(LOG_TAG, "onError " + e);
            swipeRefreshLayout.setRefreshing(false);

            // read data while offline
            valueEventListener = WeatherClient.newInstance().readFromDatabase(dataRef.child(appId), adapter, getActivity());
            dataRef.child(appId).addValueEventListener(valueEventListener);
            Toast.makeText(getActivity(), R.string.error_message_offline, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNext(List<WeatherData> weatherDataList) {
            Log.d(LOG_TAG, "onNext");
            valueEventListener = WeatherClient.newInstance().readFromDatabase(dataRef.child(appId), adapter, getActivity());
            dataRef.child(appId).addValueEventListener(valueEventListener);

            if (units.equals("Metric")) {
                adapter.setIsMetric(true);
            } else {
                adapter.setIsMetric(false);
            }
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

        SharedPreferences appPreferences = getActivity()
                .getSharedPreferences(RxWeatherApplication.APP_ID_PREFERENCE, Context.MODE_PRIVATE);
        appId = appPreferences.getString(RxWeatherApplication.APP_ID, "");
        if (appId.isEmpty()) {
            // create unique id and store it
            appId = dataRef.push().getKey();
            appPreferences.edit().putString(RxWeatherApplication.APP_ID, appId).apply();
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        location = sharedPreferences.getString(RxWeatherApplication.PREFERENCE_LOCATION, getString(R.string.preference_location_default));
        units = sharedPreferences.getString(RxWeatherApplication.PREFERENCE_UNITS, getString(R.string.preference_units_entry_imperial_value));

        adapter = new WeatherAdapter(getActivity(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWeather(location, units);
            }
        });

        getWeather(location, units);
    }

    private void getWeather(String zip, String units) {
            swipeRefreshLayout.setRefreshing(true);

            weatherSubscription = WeatherClient.newInstance()
                    .getWeather(zip, units)
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<WeatherListModel, List<WeatherData>>() {
                        @Override
                        public List<WeatherData> call(WeatherListModel weatherListModel) {
                            return WeatherClient.newInstance().weatherDataConverter(weatherListModel, dataRef.child(appId));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(firebaseWeatherObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (weatherSubscription != null && !weatherSubscription.isUnsubscribed()) {
            weatherSubscription.unsubscribe();
        }
        if (valueEventListener != null) {
            dataRef.child(appId).removeEventListener(valueEventListener);
        }
    }

    @Override
    public void onClicked(WeatherData weatherData) {
        Log.d(LOG_TAG, "weather clicked");
    }
}
