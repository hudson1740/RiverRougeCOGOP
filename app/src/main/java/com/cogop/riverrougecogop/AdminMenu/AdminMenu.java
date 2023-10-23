package com.cogop.riverrougecogop.AdminMenu;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cogop.riverrougecogop.MainActivity;
import com.cogop.riverrougecogop.R;

public class AdminMenu extends AppCompatActivity {

    private DrawerLayout mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_menu);

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

            // Convert deviceUptime to hours, minutes, and seconds
            long seconds = (deviceUptime / 1000) % 60;
            long minutes = (deviceUptime / (1000 * 60)) % 60;
            long hours = (deviceUptime / (1000 * 60 * 60));

            String uptimeFormatted = "Device Uptime: " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds";

            String infoMessage = "App Version: " + appVersion + "\n"
                    + "Device Model: " + deviceModel + "\n"
                    + uptimeFormatted + "\n"
                    + "Android Version: " + androidVersion;

            new AlertDialog.Builder(this)
                    .setTitle("System Info")
                    .setMessage(infoMessage)
                    .setPositiveButton("OK", null)
                    .show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void checkAppInstallation() {
        // Check if specific apps are installed
        PackageManager pm = getPackageManager();
        boolean isZoomInstalled = isAppInstalled("us.zoom.videomeetings", pm);
        boolean isBibleInstalled = isAppInstalled("com.sirma.mobile.bible.android", pm);
        boolean isGoogleMapsInstalled = isAppInstalled("com.google.android.apps.maps", pm);
        boolean isCashAppInstalled = isAppInstalled("com.squareup.cash", pm);

        String message = "App Check Results:\n"
                + "Zoom: " + (isZoomInstalled ? "Installed" : "Not Installed") + "\n"
                + "Bible: " + (isBibleInstalled ? "Installed" : "Not Installed") + "\n"
                + "Google Maps: " + (isGoogleMapsInstalled ? "Installed" : "Not Installed") + "\n"
                + "CashApp: " + (isCashAppInstalled ? "Installed" : "Not Installed");

        new AlertDialog.Builder(this)
                .setTitle("App Check")
                .setMessage(message)
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
        // Retrieve and display the last recorded crash log of the app
        // You'll need to implement the code to get the crash log here
        String lastCrashLog = "Sample crash log:\n\n" +
                "Exception: NullPointerException\n" +
                "Stack trace: ...\n";

        new AlertDialog.Builder(this)
                .setTitle("Last Crash Log")
                .setMessage(lastCrashLog)
                .setPositiveButton("OK", null)
                .show();
    }
}
