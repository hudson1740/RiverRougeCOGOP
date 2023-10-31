package com.cogop.riverrougecogop.Chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.cogop.riverrougecogop.Chat.ChatAdapter;
import com.cogop.riverrougecogop.R;

public class NameSelectionActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private Button saveNameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.usernameEditText);
        saveNameButton = findViewById(R.id.saveNameButton);

        // Set a click listener for the "Save" button
        // Set a click listener for the "Save" button
        saveNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                if (!username.isEmpty()) {
                    // Save the username in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.apply();

                    // Start the ChatActivity
                    Intent chatIntent = new Intent(NameSelectionActivity.this, ChatActivity.class);
                    startActivity(chatIntent);
                }
            }
        });

    }
}
