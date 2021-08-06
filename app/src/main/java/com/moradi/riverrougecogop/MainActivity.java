package com.moradi.riverrougecogop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moradi.riverrougecogop.adapter.MyCustomAdapter;
import com.moradi.riverrougecogop.model.VideoDetails;
import com.moradi.riverrougecogop.ui.home.HomeFragment;
import com.moradi.riverrougecogop.ui.home.HomeViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// This Project was built by the developers of Brett Tech Networking for the Church of God of Prophecy, River Rouge, MI //

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    WebView web;
    ListView listView;
    ArrayList<VideoDetails> videoDetailsArrayList;
    MyCustomAdapter myCustomAdapter;
    String url ="http://www.google.com";
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LightTheme);
        setContentView(R.layout.nav_activity_main);
        listView = (ListView) findViewById(R.id.listview1);
        videoDetailsArrayList = new ArrayList<>();
        myCustomAdapter = new MyCustomAdapter(MainActivity.this,videoDetailsArrayList);
        displayVideos();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
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
        }
        );
        requestQueue.add(stringRequest);
    }

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
        Toast.makeText(MainActivity.this, "This option is under development, please look for upcoming updates", Toast.LENGTH_LONG).show();
    }
    public void profile(View view){
        Toast.makeText(MainActivity.this, "This option is under development, please look for upcoming updates", Toast.LENGTH_LONG).show();
    }
    public void biblebtn(View view) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.chrome");
        if (launchIntent != null) {
            startActivity(launchIntent);
        } else if (launchIntent == null)
            {
            Toast.makeText(MainActivity.this, "There is no package available in android", Toast.LENGTH_LONG).show();
        }
    }
}
