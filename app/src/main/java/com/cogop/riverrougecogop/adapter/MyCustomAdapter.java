package com.cogop.riverrougecogop.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cogop.riverrougecogop.Notes.Note;
import com.cogop.riverrougecogop.R;
import com.cogop.riverrougecogop.VideoPlayActivity;
import com.cogop.riverrougecogop.model.VideoDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyCustomAdapter extends BaseAdapter {


    Activity activity;
    ArrayList<VideoDetails> videoDetailsArrayList;
    LayoutInflater inflater;


    public MyCustomAdapter(Activity activity, ArrayList<VideoDetails> videoDetailsArrayList) {
        this.activity = activity;
        this.videoDetailsArrayList = videoDetailsArrayList;
    }

    @Override
    public Object getItem(int position) {
        return this.videoDetailsArrayList.get(position);

    }

    @Override
    public long getItemId(int position) {

        return (long) position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = this.activity.getLayoutInflater();
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_home, null);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        TextView textView = (TextView) convertView.findViewById(R.id.mytitle);

        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.root);
        VideoDetails videoDetails = (VideoDetails) this.videoDetailsArrayList.get(position);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, VideoPlayActivity.class);
                i.putExtra("videoId", videoDetails.getVideoId());
                activity.startActivity(i);

            }
        });

        Picasso.get().load(videoDetails.getUrl()).into(imageView);

        textView.setText(videoDetails.getTitle());
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getCount() {
        return this.videoDetailsArrayList.size();
    }
}
