package com.cogop.riverrougecogop.Settings;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.cogop.riverrougecogop.MainActivity;
import com.cogop.riverrougecogop.R;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }
    public void backButton(View view) {
        Intent givingintent  = new Intent(view.getContext(), MainActivity.class);
        startActivity(givingintent);
    }
}
