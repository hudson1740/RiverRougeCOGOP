package com.cogop.riverrougecogop.Notes.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cogop.riverrougecogop.Notes.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void createNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM note ORDER BY last_modified ASC")
    LiveData<List<Note>> getAllNotes();
}
