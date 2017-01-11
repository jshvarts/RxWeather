package com.jshvarts.rxweather.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jshvarts.rxweather.R;
import com.jshvarts.rxweather.fragments.ForecastFragment;

public class MainActivity extends BaseFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return ForecastFragment.newInstance();
    }
}
