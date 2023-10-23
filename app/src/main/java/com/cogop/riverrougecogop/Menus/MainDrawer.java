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
import com.cogop.riverrougecogop.MainActivity;
import com.cogop.riverrougecogop.R;
import com.google.android.material.navigation.NavigationView;

public class MainDrawer extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_main);

        // Find the NavigationView by its ID
        NavigationView navigationView = findViewById(R.id.app_version);

        // Set a navigation item selected listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.app_version) {
                    // Open the AdminMenu activity when the "App Version" menu item is clicked
                    Intent adminmenuIntent = new Intent(MainDrawer.this, AdminMenu.class);
                    startActivity(adminmenuIntent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
    public void adminmenu(MenuItem item) {
        Intent adminmenuIntent = new Intent(this, AdminMenu.class);
        startActivity(adminmenuIntent);
        finish();
    }
}
