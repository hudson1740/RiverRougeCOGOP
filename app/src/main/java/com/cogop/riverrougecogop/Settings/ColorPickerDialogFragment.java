package com.cogop.riverrougecogop.Settings; // Use your actual package name

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cogop.riverrougecogop.ColorAdapter;
import com.cogop.riverrougecogop.R;

import java.util.Arrays;
import java.util.List;

public class ColorPickerDialogFragment extends DialogFragment implements ColorAdapter.OnColorClickListener {

    public interface ColorPickerDialogListener {
        void onColorSelected(int color);
    }

    private ColorPickerDialogListener listener;
    private RecyclerView colorRecyclerView;
    private ColorAdapter colorAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ColorPickerDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ColorPickerDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.color_picker_layout, null);

        colorRecyclerView = view.findViewById(R.id.colorRecyclerView);
        colorRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4)); // 4 columns

        // Predefined colors
        List<Integer> colorList = Arrays.asList(
                Color.BLACK, Color.DKGRAY, Color.GRAY, Color.LTGRAY,
                Color.WHITE, Color.RED, Color.GREEN, Color.BLUE,
                Color.YELLOW, Color.CYAN, Color.MAGENTA
        );

        colorAdapter = new ColorAdapter(colorList, this);
        colorRecyclerView.setAdapter(colorAdapter);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle("Choose Color")
                .setPositiveButton("OK", (dialog, id) -> {
                    // Listener handles the color selection
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    // User cancelled the dialog
                });
        return builder.create();
    }

    @Override
    public void onColorClick(int color) {
        if (listener != null) {
            listener.onColorSelected(color);
            dismiss(); // Dismiss the dialog after color selection
        }
    }
}