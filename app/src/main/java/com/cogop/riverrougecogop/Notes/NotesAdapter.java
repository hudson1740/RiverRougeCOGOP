package com.cogop.riverrougecogop.Notes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cogop.riverrougecogop.Announcements.Announcements;
import com.cogop.riverrougecogop.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
LayoutInflater inflater;
List<Note> notes;

private OnDeleteClickListener onDeleteClickListener;
NotesAdapter(Context context,List<Note> notes, OnDeleteClickListener onDeleteClickListener){
    this.inflater = LayoutInflater.from(context);
    this.notes = notes;
    this.onDeleteClickListener = onDeleteClickListener;
}

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_list_view,viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder viewHolder, int i) {
        String title = notes.get(i).getNoteTitle();
        viewHolder.nTitle.setText(title);
        viewHolder.nDetails.setText(notes.get(i).getNoteDescription());
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClickListener.onItemDelete(notes.get(i));
            }
        });
        Date date = new Date(notes.get(i).getLastModified());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd - kk:mm", Locale.getDefault());
        String time = simpleDateFormat.format(date);
        viewHolder.nDate.setText(time);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nTitle,nDetails,nDate;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nTitle = itemView.findViewById(R.id.nTitle);
            nDetails = itemView.findViewById(R.id.nDetails);
            delete = itemView.findViewById(R.id.deleteIcon);
            nDate = itemView.findViewById(R.id.nDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickListener.onItemClick(notes.get(getAbsoluteAdapterPosition()));
                }
            });
        }
    }
    interface OnDeleteClickListener {
        void onItemDelete(Note note);

        void onItemClick(Note note);
    }}