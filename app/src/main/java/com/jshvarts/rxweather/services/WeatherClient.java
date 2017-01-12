package com.jshvarts.rxweather.services;

import com.jshvarts.rxweather.BuildConfig;
import com.jshvarts.rxweather.entities.WeatherData;
import com.jshvarts.rxweather.infrastruture.RxWeatherApplication;
import com.jshvarts.rxweather.model.WeatherDescription;
import com.jshvarts.rxweather.model.WeatherDetails;
import com.jshvarts.rxweather.model.WeatherListModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class WeatherClient {
    private static WeatherClient instance;
    private WeatherWebService weatherWebService;

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
        return weatherWebService.getWeather(zip, "json", units, "7", BuildConfig.WEATHER_API_KEY);
    }

    public List<WeatherData> weatherDataConverter(WeatherListModel weatherListModel) {
        List<WeatherData> weatherDataList = new ArrayList<>();
        int position = 0;
        for (WeatherDetails weatherDetails : weatherListModel.weatherDetailsList) {
            WeatherData weatherData = new WeatherData(
                    weatherDetails.getHumidity(),
                    weatherDetails.getTemperatureDetails().getMaxTemp(),
                    weatherDetails.getTemperatureDetails().getMinTemp(),
                    weatherDetails.getPressure(),
                    getDate(position),
                    "",
                    "");

            // now update weather descriptions
            for (WeatherDescription weatherDescription : weatherDetails.getWeatherDescriptions()) {
                weatherData.setWeatherSummary(weatherDescription.getBasicDescription());
                weatherData.setWeatherDetail(weatherDescription.getDetailedDescription());
            }

            weatherDataList.add(weatherData);

            position++;
        }

        return weatherDataList;
    }

    private String getDate(int position) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.add(GregorianCalendar.DATE, position);
        Date date = gregorianCalendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE MM dd");
        return simpleDateFormat.format(date);
    }
}
