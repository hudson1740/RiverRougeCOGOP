package com.cogop.riverrougecogop.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cogop.riverrougecogop.R;

public class Settings extends AppCompatActivity {
    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_VERSE_UPDATE_INTERVAL = "verse_update_interval";
    private static final String KEY_NOTIFICATIONS = "notifications_enabled";

    private Spinner verseUpdateSpinner;
    private Switch notificationSwitch;
    private long selectedInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        verseUpdateSpinner = findViewById(R.id.verseUpdateSpinner);
        notificationSwitch = findViewById(R.id.notificationSwitch);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        selectedInterval = prefs.getLong(KEY_VERSE_UPDATE_INTERVAL, 24 * 60 * 60 * 1000); // Default 24 hours
        boolean notificationsEnabled = prefs.getBoolean(KEY_NOTIFICATIONS, true);

        verseUpdateSpinner.setSelection(getIntervalPosition(selectedInterval));
        notificationSwitch.setChecked(notificationsEnabled);

        verseUpdateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                long[] intervals = {1 * 60 * 60 * 1000, 12 * 60 * 60 * 1000, 24 * 60 * 60 * 1000};
                selectedInterval = intervals[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveSettings());

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    private int getIntervalPosition(long interval) {
        if (interval == 1 * 60 * 60 * 1000) return 0;
        if (interval == 12 * 60 * 60 * 1000) return 1;
        return 2; // Default 24 hours
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putLong(KEY_VERSE_UPDATE_INTERVAL, selectedInterval);
        editor.putBoolean(KEY_NOTIFICATIONS, notificationSwitch.isChecked());
        editor.apply();

        Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.putExtra("verse_update_interval", selectedInterval);
        intent.putExtra("notifications_enabled", notificationSwitch.isChecked());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void backButton(View view) {
        finish();
    }

    public void settings(View view) {}
}