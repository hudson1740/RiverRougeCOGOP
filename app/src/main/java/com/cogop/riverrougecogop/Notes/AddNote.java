package com.cogop.riverrougecogop.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cogop.riverrougecogop.MainActivity;
import com.cogop.riverrougecogop.Notes.database.NoteRepository;
import com.cogop.riverrougecogop.R;

import java.util.Calendar;

public class AddNote extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle, noteDetails;
    Calendar c;
    String todaysDate;
    String currentTime;
    View saveNote;

    private NoteRepository noteRepository;

    private int ID;
    private boolean isUpdate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
// TOOLBAR 2 -------------------------------------------------
        // Reference to the toolbar2
        Toolbar toolbar2 = findViewById(R.id.toolbar2);

        // Set toolbar2 as the action bar
        setSupportActionBar(toolbar2);

        // Remove the title from toolbar2
        getSupportActionBar().setTitle(null);  // or use null

        // Inflate the menu
        toolbar2.inflateMenu(R.menu.menu_toolbar2);

        // Handle menu item click events
        toolbar2.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            // Handle item clicks here
            switch (itemId) {
                case R.id.bold:
                    // Handle item 1 click
                    break;
                case R.id.italic:
                    // Handle item 2 click
                    break;
                case R.id.underline:
                    // Handle item 2 click
                    break;
            }
            return true;
        });
// End ToolBar 2 -----------------------------------------------
        isUpdate = false;
        Toolbar actionBar = findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        noteTitle = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails);
        saveNote = findViewById(R.id.floatingActionButton);

        // get current date and time
        c = Calendar.getInstance();
        todaysDate = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        currentTime = pad(c.get(Calendar.HOUR)) + ":" + pad(c.get(Calendar.MINUTE));

        Log.d("calendar", "Date and Time: " + todaysDate + " and " + currentTime);

        noteRepository = new NoteRepository(getApplication());
        listeners();
        checkBundle();
    }
    private void checkBundle(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            ID = bundle.getInt("ID",0);
            noteTitle.setText(bundle.getString("title"));
            noteDetails.setText(bundle.getString("description"));
            isUpdate = true;
        }
    }

    private String pad(int i) {
        if (i < 10)
            return "0" + i;
        return String.valueOf(i);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }*/

  /*  @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            Toast.makeText(this, "Note Not Saved.", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.save) {
            Note note = new Note(noteTitle.getText().toString(), noteDetails.getText().toString(), todaysDate, currentTime);
            com.cogop.riverrougecogop.Notes.NoteDatabase db = new com.cogop.riverrougecogop.Notes.NoteDatabase(this);
            db.addNote(note);
            Toast.makeText(this, "Save btn", Toast.LENGTH_SHORT).show();
            goToMain();
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void goToMain() {
        Intent i = new Intent(this, com.cogop.riverrougecogop.Notes.NotesMainActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean isNoteDataEmpty(){
        if(noteTitle.getText().toString().isEmpty() && noteDetails.getText().toString().isEmpty()){
            Toast.makeText(this,"Note is empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void listeners(){
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNoteDataEmpty()){
                    Note note = new Note();
                    if(!isUpdate){
                        note.setNoteTitle(noteTitle.getText().toString());
                        note.setNoteDescription(noteDetails.getText().toString());
                        note.setCreatedAt(System.currentTimeMillis());
                        note.setLastModified(System.currentTimeMillis());
                        noteRepository.createNote(note);
                        Toast.makeText(getBaseContext(),"Note saved",Toast.LENGTH_SHORT).show();
                    } else {
                        note.setId(ID);
                        note.setNoteTitle(noteTitle.getText().toString());
                        note.setNoteDescription(noteDetails.getText().toString());
                        note.setCreatedAt(System.currentTimeMillis());
                        note.setLastModified(System.currentTimeMillis());
                        noteRepository.update(note);
                        Toast.makeText(getBaseContext(),"Note updated",Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(AddNote.this, NotesMainActivity.class);
                    intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
                    startActivity(intent);
                }
            }
        });
    }

}

