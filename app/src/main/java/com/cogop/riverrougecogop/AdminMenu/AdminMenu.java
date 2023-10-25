package com.cogop.riverrougecogop.AdminMenu;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.cogop.riverrougecogop.MainActivity;
import com.cogop.riverrougecogop.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;

public class AdminMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_menu);
        FirebaseApp.initializeApp(this);

        TextView systemInfoButton = findViewById(R.id.systemInfoButton);
        TextView appCheckButton = findViewById(R.id.appCheckButton);
        TextView crashLogsButton = findViewById(R.id.crashLogsButton);
        TextView accessibilityButton = findViewById(R.id.accessibilityButton);

        systemInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSystemInfo();
            }
        });

        appCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAppInstallation();
            }
        });

        crashLogsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLastCrashLog();
            }
        });
    }

    public void homebtn(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showSystemInfo() {
        // Retrieve system information and display in a dialog
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String appVersion = packageInfo.versionName;
            String deviceModel = android.os.Build.MODEL;
            long deviceUptime = android.os.SystemClock.uptimeMillis();
            String androidVersion = android.os.Build.VERSION.RELEASE;

            // Convert deviceUptime to hours, minutes, and seconds for uptime
            long seconds = (deviceUptime / 1000) % 60;
            long minutes = (deviceUptime / (1000 * 60)) % 60;
            long hours = (deviceUptime / (1000 * 60 * 60));

            String uptimeFormatted = "Device Uptime: " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds";

            // Check for the latest software updates
            String softwareUpdateStatus = checkForSoftwareUpdates();

            // Display system information in a dialog
            String infoMessage =
                      "App Version: " + appVersion + "\n"
                    + "Device Model: " + deviceModel + "\n"
                    + uptimeFormatted + "\n"
                    + "Android Version: " + androidVersion + "\n"
                    + "Software Updates: " + softwareUpdateStatus;

            new AlertDialog.Builder(this)
                    .setTitle("System Info")
                    .setMessage(infoMessage)
                    .setIcon(R.drawable.ic_baseline_info_24)
                    .setPositiveButton("OK", null)
                    .show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    //check for latest software update
    private String checkForSoftwareUpdates() {
        String currentAndroidVersion = android.os.Build.VERSION.RELEASE;
        String deviceModel = android.os.Build.MODEL;

        // Use deviceModel to query official Android sources or APIs to get the latest supported version for the user's device.

        String latestAndroidVersion = getLatestAndroidVersionForDevice(deviceModel);

        if (latestAndroidVersion != null && currentAndroidVersion.equals(latestAndroidVersion)) {
            return "Up to date ";
        } else {
            return "No ";
        }
    }
    // This is a placeholder method for querying the latest supported version based on device model.
    private String getLatestAndroidVersionForDevice(String deviceModel) {
        // Implement the logic to query official Android sources or APIs to get the latest version for the given device model.
        // You may need to make network requests or access external data sources.
        // Return the latest version or null if it's not available.
        return "Latest Version "; // Replace with actual logic.
    }
// end of check for software update

    private void checkAppInstallation() {
        // Check if specific apps are installed
        PackageManager pm = getPackageManager();
        boolean isZoomInstalled = isAppInstalled("us.zoom.videomeetings", pm);
        boolean isBibleInstalled = isAppInstalled("com.sirma.mobile.bible.android", pm);
        boolean isGoogleMapsInstalled = isAppInstalled("com.google.android.apps.maps", pm);
        boolean isCashAppInstalled = isAppInstalled("com.squareup.cash", pm);

        String message = "App Check Results:\n" + "\n"
                + "Zoom: " + (isZoomInstalled ? "Installed" : "Not Installed") + "\n"
                + "Bible: " + (isBibleInstalled ? "Installed" : "Not Installed") + "\n"
                + "Google Maps: " + (isGoogleMapsInstalled ? "Installed" : "Not Installed") + "\n"
                + "CashApp: " + (isCashAppInstalled ? "Installed" : "Not Installed");

        new AlertDialog.Builder(this)
                .setTitle("App Check")
                .setMessage(message)
                .setIcon(R.drawable.apps)
                .setPositiveButton("OK", null)
                .show();
    }

    private boolean isAppInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void showLastCrashLog() {
        FirebaseCrashlytics.getInstance().checkForUnsentReports();

        FirebaseCrashlytics.getInstance().sendUnsentReports();

        new AlertDialog.Builder(this)
                .setTitle("Crash Logs")
                .setMessage("Crash logs have been sent to Firebase Crashlytics for analysis.")
                .setIcon(R.drawable.error)
                .setPositiveButton("OK", null)
                .show();
    }


}
