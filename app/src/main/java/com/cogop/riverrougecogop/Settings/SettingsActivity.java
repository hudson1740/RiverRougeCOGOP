package com.cogop.riverrougecogop.Settings;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cogop.riverrougecogop.R;
import com.cogop.riverrougecogop.Settings.SettingsFragment;
import com.cogop.riverrougecogop.ui.dashboard.DashboardFragment;

public class SettingsActivity extends AppCompatActivity {

    private DashboardFragment dashboardFragment;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume() called");

        boolean isZoomInstalled = isZoomAppInstalled("us.zoom.videomeetings");
        TextView myTextView = findViewById(R.id.textView44);

        if (isZoomInstalled) {
            // Zoom is installed
            myTextView.setTextColor(Color.parseColor("#32CD32")); // Green color
            myTextView.setText("Zoom installed: YES");
            Log.d("ZoomInstallation", "Zoom is installed.");
        } else {
            // Zoom is not installed
            myTextView.setTextColor(Color.parseColor("#ECF400")); // Yellow color
            myTextView.setText("Zoom Installed: NO");
            Log.d("ZoomInstallation", "Zoom is not installed.");
        }
    }

    public boolean isZoomAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true; // Package is installed
        } catch (PackageManager.NameNotFoundException e) {
            return false; // Package is not installed
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

// Replace the existing fragment with the SettingsFragment
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()) // Use the correct Fragment type
                .commit();

        // Initialize the SettingsFragment
        SettingsFragment settingsFragment = new SettingsFragment();


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, settingsFragment)
                .commit();
    }
}
