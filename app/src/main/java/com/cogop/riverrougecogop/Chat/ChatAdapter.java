package com.cogop.riverrougecogop.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.cogop.riverrougecogop.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChatMessage> chatMessages;
    private Context context;


    public ChatAdapter(Context context, List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        this.context = context;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);
        holder.messageTextView.setText(message.getMessageText());

        // Always display the username
        holder.userNameTextView.setText(message.getUsername());
        holder.userNameTextView.setVisibility(View.VISIBLE);
    }



    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        TextView messageTextView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
        }
    }
}
