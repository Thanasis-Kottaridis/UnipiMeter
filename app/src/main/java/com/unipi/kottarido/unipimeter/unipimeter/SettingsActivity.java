package com.unipi.kottarido.unipimeter.unipimeter;

import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity {

    public static final String KEY_PREFERENCES_SPEED_LIMIT ="SpeedLimit_Settings";
    public static final String KEY_PREFERENCES_DISTANCE_RANGE = "DistanceRange_Settings";
    public static final String KEY_PREFERENCES_VOICE_ALERT = "VoiceAlert_Settings";

    private AppCompatDelegate mDelegate;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDelegate().getSupportActionBar().setTitle("Settings");

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

//        preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String speedLimit = preferences.getString(KEY_PREFERENCES_SPEED_LIMIT,"50");
//        String range = preferences.getString(KEY_PREFERENCES_DISTANCE_RANGE,"100");
//        Boolean voice = preferences.getBoolean(KEY_PREFERENCES_VOICE_ALERT,true);

    }

    public static class MyPreferenceFragment extends PreferenceFragment{
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

    //event Sto patima tou default back button tou android
    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}
