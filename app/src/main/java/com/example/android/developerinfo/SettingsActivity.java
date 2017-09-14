package com.example.android.developerinfo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by USER on 8/25/2017.
 */

public class SettingsActivity extends AppCompatActivity {
    //This is to enable users change location and programming language of the developer from default
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class DeveloperPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        //To hold the language preference as a global variable
        Preference languagePreference;
        //to hold the location preference as a global variable
        Preference locationPreference;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
            languagePreference = findPreference(getString(R.string.settings_language_key));
            Preference original = findPreference(getString(R.string.settings_defaultBehavior_key));
            locationPreference = findPreference(getString(R.string.settings_location_key));

            bind(languagePreference);
            bind(original);
            bind(locationPreference);
        }
//this changes the shared preference object based on users preferred values
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String s = newValue.toString();
            if (preference instanceof ListPreference) {
                ListPreference lp = (ListPreference) preference;
                int index = lp.findIndexOfValue(s);
                //checks if user selected yes checkbox (index==0) and hides preferences for the location and language if this is selected
                if (index == 0) {
                    PreferenceScreen ps = getPreferenceScreen();
                    ps.removePreference(languagePreference);
                    ps.removePreference(locationPreference);
                    //checks if user selected no checkbox (index==1) and shows preferences for the location and language if this is selected
                } else {
                    PreferenceScreen ps = getPreferenceScreen();
                    ps.addPreference(languagePreference);
                    ps.addPreference(locationPreference);
                }
            }
            preference.setSummary(s);
            return true;
        }

        private void bind(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String value = sharedPreferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, value);
        }
    }

}
