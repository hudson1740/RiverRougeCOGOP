package com.cogop.riverrougecogop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;

public class landscape extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_dashboard_landscape);
    }


    public void home(View view){
        Intent home = new Intent(view.getContext(), Home.class);
        startActivityForResult(home, 0);
    }
}

