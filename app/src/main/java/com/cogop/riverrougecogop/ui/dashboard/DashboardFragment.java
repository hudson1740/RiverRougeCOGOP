package com.cogop.riverrougecogop.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cogop.riverrougecogop.R;
import com.cogop.riverrougecogop.SettingsActivity;
import com.cogop.riverrougecogop.SettingsFragment;

public class DashboardFragment extends Fragment implements SettingsFragment.OnShowTextView44ChangeListener {

    private DashboardViewModel dashboardViewModel;
    private TextView textView44;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        textView44 = root.findViewById(R.id.textView44);

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView44.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Register this DashboardFragment as a listener to the SettingsFragment
        SettingsFragment settingsFragment = (SettingsFragment) getParentFragmentManager().findFragmentById(R.id.settings);
        if (settingsFragment != null) {
            settingsFragment.setOnShowTextView44ChangeListener(this);
        }
    }

    // Implement the interface method to update the textView44 visibility
    @Override
    public void onShowTextView44Changed(boolean show) {
        if (show) {
            textView44.setVisibility(View.VISIBLE);
        } else {
            textView44.setVisibility(View.GONE);
        }
    }
}
