package com.cogop.riverrougecogop.Chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cogop.riverrougecogop.R;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private Button sendButton;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_area);

        // Initialize UI elements
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        // Initialize the chatMessages list
        chatMessages = new ArrayList<>();

        // Initialize the RecyclerView and its adapter
        chatAdapter = new ChatAdapter(this, chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);


        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        // Set the username in the TextView
        TextView userNameTextView = findViewById(R.id.userNameTextView);
        userNameTextView.setText(username);
        userNameTextView.setVisibility(View.VISIBLE);

        Log.d("ChatActivity", "Username: " + username); // Add this line to log the username

        // Now, save the username in SharedPreferences if it's not already saved
        if (username.isEmpty()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.apply();
        }

        // Set a click listener for the "Send" button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    // Create a ChatMessage object with the username
                    ChatMessage message = new ChatMessage(username, messageText, true);
                    chatMessages.add(message);
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
                    messageEditText.setText("");
                }
            }
        });
    }
}
