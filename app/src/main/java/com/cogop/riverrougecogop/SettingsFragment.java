package com.cogop.riverrougecogop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String KEY_SHOW_TEXT_VIEW_44 = "show_text_view_44";
    private OnShowTextView44ChangeListener listener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        // Find the SwitchPreference for showing/hiding textView44
        SwitchPreferenceCompat showTextView44Pref = findPreference(KEY_SHOW_TEXT_VIEW_44);
        if (showTextView44Pref != null) {
            showTextView44Pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    // Apply the PreferencesTheme style to the preferences screen
                    getPreferenceManager().setSharedPreferencesName("root_preferences");
                    setPreferencesFromResource(R.xml.root_preferences, rootKey);
                    getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
                    getPreferenceManager().setSharedPreferencesName("root_preferences");
                    boolean showTextView44 = (boolean) newValue;
                    // Notify the activity about the change
                    if (listener != null) {
                        listener.onShowTextView44Changed(showTextView44);
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Set the default value for the switch
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        boolean defaultValue = sharedPreferences.getBoolean(KEY_SHOW_TEXT_VIEW_44, true);
        if (findPreference(KEY_SHOW_TEXT_VIEW_44) instanceof SwitchPreferenceCompat) {
            ((SwitchPreferenceCompat) findPreference(KEY_SHOW_TEXT_VIEW_44)).setChecked(defaultValue);
        }
    }

    public void setOnShowTextView44ChangeListener(OnShowTextView44ChangeListener listener) {
        this.listener = listener;
    }

    public interface OnShowTextView44ChangeListener {
        void onShowTextView44Changed(boolean show);
    }
}
