package com.cogop.riverrougecogop;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MeetOurLeadersFragment extends BottomSheetDialogFragment {

    public MeetOurLeadersFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meet_leaders, container, false);

        ScrollView scrollView = view.findViewById(R.id.scrollView);
        TextView leonardButton = view.findViewById(R.id.leonardButton);
        TextView desereneButton = view.findViewById(R.id.desereneButton);

        leonardButton.setOnClickListener(v ->
                scrollView.smoothScrollTo(0, view.findViewById(R.id.leonardCard).getTop()));

        desereneButton.setOnClickListener(v ->
                scrollView.smoothScrollTo(0, view.findViewById(R.id.desereneCard).getTop()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        if (dialog != null) {
            FrameLayout bottomSheet = dialog.findViewById(
                    com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(bottomSheet);

                // Set peek height to 400dp (default collapsed height)
                int peekHeight = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        700,
                        getResources().getDisplayMetrics()
                );
                behavior.setPeekHeight(peekHeight);

                // Allow full expansion
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                behavior.setHideable(true); // Allow swiping down to dismiss
                behavior.setSkipCollapsed(false); // Let user expand/collapse freely
            }
        }
    }
}
