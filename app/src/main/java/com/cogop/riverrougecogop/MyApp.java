package com.cogop.riverrougecogop;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String apiKey = ApiKey.MY_API_KEY;

        // Initialize Firebase with the correct database URL
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("com.cogop.riverrougecogop")
                .setApiKey(apiKey)
                .setDatabaseUrl("https://riverrougecogop-bbe78-default-rtdb.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(this, options);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityPreCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                EdgeToEdgeHelper.enable(activity);
            }

            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                if (EdgeToEdgeHelper.shouldEnableInOnCreate()) {
                    EdgeToEdgeHelper.enable(activity);
                }
                EdgeToEdgeHelper.applyContentInsets(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
            }
        });
    }
}
