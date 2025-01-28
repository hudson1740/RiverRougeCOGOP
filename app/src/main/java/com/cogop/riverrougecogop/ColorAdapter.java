package com.cogop.riverrougecogop;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cogop.riverrougecogop.R;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    private List<Integer> colorList;
    private OnColorClickListener listener;

    public interface OnColorClickListener {
        void onColorClick(int color);
    }

    public ColorAdapter(List<Integer> colorList, OnColorClickListener listener) {
        this.colorList = colorList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_item, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        int color = colorList.get(position);
        holder.colorView.setBackgroundColor(color);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onColorClick(color);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public static class ColorViewHolder extends RecyclerView.ViewHolder {
        View colorView;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            colorView = itemView.findViewById(R.id.colorView);
        }
    }
}