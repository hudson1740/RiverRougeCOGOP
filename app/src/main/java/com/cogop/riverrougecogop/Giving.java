package com.cogop.riverrougecogop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.cogop.riverrougecogop.ui.dashboard.DashboardFragment;

public class Giving extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giving);
    }
    public void backButtonGiving(View view) {
        Intent givingintent  = new Intent(view.getContext(), MainActivity.class);
        startActivity(givingintent);
    }

}
