package com.cogop.riverrougecogop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cogop.riverrougecogop.Notes.Edit;
import com.cogop.riverrougecogop.Notes.Note;
import com.cogop.riverrougecogop.Notes.NoteDatabase;
import com.cogop.riverrougecogop.Notes.NotesMainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Details extends AppCompatActivity {
    TextView mDetails;
    NoteDatabase db;
    Note note;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDetails = findViewById(R.id.detailsOfNote);

        Intent i = getIntent();
       Long id = i.getLongExtra("ID",0);

        db = new NoteDatabase(this);
       // note = db.getNote(id);
      //  getSupportActionBar().setTitle(note.getTitle());
      //  mDetails.setText(note.getContent());

        // FLOATING BUTTON //
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  db.deleteNote(note.getID());
                Toast.makeText(getApplicationContext(),"Note Deleted",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), NotesMainActivity.class));
                goToMain();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.editNote){
            Intent i = new Intent(this, Edit.class);
           // i.putExtra("ID",note.getID());
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void goToMain() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}
