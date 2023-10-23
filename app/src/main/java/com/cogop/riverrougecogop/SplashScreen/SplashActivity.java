package com.cogop.riverrougecogop.SplashScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cogop.riverrougecogop.MainActivity;
import com.cogop.riverrougecogop.R;
import com.cogop.riverrougecogop.ui.dashboard.DashboardFragment;

public class SplashActivity extends Activity {

    final private Context mContext = SplashActivity.this;
    final private static int SPLASH_TIME_OUT = 3500;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    /* remove call back in on destroy */
    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }

    /* device back button click event */
    @Override
    public void onBackPressed() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        super.onBackPressed();
    }
}