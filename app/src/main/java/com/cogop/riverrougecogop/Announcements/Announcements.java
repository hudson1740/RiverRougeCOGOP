package com.cogop.riverrougecogop.Announcements;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cogop.riverrougecogop.Announcements.AnnouncementsAdapter;
import com.cogop.riverrougecogop.MainActivity;
import com.cogop.riverrougecogop.R;
import com.cogop.riverrougecogop.ui.dashboard.DashboardFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Announcements extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AnnouncementsAdapter announcementsAdapter;
    private List<String> announcementsList;
    ImageButton backButton1;
    public void backButton1(View view) {
        Intent givingintent  = new Intent(view.getContext(), MainActivity.class);
        startActivity(givingintent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcements_activity);

        // Setup RecyclerView and Firebase Database reference
        DatabaseReference announcementsRef = FirebaseDatabase.getInstance().getReference().child("announcements");
        recyclerView = findViewById(R.id.recyclerview_announcements);
        recyclerView.setLayoutManager(new LinearLayoutManager(Announcements.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(Announcements.this, DividerItemDecoration.VERTICAL));

        announcementsList = new ArrayList<>();
        announcementsAdapter = new AnnouncementsAdapter(announcementsList);
        recyclerView.setAdapter(announcementsAdapter);

        // Read data from the "announcements" node
        announcementsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method will be triggered when the data in the database changes
                // Clear the announcementsList to avoid duplicates
                announcementsList.clear();

                // You can parse the data and update your UI here
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get each announcement as a string
                    String announcement = snapshot.getValue(String.class);
                    if (announcement != null) {
                        announcementsList.add(announcement);
                    }
                }

                // Notify the adapter that the data has changed
                announcementsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur while reading data
            }
        });
    }
    // Custom method to navigate back to MainActivity
    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the back stack
        startActivity(intent);
        finish(); // Finish the current activity
    }

    // Override the onBackPressed method to use our custom behavior
    @Override
    public void onBackPressed() {
        navigateToMainActivity();
    }
}
