package com.cogop.riverrougecogop.Notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cogop.riverrougecogop.MainActivity;
import com.cogop.riverrougecogop.Notes.database.NoteRepository;
import com.cogop.riverrougecogop.R;
import com.cogop.riverrougecogop.adapter.MyCustomAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotesMainActivity extends AppCompatActivity implements NotesAdapter.OnDeleteClickListener {
    Toolbar toolbar;
    RecyclerView recyclerView;
    NotesAdapter adapter;
    List<Note> allNotes = new ArrayList<>();
    TextView noNoteFound;
    SearchView searchView_home;
    MyCustomAdapter notesListAdapter;



    private NoteRepository noteRepository;
    private NotesAdapter.OnDeleteClickListener onDeleteClickListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onDeleteClickListener = this;
        setContentView(R.layout.notes_main_activity);
        toolbar = findViewById(R.id.toolbar);
        noNoteFound = findViewById(R.id.no_note_found);
        searchView_home = findViewById(R.id.searchView_home);


//my code
        Toolbar actionBar = findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        actionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onBackPressed(); } });
//end my code

       // setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.listOfNotes);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        noteRepository = new NoteRepository(getApplication());
        noteRepository.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                allNotes = notes;
                adapter = new NotesAdapter(getApplicationContext(),allNotes,onDeleteClickListener);
                recyclerView.setAdapter(adapter);
                if (allNotes.size() == 0){
                    noNoteFound.setVisibility(View.VISIBLE);
                } else {
                    noNoteFound.setVisibility(View.GONE);
                }
            }
        });
    }
    //note search filter code
    private void filter(String newText) {
        List<Note> filteredList = new ArrayList<>();
        for (Note singleNote : allNotes) {
            if (singleNote.getNoteTitle().toLowerCase().contains(newText.toLowerCase()) || singleNote.getNoteDescription().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(singleNote);
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void addNoteButton(View view) {
        Intent intent = new Intent(this, AddNote.class);
        startActivity(intent);
    }
    @Override
    public void onItemDelete(Note note) {
         showDeleteDialog(note);
    }

    @Override
    public void onItemClick(Note note) {
        Bundle bundle = new Bundle();
        bundle.putInt("ID", note.getId());
        bundle.putString("title",note.getNoteTitle());
        bundle.putString("description",note.getNoteDescription());

        Intent i = new Intent(NotesMainActivity.this, AddNote.class);
        i.putExtras(bundle);
        startActivity(i);
    }

    private void showDeleteDialog(Note note){
        AlertDialog.Builder builder = new AlertDialog.Builder(NotesMainActivity.this);
        builder.setMessage("Do you want to Delete this Note?");
        builder.setTitle("Delete Note!");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            Toast.makeText(this,"Note deleted",Toast.LENGTH_SHORT).show();
            noteRepository.delete(note);
            dialog.dismiss();
        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
