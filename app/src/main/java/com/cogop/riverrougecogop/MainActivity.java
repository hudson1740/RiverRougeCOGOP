package com.cogop.riverrougecogop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// This Project was built by the developers of Brett Tech Networking for the Church of God of Prophecy, River Rouge, MI //

public class MainActivity extends AppCompatActivity {
    // Identifires ------------------------------------------------//
    DrawerLayout drawerLayout;
    WebView web;

    ListView listView;
    ArrayList<VideoDetails> videoDetailsArrayList;
    MyCustomAdapter myCustomAdapter;
    String url = "http://www.google.com";

    @Override
    protected void onStart() {
        super.onStart();

        if (checkInstallation(MainActivity.this, "us.zoom.videomeetings")) {
            // zoom installed
            TextView myTextView = findViewById(R.id.textView44);
            myTextView.setText("Zoom Installed: YES");
            myTextView.setTextColor(Color.parseColor("#32CD32"));

        } else {
            // zoom not installed
            TextView myTextView = findViewById(R.id.textView44);
            myTextView.setText("Zoom Installed: NO");
            myTextView.setTextColor(Color.parseColor("#ff0000"));


        }
    }

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
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.nav_activity_main);
        listView = (ListView) findViewById(R.id.listview1);
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
    DrawerLayout mMenu;
    public void menu(View view){
        try {
            mMenu = findViewById(R.id.drawer_layout);
            mMenu.open();
        }
        catch(Exception e){
        }
    }

    public void profile(View view) {
        Toast.makeText(MainActivity.this, "This option is under development, please look for upcoming updates", Toast.LENGTH_LONG).show();
    }
}