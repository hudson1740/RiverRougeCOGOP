package com.cogop.riverrougecogop;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Meet extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meet_leaders);

        ScrollView scrollView = findViewById(R.id.scrollView);
        Button leonardButton = findViewById(R.id.leonardButton);
        Button desereneButton = findViewById(R.id.desereneButton);

        leonardButton.setOnClickListener(v -> {
            scrollView.smoothScrollTo(0, findViewById(R.id.leonardCard).getTop());
        });

        desereneButton.setOnClickListener(v -> {
            scrollView.smoothScrollTo(0, findViewById(R.id.desereneCard).getTop());
        });
    }
}