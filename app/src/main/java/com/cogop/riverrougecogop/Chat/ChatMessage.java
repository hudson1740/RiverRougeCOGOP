package com.cogop.riverrougecogop.Chat;

public class ChatMessage {
    private String username;
    private String messageText;
    private boolean userMessage;

    public ChatMessage(String username, String messageText, boolean userMessage) {
        this.username = username;
        this.messageText = messageText;
        this.userMessage = userMessage;
    }

    public String getUsername() {
        return username;
    }

    public String getMessageText() {
        return messageText;
    }

    public boolean isUserMessage() {
        return userMessage;
    }
}
