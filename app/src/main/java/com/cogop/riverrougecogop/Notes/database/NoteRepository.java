package com.cogop.riverrougecogop.Notes.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.cogop.riverrougecogop.Notes.Note;

import java.util.List;

public class NoteRepository {

    private NoteDao notedao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDB database = NoteDB.getInstance(application);
        notedao = database.noteDao();
        allNotes = notedao.getAllNotes();
    }
    // creating a method to insert the data to our database.
    public void createNote(Note model) {
        new CreateNoteAsyncTask(notedao).execute(model);
    }

    // creating a method to update data in database.
    public void update(Note model) {
        new UpdateNoteAsyncTask(notedao).execute(model);
    }

    // creating a method to delete the data in our database.
    public void delete(Note model) {
        new DeleteNoteAsyncTask(notedao).execute(model);
    }

    // below method is to read all the courses.
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }


    private static class CreateNoteAsyncTask  extends AsyncTask<Note, Void, Void> {
        private NoteDao dao;

        private CreateNoteAsyncTask(NoteDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Note... model) {
            // below line is use to insert our modal in dao.
            dao.createNote(model[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void>{
        private NoteDao dao;

        private UpdateNoteAsyncTask(NoteDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            dao.updateNote(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDao noteDao;
        public DeleteNoteAsyncTask(NoteDao notedao) {
            this.noteDao = notedao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.deleteNote(notes[0]);
            return null;
        }
    }
}
