package com.example.savenotes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotesDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Notes notes);

    @Delete
    void delete(Notes notes);

    @Query("SELECT * from notes_table ORDER BY id ASC")
    LiveData<List<Notes>> getAllNotes();

    @Update
    void update(Notes... notes);
}
