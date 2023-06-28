package com.cogop.riverrougecogop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Home extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_dashboard);
    }
    public void landscape(View view){
        Intent landscape = new Intent(view.getContext(), landscape.class);
        startActivityForResult(landscape, 0);
    }
}

