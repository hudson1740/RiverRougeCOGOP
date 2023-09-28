package com.cogop.riverrougecogop.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cogop.riverrougecogop.R;

public class DashboardFragment extends Fragment {
    private TextView textView44; // Reference to the textView44 in DashboardFragment layout
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

      //  textView44 = view.findViewById(R.id.textView44); // Find the textView44 in the DashboardFragment layout
        preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext());

        // Get the preference value and update the visibility of textView44
        boolean showTextView44 = preferences.getBoolean("show_text_view_44", true);
        setShowTextView44(showTextView44);

        // ... Rest of your DashboardFragment setup ...

        return view;
    }

    // Method to update the visibility of textView44
    public void setShowTextView44(boolean show) {
        if (textView44 != null) {
            textView44.setVisibility(show ? View.VISIBLE : View.GONE);
        }

    }
}
