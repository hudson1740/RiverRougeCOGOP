package com.cogop.riverrougecogop.Settings; // Use your actual package name

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cogop.riverrougecogop.MainActivity;
import com.cogop.riverrougecogop.R;
import com.cogop.riverrougecogop.Settings.ColorPickerDialogFragment; // Import the DialogFragment


public class Settings extends AppCompatActivity implements ColorPickerDialogFragment.ColorPickerDialogListener {

    private static final String PREFS_NAME = "widget_settings";
    private static final String KEY_VERSE_COLOR = "verse_color";
    private static final String KEY_BACKGROUND_COLOR = "background_color";

    private TextView verseColorDisplay;
    private TextView backgroundColorDisplay;
    private int selectedVerseColor;
    private int selectedBackgroundColor;
    private boolean isVerseColor; // Add this variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        verseColorDisplay = findViewById(R.id.verseColorDisplay);
        backgroundColorDisplay = findViewById(R.id.backgroundColorDisplay);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        selectedVerseColor = prefs.getInt(KEY_VERSE_COLOR, ContextCompat.getColor(this, android.R.color.black));
        selectedBackgroundColor = prefs.getInt(KEY_BACKGROUND_COLOR, ContextCompat.getColor(this, android.R.color.white));

        updateColorDisplays();

        Button chooseVerseColorButton = findViewById(R.id.chooseVerseColorButton);
        chooseVerseColorButton.setOnClickListener(v -> showColorPickerDialog(true));

        Button chooseBackgroundColorButton = findViewById(R.id.chooseBackgroundColorButton);
        chooseBackgroundColorButton.setOnClickListener(v -> showColorPickerDialog(false));

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveColors());
    }

    private void updateColorDisplays() {
        verseColorDisplay.setBackgroundColor(selectedVerseColor);
        backgroundColorDisplay.setBackgroundColor(selectedBackgroundColor);
    }

    private void showColorPickerDialog(final boolean isVerseColor) {
        this.isVerseColor = isVerseColor; // Set the variable
        ColorPickerDialogFragment dialog = new ColorPickerDialogFragment();
        dialog.show(getSupportFragmentManager(), "color_picker");
    }

    @Override
    public void onColorSelected(int color) {
        if (isVerseColor) { // Now you can use it here
            selectedVerseColor = color;
        } else {
            selectedBackgroundColor = color;
        }
        updateColorDisplays();
    }

    private void saveColors() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(KEY_VERSE_COLOR, selectedVerseColor);
        editor.putInt(KEY_BACKGROUND_COLOR, selectedBackgroundColor);
        editor.apply();

        Toast.makeText(this, "Widget settings saved!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        sendBroadcast(intent);
    }

    public void backButton(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}