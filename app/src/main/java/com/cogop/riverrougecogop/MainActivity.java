package com.cogop.riverrougecogop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cogop.riverrougecogop.AdminMenu.AdminPasswordCheck;
import com.cogop.riverrougecogop.Announcements.Announcements;
import com.cogop.riverrougecogop.Bible.BibleVersesProvider;
import com.cogop.riverrougecogop.Chat.NameSelectionActivity;
import com.cogop.riverrougecogop.Notes.NotesMainActivity;
import com.cogop.riverrougecogop.Settings.Settings;
import com.cogop.riverrougecogop.adapter.MyCustomAdapter;
import com.cogop.riverrougecogop.model.VideoDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean backPressedToExit = false;

    // Identifiers ------------------------------------------------//
    DrawerLayout drawerLayout;
    WebView web;
    ListView listView;
    ArrayList<VideoDetails> videoDetailsArrayList;
    MyCustomAdapter myCustomAdapter;
    String url = "https://www.google.com";
    private TextView textView44;
    ImageView backButton;
    private View bottomBanner;
    private TextView textViewVerse;
    private View notificationBubble;
    private CountDownTimer verseTimer;
    private long remainingTime;
    private long verseUpdateInterval;

    public static boolean checkInstallation(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void passwordcheck(MenuItem item) {
        Intent pinEntryIntent = new Intent(this, AdminPasswordCheck.class);
        startActivity(pinEntryIntent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LightTheme);
        FirebaseApp.initializeApp(this);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.nav_activity_main);
        bottomBanner = findViewById(R.id.bottomBanner);

        // Initialize notification bubble
        notificationBubble = findViewById(R.id.notification_bubble);

        // Load settings
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        verseUpdateInterval = prefs.getLong("verse_update_interval", 24 * 60 * 60 * 1000); // Default 24 hours
        boolean notificationsEnabled = prefs.getBoolean("notifications_enabled", true);
        notificationBubble.setVisibility(notificationsEnabled ? View.VISIBLE : View.GONE);

        // Handle intent from Settings
        Intent intent = getIntent();
        if (intent.hasExtra("verse_update_interval")) {
            verseUpdateInterval = intent.getLongExtra("verse_update_interval", verseUpdateInterval);
            notificationsEnabled = intent.getBooleanExtra("notifications_enabled", notificationsEnabled);
            prefs.edit()
                    .putLong("verse_update_interval", verseUpdateInterval)
                    .putBoolean("notifications_enabled", notificationsEnabled)
                    .apply();
            notificationBubble.setVisibility(notificationsEnabled ? View.VISIBLE : View.GONE);
        }

        // Notification permissions
        int permissionState = ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS);
        if (permissionState == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        // Firebase messaging
        if (notificationsEnabled) {
            FirebaseMessaging.getInstance().subscribeToTopic("announcements");
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("announcements");
        }
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String token = task.getResult();
                    Log.d(TAG, "FCM Registration Token: " + token);
                });

        textViewVerse = findViewById(R.id.textViewVerse);
        listView = findViewById(R.id.listview1);
        videoDetailsArrayList = new ArrayList<>();
        myCustomAdapter = new MyCustomAdapter(MainActivity.this, videoDetailsArrayList);
        displayVideos();

        // Lower Navigation bar containing Home, Dashboard, & Notifications
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        // Start on Dashboard
        navView.getMenu().getItem(1).setChecked(true);
        navView.setSelectedItemId(R.id.navigation_dashboard);

        // Verse update logic
        long savedTimestamp = getSharedPreferences("MainActivity", Context.MODE_PRIVATE).getLong("timestamp", 0);
        if (savedTimestamp == 0) {
            displayRandomVerse();
            startVerseTimer(verseUpdateInterval);
        } else {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - savedTimestamp;
            if (elapsedTime >= verseUpdateInterval) {
                displayRandomVerse();
                startVerseTimer(verseUpdateInterval);
            } else {
                long remaining = verseUpdateInterval - elapsedTime;
                startVerseTimer(remaining);
            }
        }
    }

    private void displayRandomVerse() {
        String randomVerse = BibleVersesProvider.getRandomVerse();
        textViewVerse.setText(randomVerse);
    }

    private Handler handler = new Handler();
    private Runnable verseUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            displayRandomVerse();
            handler.postDelayed(this, 2 * 60 * 1000); // 2 minutes
        }
    };

    private final long VERSE_UPDATE_INTERVAL = 2 * 60 * 1000; // 2 minutes

    private void startVerseTimer(long interval) {
        if (verseTimer != null) {
            verseTimer.cancel();
        }
        verseTimer = new CountDownTimer(interval, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                displayRandomVerse();
                startVerseTimer(verseUpdateInterval);
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        long currentTimestamp = System.currentTimeMillis();
        getSharedPreferences("MainActivity", Context.MODE_PRIVATE).edit().putLong("timestamp", currentTimestamp).apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
            verseUpdateInterval = data.getLongExtra("verse_update_interval", verseUpdateInterval);
            boolean notificationsEnabled = data.getBooleanExtra("notifications_enabled", true);
            prefs.edit()
                    .putLong("verse_update_interval", verseUpdateInterval)
                    .putBoolean("notifications_enabled", notificationsEnabled)
                    .apply();
            notificationBubble.setVisibility(notificationsEnabled ? View.VISIBLE : View.GONE);
            if (verseTimer != null) {
                verseTimer.cancel();
            }
            displayRandomVerse();
            startVerseTimer(verseUpdateInterval);
            if (notificationsEnabled) {
                FirebaseMessaging.getInstance().subscribeToTopic("announcements");
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("announcements");
            }
        }
    }

    public void giveButton(MenuItem item) {
        Intent giveintent = new Intent(this, Giving.class);
        startActivity(giveintent);
    }

    public void notesbtn(MenuItem item) {
        Intent giveintent = new Intent(this, NotesMainActivity.class);
        startActivity(giveintent);
    }

    public void announcebtn(MenuItem item) {
        Intent announcementsIntent = new Intent(this, Announcements.class);
        startActivity(announcementsIntent);
    }

    public void meetleaders(MenuItem item) {
        Intent meetIntent = new Intent(this, Meet.class);
        startActivity(meetIntent);
    }

    public void chatButton(MenuItem item) {
        Intent chatButton = new Intent(this, NameSelectionActivity.class);
        startActivity(chatButton);
    }

    private void displayVideos() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonVideoID = jsonObject1.getJSONObject("id");
                        JSONObject jsonObjectSnippet = jsonObject1.getJSONObject("snippet");
                        JSONObject jsonObjectDefault = jsonObjectSnippet.getJSONObject("thumbnails").getJSONObject("medium");

                        String video_id = jsonVideoID.getString("videoId");
                        VideoDetails vd = new VideoDetails();
                        vd.setVideoId(video_id);
                        vd.setTitle(jsonObjectSnippet.getString("title"));
                        vd.setDescription(jsonObjectSnippet.getString("description"));
                        vd.setUrl(jsonObjectDefault.getString("url"));
                        videoDetailsArrayList.add(vd);
                    }
                    listView.setAdapter(myCustomAdapter);
                    myCustomAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void join1(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bit.ly/RRCOGOP"));
        startActivity(browserIntent);
    }

    public void youtubeclick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/@RiverRougeCOGOP/streams"));
        startActivity(browserIntent);
    }

    public void fbbtn(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/RiverRougeCOGOP"));
        startActivity(browserIntent);
    }

    public void twitterbtn(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com/RiverRougeCOGOP"));
        startActivity(browserIntent);
    }

    public void instabtn(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/RiverRougeCOGOP"));
        startActivity(browserIntent);
    }

    public void locationbtn(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/4vFb2BirR8drRxn3A"));
        startActivity(browserIntent);
    }

    public void biblebtn(View view) {
        Intent i;
        PackageManager manager = getPackageManager();
        try {
            i = manager.getLaunchIntentForPackage("com.sirma.mobile.bible.android");
            if (i == null)
                throw new PackageManager.NameNotFoundException();
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.sirma.mobile.bible.android"));
            startActivity(browserIntent);
        }
    }

    public void bible1(View view) {
        Intent bibleintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
        startActivity(bibleintent);
    }

    public void announcementsbtn(View view) {
        Intent announcements = new Intent(view.getContext(), Announcements.class);
        startActivity(announcements);
    }

    public void home(View view) {
        Intent home = new Intent(view.getContext(), Home.class);
        startActivity(home);
    }

    public void notesbtn(View view) {
        Intent notesintent = new Intent(MainActivity.this, NotesMainActivity.class);
        startActivity(notesintent);
    }

    public void settings(View view) {
        Intent settingsintent = new Intent(this, Settings.class);
        startActivityForResult(settingsintent, 1);
    }

    public void giving(View view) {
        Intent givingintent = new Intent(view.getContext(), Giving.class);
        startActivity(givingintent);
    }

    public void cashapp(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cash.app/$RiverRougeCOGOP"));
        startActivity(browserIntent);
    }

    DrawerLayout mMenu;

    public void menu(View view) {
        try {
            mMenu = findViewById(R.id.drawer_layout);
            mMenu.open();
        } catch (Exception e) {
        }
    }

    public void profile(View view) {
        Toast.makeText(MainActivity.this, "This option is under development, please look for upcoming updates", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if (navController.getPreviousBackStackEntry() != null) {
            navController.popBackStack();
        } else {
            backPressedToExit = true;
            super.onBackPressed();
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if (navController.getPreviousBackStackEntry() != null) {
            navController.popBackStack();
        } else {
            finish();
        }
    }
}