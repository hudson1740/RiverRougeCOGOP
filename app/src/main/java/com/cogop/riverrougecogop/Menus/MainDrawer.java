package com.cogop.riverrougecogop.Menus;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cogop.riverrougecogop.AdminMenu.AdminMenu;
import com.cogop.riverrougecogop.AdminMenu.AdminPasswordCheck;
import com.cogop.riverrougecogop.MainActivity;
import com.cogop.riverrougecogop.R;
import com.google.android.material.navigation.NavigationView;


public class MainDrawer extends AppCompatActivity {
    private int clickCount = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }
    public void passwordcheck(MenuItem item) {
        // Open the PIN entry screen
        Intent pinEntryIntent = new Intent(this, AdminPasswordCheck.class);
        startActivity(pinEntryIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_main);
    }

}
