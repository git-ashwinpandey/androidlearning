package com.example.savenotes;

import android.content.Context;
import android.content.ContextWrapper;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Notes.class,version = 1)
public abstract class NotesRoomDatabase extends RoomDatabase {
    public abstract NotesDAO notesDAO();
    public static NotesRoomDatabase INSTANCE;
    private static String getPath = "/storage/emulated/0/SaveNotes/notes_table";

    public static synchronized NotesRoomDatabase getDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),NotesRoomDatabase.class,"Save_Notes")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
