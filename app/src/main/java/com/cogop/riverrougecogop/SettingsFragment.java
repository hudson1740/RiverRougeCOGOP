package com.cogop.riverrougecogop;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String KEY_SHOW_TEXT_VIEW_44 = "show_text_view_44";

    // Listener instance to hold the activity reference
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

    // Setter method to set the listener
    public void setOnShowTextView44ChangeListener(OnShowTextView44ChangeListener listener) {
        this.listener = listener;
    }

    // Interface to communicate with the hosting activity
    public interface OnShowTextView44ChangeListener {
        void onShowTextView44Changed(boolean show);
    }
}
