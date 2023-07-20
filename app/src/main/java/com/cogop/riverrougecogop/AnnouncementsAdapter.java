package com.cogop.riverrougecogop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.ViewHolder> {

    private List<String> announcementsList;

    public AnnouncementsAdapter(List<String> announcementsList) {
        this.announcementsList = announcementsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String announcement = announcementsList.get(position);
        holder.bind(announcement);
    }

    @Override
    public int getItemCount() {
        return announcementsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewAnnouncement;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAnnouncement = itemView.findViewById(R.id.textView_announcement);
        }

        public void bind(String announcement) {
            textViewAnnouncement.setText(announcement);
        }
    }
}
