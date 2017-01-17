package com.jshvarts.rxweather.services;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import rx.Observable;
import rx.Subscriber;

/**
 * Implement an Rx-style location service by wrapping the Android LocationManager and providing
 * the location result as an Observable.
 *
 * Kudos to https://github.com/vyshane/rex-weather/blob/master/app/src/main/java/mu/node/rexweather/app/Services/LocationService.java
 */
public class LocationService {
    private static final String LOG_TAG = LocationService.class.getSimpleName();

    private final LocationManager locationManager;

    public LocationService(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public Observable<Location> getLocation() {
        return Observable.create(new Observable.OnSubscribe<Location>() {
            @Override
            public void call(final Subscriber<? super Location> subscriber) {
                final LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(final Location location) {
                        Log.d(LOG_TAG, "onLocationChanged");
                        subscriber.onNext(location);
                        subscriber.onCompleted();
                        Looper.myLooper().quit();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Log.d(LOG_TAG, "onStatusChanged");
                        // do nothing
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        Log.d(LOG_TAG, "onProviderEnabled");
                        // do nothing
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Log.d(LOG_TAG, "onProviderDisabled");
                        // do nothing
                    }
                };

                final Criteria locationCriteria = new Criteria();
                locationCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
                locationCriteria.setPowerRequirement(Criteria.POWER_LOW);
                final String locationProvider = locationManager
                        .getBestProvider(locationCriteria, true);

                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }

                try {

                    locationManager.requestSingleUpdate(locationProvider,
                            locationListener, Looper.myLooper());
                } catch (SecurityException e) {
                    Log.d(LOG_TAG, "No Location permission given");
                }

                Looper.loop();
            }
        });
    }
}
