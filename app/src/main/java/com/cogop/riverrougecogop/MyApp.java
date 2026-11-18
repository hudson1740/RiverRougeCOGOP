package com.cogop.riverrougecogop;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Firebase is initialized automatically by FirebaseInitProvider via google-services.json.
        // Do not call FirebaseApp.initializeApp() here — it crashes with
        // "FirebaseApp name [DEFAULT] already exists!".

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
