package com.cogop.riverrougecogop;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Firebase with the correct database URL
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("com.cogop.riverrougecogop")
                .setApiKey("AIzaSyDDfdxkhYxSJjyn2P2IQc3d-A8ATrusP4A")
                .setDatabaseUrl("https://riverrougecogop-bbe78-default-rtdb.firebaseio.com") // Replace with your database URL
                .build();

        FirebaseApp.initializeApp(this, options);
    }
}