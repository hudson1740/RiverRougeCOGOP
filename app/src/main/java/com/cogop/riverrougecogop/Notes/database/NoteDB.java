package com.cogop.riverrougecogop.Notes.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.cogop.riverrougecogop.Notes.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDB extends RoomDatabase {

    private static NoteDB instance;
    public abstract NoteDao noteDao();

    public static synchronized NoteDB getInstance(Context context){
     if(instance == null){
         instance = Room.databaseBuilder(context.getApplicationContext(),
                         NoteDB.class, "note_database")
                 .fallbackToDestructiveMigration()
                 .addCallback(roomCallback)
                 .build();
     }
     return instance;
    }

    // below line is to create a callback for our room database.
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDbAsyncTask(NoteDB instance) {
            NoteDao dao = instance.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

    @Override
    public void clearAllTables() {

    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(@NonNull DatabaseConfiguration databaseConfiguration) {
        return null;
    }
}
