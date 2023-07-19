package com.cogop.riverrougecogop;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.cogop.riverrougecogop.ui.dashboard.DashboardFragment;

public class SettingsActivity extends AppCompatActivity implements SettingsFragment.OnShowTextView44ChangeListener {

    private DashboardFragment dashboardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize the SettingsFragment
        SettingsFragment settingsFragment = new SettingsFragment();

        // Set the listener to handle changes in textView44 visibility
        settingsFragment.setOnShowTextView44ChangeListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, settingsFragment)
                .commit();
    }

    @Override
    public void onShowTextView44Changed(boolean show) {
        // Pass the preference value to the DashboardFragment
        dashboardFragment = (DashboardFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_dashboard);
        if (dashboardFragment != null) {
            dashboardFragment.setShowTextView44(show);
        }
    }
}
