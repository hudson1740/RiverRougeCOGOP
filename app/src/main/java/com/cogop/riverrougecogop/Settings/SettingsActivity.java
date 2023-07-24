package com.cogop.riverrougecogop.Settings;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.cogop.riverrougecogop.R;
import com.cogop.riverrougecogop.Settings.SettingsFragment;
import com.cogop.riverrougecogop.ui.dashboard.DashboardFragment;

public class SettingsActivity extends AppCompatActivity {

    private DashboardFragment dashboardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize the SettingsFragment
        SettingsFragment settingsFragment = new SettingsFragment();


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, settingsFragment)
                .commit();
    }
}
