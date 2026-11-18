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
import com.google.firebase.messaging.FirebaseMessaging;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private boolean backPressedToExit = false;

    // Identifiers ------------------------------------------------//
    DrawerLayout drawerLayout;
    WebView web;
    ListView listView;
    ArrayList<VideoDetails> videoDetailsArrayList;
    MyCustomAdapter myCustomAdapter;
    // Public channel Atom feed — no API key / quota / 403 from YouTube Data API.
    String url = "https://www.youtube.com/feeds/videos.xml?channel_id=UCUhcJn2pYIyPa7Yp1AzKOkA";
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
        setTheme(R.style.LightTheme);
        super.onCreate(savedInstanceState);
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
        bottomBanner = findViewById(R.id.bottomBanner);
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
                // Less than 12 hours has passed, resume the timer with the remaining time
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
        // Meet Our Leaders Button: Show as a BottomSheetDialogFragment
        MeetOurLeadersFragment bottomSheetFragment = new MeetOurLeadersFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    private void displayVideos() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    parseYoutubeFeed(response);
                    listView.setAdapter(myCustomAdapter);
                    myCustomAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(TAG, "Failed to parse YouTube feed", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = error.getMessage();
                if (message == null || message.trim().isEmpty()) {
                    message = "Unable to load videos right now";
                }
                Log.e(TAG, "YouTube feed request failed: " + message, error);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void parseYoutubeFeed(String xml) throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xml));

        VideoDetails current = null;
        String text = "";
        int loaded = 0;
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT && loaded < 9) {
            String name = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if ("entry".equals(name)) {
                        current = new VideoDetails();
                    } else if (current != null && "thumbnail".equals(name)) {
                        String thumbUrl = parser.getAttributeValue(null, "url");
                        if (thumbUrl != null) {
                            current.setUrl(thumbUrl);
                        }
                    }
                    break;
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    if (current == null) {
                        break;
                    }
                    if ("videoId".equals(name)) {
                        current.setVideoId(text);
                    } else if ("title".equals(name) && current.getTitle() == null) {
                        current.setTitle(text);
                    } else if ("description".equals(name)) {
                        current.setDescription(text != null ? text : "");
                    } else if ("entry".equals(name)) {
                        if (current.getVideoId() != null && current.getTitle() != null) {
                            if (current.getDescription() == null) {
                                current.setDescription("");
                            }
                            if (current.getUrl() == null || current.getUrl().isEmpty()) {
                                current.setUrl("https://i.ytimg.com/vi/" + current.getVideoId() + "/hqdefault.jpg");
                            }
                            videoDetailsArrayList.add(current);
                            loaded++;
                        }
                        current = null;
                    }
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
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