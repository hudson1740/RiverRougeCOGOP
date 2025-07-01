package com.cogop.riverrougecogop;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cogop.riverrougecogop.AdminMenu.AdminPasswordCheck;
import com.cogop.riverrougecogop.Announcements.Announcements;

import static android.content.ContentValues.TAG;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.cogop.riverrougecogop.Bible.BibleVersesProvider;
import com.cogop.riverrougecogop.Chat.ChatActivity;
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
    private CountDownTimer verseTimer;
    private long remainingTime;

    public static boolean checkInstallation(Context context, String packageName) {
        // on below line creating a variable for package manager.
        PackageManager pm = context.getPackageManager();
        try {
            // on below line getting package info
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            // on below line returning true if package is installed.
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // returning false if package is not installed on device.
            return false;
        }
    }
    public void passwordcheck(MenuItem item) {
        // Open the PIN entry screen
        Intent pinEntryIntent = new Intent(this, AdminPasswordCheck.class);
        startActivity(pinEntryIntent);
    }
    // OnCreate --------------------------------------------------//
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LightTheme);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        FirebaseApp.initializeApp(this);
        bottomBanner = findViewById(R.id.bottomBanner);

        // ---------- Asks user permission for notification permissions for push notification use -------- //
        int permissionState = ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS);
        // If the permission is not granted, request it.
        if (permissionState == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        //firebase messaging // Get the Firebase Messaging token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get the token
                    String token = task.getResult();
                    Log.d(TAG, "FCM Registration Token: " + token);
                });
        setContentView(R.layout.nav_activity_main);
        textViewVerse = findViewById(R.id.textViewVerse);
        listView = findViewById(R.id.listview1);
        videoDetailsArrayList = new ArrayList<>();
        myCustomAdapter = new MyCustomAdapter(MainActivity.this, videoDetailsArrayList);
        displayVideos();

        //Lower Navigation bar containing Home,Dashboard, & Notifications ---------------------------//
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //Tells the app too start on Dashboard instead of "Home"
        navView.getMenu().getItem(1).setChecked(true);
        navView.setSelectedItemId(R.id.navigation_dashboard);

        // Check if a saved timestamp exists
        long savedTimestamp = getSharedPreferences("MainActivity", Context.MODE_PRIVATE).getLong("timestamp", 0);
        if (savedTimestamp == 0) {
            // No saved timestamp, display a random verse and start the timer
            displayRandomVerse();
            startVerseTimer(12 * 60 * 60 * 1000); // 12 hours in milliseconds
        } else {
            // Calculate the remaining time from the saved timestamp
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - savedTimestamp;
            if (elapsedTime >= 12 * 60 * 60 * 1000) {
                // More than 12 hours have passed, display a random verse and start the timer
                displayRandomVerse();
                startVerseTimer(12 * 60 * 60 * 1000); // 12 hours in milliseconds
            } else {
                // Less than 12 hours have passed, resume the timer with the remaining time
                long remainingTime = 12 * 60 * 60 * 1000 - elapsedTime;
                startVerseTimer((int) remainingTime);
            }
        }
    }

    //navigation menu items make sure it is MenuItem item note View view !!!!!
    public void giveButton(MenuItem item) {
        // Your action when the "Give (offering, donations etc.)" item is clicked
        Intent giveintent  = new Intent(this, Giving.class);
        startActivity(giveintent);
    }
    public void notesbtn(MenuItem item) {
        Intent giveintent  = new Intent(this, NotesMainActivity.class);
        startActivity(giveintent);
    }
    public void announcebtn(MenuItem item) {
        Intent announcementsIntent = new Intent(this, Announcements.class);
        startActivity(announcementsIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save the current timestamp
        long currentTimestamp = System.currentTimeMillis();
        getSharedPreferences("MainActivity", Context.MODE_PRIVATE).edit().putLong("timestamp", currentTimestamp).apply();
    }
    public void meetleaders(MenuItem item) {
        //Meet Our Leaders Button
        Intent meetIntent = new Intent(this, Meet.class);
        startActivity(meetIntent);
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

    // Dashboard Button Actions ----------------------------------------------------------------------//
    public void join1(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bit.ly/RRCOGOP"));
        startActivity(browserIntent);
    }
    public void chatButton(MenuItem item) {
        Intent chatButton  = new Intent(this, NameSelectionActivity.class);
        startActivity(chatButton);
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
        //intents
        Intent i;
        PackageManager manager = getPackageManager();

        //find and open you version bible app or redirect to google play to install app
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

    /*public void landscape(View view) {
        Intent landscape = new Intent(view.getContext(), landscape.class);
        startActivity(landscape);
    }*/

    public void home(View view) {
        Intent home = new Intent(view.getContext(), Home.class);
        startActivity(home);
    }

    public void notesbtn(View view) {
       // Toast.makeText(MainActivity.this, "This option is under development, please look for upcoming updates", Toast.LENGTH_LONG).show();
        Intent notesintent = new Intent(MainActivity.this, NotesMainActivity.class);
        startActivity(notesintent);
    }
    public void settings(View view) {
        Intent settingsintent = new Intent(this, Settings.class);
        startActivity(settingsintent);
    }

    public void giving(View view) {
        Intent givingintent  = new Intent(view.getContext(), Giving.class);
        startActivity(givingintent);

        // Toast.makeText(this, "This option is under development, please look for upcoming updates", Toast.LENGTH_LONG).show();
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

    private void displayRandomVerse() {
        // Fetch a random Bible verse from the BibleVersesProvider
        String randomVerse = BibleVersesProvider.getRandomVerse();

        // Display the verse in the textViewVerse
        TextView textViewVerse = findViewById(R.id.textViewVerse);
        textViewVerse.setText(randomVerse);
    }

    private Handler handler = new Handler();
    private Runnable verseUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            // Display a new random verse
            displayRandomVerse();
            // Schedule the next update after 2 minutes
            handler.postDelayed(this, 2 * 60 * 1000); // 2 minutes in milliseconds
        }
    };


    private final long VERSE_UPDATE_INTERVAL = 2 * 60 * 1000; // 2 minutes in milliseconds

    private void startVerseTimer(int i) {
        verseTimer = new CountDownTimer(VERSE_UPDATE_INTERVAL, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                // Timer finished, display a new random verse and restart the timer
                displayRandomVerse();
                startVerseTimer(2 * 60 * 1000);
            }
        }.start();

        // Initial display of a random verse
        displayRandomVerse();
    }


    @Override
    public void onBackPressed() {
        // Get the NavController for the navigation host fragment
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Check if there is a previous destination in the back stack
        if (navController.getPreviousBackStackEntry() != null) {
            // If there is a previous destination, navigate back to it
            navController.popBackStack();
        } else {
            // If there is no previous destination, set the flag to indicate back press for exit
            backPressedToExit = true;
            super.onBackPressed();
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        if (navController.getPreviousBackStackEntry() != null) {
            navController.popBackStack(); }
            else{
            finish();
        }
    }
}