package com.jshvarts.rxweather.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import com.jshvarts.rxweather.R;
import com.jshvarts.rxweather.infrastruture.RxWeatherApplication;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SortPreferenceFragment())
                .commit();
    }

    public static class SortPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_general);
            bindPreferenceToBooleanValue(findPreference(getString(R.string.preference_auto_location_key)));
            bindPreferenceToStringValue(findPreference(getString(R.string.preference_location_key)));
            bindPreferenceToStringValue(findPreference(getString(R.string.preference_units_key)));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            setPreference(preference, newValue);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (preference instanceof EditTextPreference) {
                editor.putString(RxWeatherApplication.PREFERENCE_LOCATION, newValue.toString()).apply();
            } else if (preference instanceof ListPreference) {
                editor.putString(RxWeatherApplication.PREFERENCE_UNITS, newValue.toString()).apply();
            } else if (preference instanceof SwitchPreference) {
                editor.putBoolean(RxWeatherApplication.PREFERENCE_AUTO_LOCATION, (boolean) newValue).apply();;
            }
            return true;
        }

        private void bindPreferenceToStringValue(Preference preference) {
            if (preference == null) {
                // may happen, for example, when location edit text preference has been removed since current location is used.
                return;
            }
            preference.setOnPreferenceChangeListener(this);
            setPreference(preference, PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext())
                    .getString(preference.getKey(), ""));
        }

        private void bindPreferenceToBooleanValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            setPreference(preference, PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext())
                    .getBoolean(preference.getKey(), false));
        }

        private void setPreference(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int preferenceIndex = listPreference.findIndexOfValue(stringValue);
                if (preferenceIndex >= 0) {
                    preference.setSummary(listPreference.getEntries()[preferenceIndex]);
                }
            } else if (preference instanceof EditTextPreference) {
                preference.setSummary(stringValue);
            } else if (preference instanceof SwitchPreference) {
                ((SwitchPreference) preference).setChecked((boolean) value);
                if (((SwitchPreference) preference).isChecked()) {
                    Preference locationEditTextPreference = findPreference(getString(R.string.preference_location_key));
                    getPreferenceScreen().removePreference(locationEditTextPreference);
                } else {
                    Preference locationEditTextPreference = findPreference(getString(R.string.preference_location_key));
                    if (locationEditTextPreference == null) {
                        locationEditTextPreference = new EditTextPreference(getActivity());
                        locationEditTextPreference.setKey(getString(R.string.preference_location_key));
                        locationEditTextPreference.setTitle(getString(R.string.preference_location_label));
                        locationEditTextPreference.setDefaultValue(R.string.preference_location_default);
                        // set location value to the last known one stored in shared prefs
                        locationEditTextPreference.setSummary(PreferenceManager
                                .getDefaultSharedPreferences(preference.getContext())
                                .getString(getString(R.string.preference_location_key), getString(R.string.preference_location_default)));
                        locationEditTextPreference.setOrder(1);
                        getPreferenceScreen().addPreference(locationEditTextPreference);
                    }
                }
            }
        }
    }
}
