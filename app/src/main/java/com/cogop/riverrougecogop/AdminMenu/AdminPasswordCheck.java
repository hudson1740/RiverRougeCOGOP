package com.cogop.riverrougecogop.AdminMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.cogop.riverrougecogop.R;

public class AdminPasswordCheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_admin_menu);
    }

    public void submitPIN(View view) {
        // Get the entered PIN from the EditText
        EditText pinEditText = findViewById(R.id.pinEditText);
        String enteredPIN = pinEditText.getText().toString();

        // Define the correct PIN (e.g., "1234")
        String correctPIN = "37320";

        // Check if the entered PIN is correct
        if (enteredPIN.equals(correctPIN)) {
            // If correct, open the AdminMenu activity
            Intent adminmenuIntent = new Intent(this, AdminMenu.class);
            startActivity(adminmenuIntent);
            finish();
        } else {
            // If incorrect, you can show an error message or clear the EditText
            pinEditText.setError("Incorrect PIN! This tool is for debugging purposes, If your require access please contact Brett.");
            pinEditText.setText("");
        }
    }
}