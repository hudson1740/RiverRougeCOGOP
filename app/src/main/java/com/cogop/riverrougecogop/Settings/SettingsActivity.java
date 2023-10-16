package com.cogop.riverrougecogop.Settings;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cogop.riverrougecogop.MainActivity;
import com.cogop.riverrougecogop.R;
import com.cogop.riverrougecogop.Settings.SettingsFragment;
import com.cogop.riverrougecogop.ui.dashboard.DashboardFragment;

import java.util.Objects;

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
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle the up button click
        onBackPressed();
        return true;
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the back stack
        startActivity(intent);
        finish(); // Finish the current activity
    }

    @Override
    public void onBackPressed() {
        navigateToMainActivity();
    }
}
