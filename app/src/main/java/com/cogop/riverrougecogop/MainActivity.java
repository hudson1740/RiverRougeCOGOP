package com.cogop.riverrougecogop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.cogop.riverrougecogop.adapter.MyCustomAdapter;
import com.cogop.riverrougecogop.model.VideoDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    // Identifires ------------------------------------------------//
    DrawerLayout drawerLayout;
    WebView web;

    ListView listView;
    ArrayList<VideoDetails> videoDetailsArrayList;
    MyCustomAdapter myCustomAdapter;
    String url = "https://www.google.com";

    private TextView textViewVerse;
    private CountDownTimer verseTimer;
    private long remainingTime;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "onStart() called");
        if (checkInstallation(MainActivity.this, "us.zoom.videomeetings")) {
            // zoom installed
            TextView myTextView = findViewById(R.id.textView44);
            myTextView.setText(R.string.zoom_installed_yes);
            myTextView.setTextColor(Color.parseColor("#32CD32"));
        } else {
            // zoom not installed
            TextView myTextView = findViewById(R.id.textView44);
            myTextView.setText(R.string.zoom_installed_no);
            myTextView.setTextColor(Color.parseColor("#F3FB03"));
        }
    }

  /*  @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Check the new orientation
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.fragment_dashboard_landscape);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.fragment_dashboard);
        }
    }*/

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

    // OnCreate --------------------------------------------------//
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LightTheme);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
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
                startVerseTimer(remainingTime);
            }
        }
    }
    //navigation menu items make sure it is MenuItem item note View view !!!!!
    public void giveButton(MenuItem item) {
        // Your action when the "Give (offering, donations etc.)" item is clicked
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cash.app/$RiverRougeCOGOP"));
        startActivity(browserIntent);
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
        startActivityForResult(announcements, 0);
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
        Intent notesintent = new Intent(view.getContext(), NotesMainActivity.class);
        startActivityForResult(notesintent, 0);
    }

    public void settings(View view) {
        Intent settingsintent = new Intent(view.getContext(), SettingsActivity.class);
        startActivityForResult(settingsintent, 0);
    }

    public void help(View view) {
    }

    public void cashapp(View view){
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
        // Replace with your logic to fetch and display a random Bible verse
        String[] verses = {
                "Daily Scripture  \n" + "For God so loved the world, that he gave his only Son, that whoever believes in him should not perish but have eternal life. - John 3:16",
                "Daily Scripture \n" + "Trust in the LORD with all your heart, and do not lean on your own understanding. - Proverbs 3:5",
                "Daily Scripture \n" + "The LORD is my shepherd; I shall not want. - Psalm 23:1",
                // Add more Bible verses as needed
        };

        Random random = new Random();
        int index = random.nextInt(verses.length);
        String randomVerse = verses[index];

        textViewVerse.setText(randomVerse);
    }

    private void startVerseTimer(long duration) {
        verseTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                // Timer finished, display a new random verse
                displayRandomVerse();
                // Restart the timer
                startVerseTimer(12 * 60 * 60 * 1000); // 12 hours in milliseconds
            }
        }.start();

        // Initial display of a random verse
        displayRandomVerse();
    }
}