package com.example.savenotes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NotesRepository {
    private NotesDAO notesDAO;
    private LiveData<List<Notes>> mAllNotes;

    NotesRepository(Application application){
        NotesRoomDatabase db = NotesRoomDatabase.getDatabase(application);
        notesDAO = db.notesDAO();
        mAllNotes = notesDAO.getAllNotes();
    }

    public LiveData<List<Notes>> getAllNotes() {
        return mAllNotes;
    }

    public void insert(Notes notes){
        new MyAsyncTask(notesDAO,"insert").execute(notes);
    }

    public void delete(Notes notes){
        new MyAsyncTask(notesDAO,"delete").execute(notes);
    }

    public void update(Notes notes) {
        new MyAsyncTask(notesDAO,"update").execute(notes);
    }

    public static class MyAsyncTask extends AsyncTask<Notes,Void,Void> {
        private NotesDAO asyncDAO;
        private String myTask;

        MyAsyncTask(NotesDAO dao,String task){
            asyncDAO = dao;
            myTask = task;
        }

        @Override
        protected Void doInBackground(Notes... notes) {
            switch (myTask){
                case "insert":
                    asyncDAO.insert(notes[0]);
                    break;
                case "delete":
                    asyncDAO.delete(notes[0]);
                    break;
                case "update":
                    asyncDAO.update(notes[0]);
            }
            return null;
        }
    }
}
