package com.jshvarts.rxweather.services;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jshvarts.rxweather.BuildConfig;
import com.jshvarts.rxweather.R;
import com.jshvarts.rxweather.entities.WeatherData;
import com.jshvarts.rxweather.infrastruture.RxWeatherApplication;
import com.jshvarts.rxweather.model.WeatherDescription;
import com.jshvarts.rxweather.model.WeatherDetails;
import com.jshvarts.rxweather.model.WeatherListModel;
import com.jshvarts.rxweather.views.WeatherAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class WeatherClient {
    private static String MODE = "json";
    private static String INCLUDE_DAYS = "7";
    private static WeatherClient instance;
    private final WeatherWebService weatherWebService;

    private WeatherClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(RxWeatherApplication.BASE_WEATHER_URL)
                .client(httpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherWebService = retrofit.create(WeatherWebService.class);
    }

    public static WeatherClient newInstance() {
        if (instance == null) {
            instance = new WeatherClient();
        }
        return instance;
    }

    public Observable<WeatherListModel> getWeather(String zip, String units) {
        return weatherWebService.getWeather(zip,
                MODE,
                units,
                INCLUDE_DAYS,
                BuildConfig.WEATHER_API_KEY);
    }

    public List<WeatherData> weatherDataConverter(WeatherListModel weatherListModel, final DatabaseReference appDataRef) {
        final List<WeatherData> weatherDataList = new ArrayList<>();
        int position = 0;
        for (WeatherDetails weatherDetails : weatherListModel.weatherDetailsList) {
            WeatherData weatherData = new WeatherData(
                    weatherDetails.getTemperatureDetails().getMaxTemp(),
                    weatherDetails.getTemperatureDetails().getMinTemp(),
                    getDate(position),
                    null,
                    null,
                    null);

            // now update weather descriptions
            for (WeatherDescription weatherDescription : weatherDetails.getWeatherDescriptions()) {
                weatherData.setWeatherSummary(weatherDescription.getBasicDescription());
                weatherData.setWeatherDetail(weatherDescription.getDetailedDescription());
                weatherData.setIcon(weatherDescription.getIcon());
            }

            weatherDataList.add(weatherData);

            position++;
        }

        appDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    int index = 0;
                    for (WeatherData weatherData : weatherDataList) {
                        appDataRef.child(Integer.toString(index)).setValue(weatherData);
                        index++;
                    }
                } else {
                    int index = 0;
                    for (WeatherData weatherData : weatherDataList) {
                        Map newWeatherData = new HashMap();
                        newWeatherData.put("icon", weatherData.getIcon());
                        newWeatherData.put("maxTemp", weatherData.getMaxTemp());
                        newWeatherData.put("minTemp", weatherData.getMinTemp());
                        newWeatherData.put("weatherDate", weatherData.getWeatherDate());
                        newWeatherData.put("weatherDetail", weatherData.getWeatherDetail());
                        newWeatherData.put("weatherSummary", weatherData.getWeatherSummary());
                        appDataRef.child(Integer.toString(index)).updateChildren(newWeatherData);
                        index++;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return weatherDataList;
    }

    public ValueEventListener readFromDatabase(DatabaseReference appDataRef, final WeatherAdapter weatherAdapter, final Context context) {
        return appDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<WeatherData> weatherDataList = new ArrayList<>();
                if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        WeatherData weatherData = childDataSnapshot.getValue(WeatherData.class);
                        weatherDataList.add(weatherData);
                    }
                }

                if (weatherDataList.isEmpty()) {
                    Toast.makeText(context, R.string.error_message_check_connection, Toast.LENGTH_LONG).show();
                }

                weatherAdapter.setWeatherDataList(weatherDataList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String getDate(int position) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.add(GregorianCalendar.DATE, position);
        return new SimpleDateFormat("EE, MMM d").format(gregorianCalendar.getTime());
    }
}
