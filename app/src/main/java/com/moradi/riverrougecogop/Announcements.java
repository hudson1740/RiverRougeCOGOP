package com.moradi.riverrougecogop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Announcements extends AppCompatActivity {

    ListView listView3;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcements_activity);

        //Announcements list in Fragment_notifications
        listView3=(ListView)findViewById(R.id.listview3);
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("Sunday School 11am-12pm");
        arrayList.add("Sunday Service 12pm-1pm");
        arrayList.add("Bible Study Wed. 6pm-8pm");
        arrayList.add("Regional Convention Sep. 10th-11th, Harvest Worship Center ");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        listView3.setAdapter(arrayAdapter);
    }
}