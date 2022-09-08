package com.example.photonotes.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PhotoDAO {

    @Insert
    suspend fun insert(notes: PhotoNotes)

    @Update
    suspend fun update(notes: PhotoNotes)

    @Delete
    suspend fun delete(notes: PhotoNotes)

    @Query("SELECT * FROM photo_notes_database WHERE photo_title LIKE :search")
    fun searchTitle(search: String): LiveData<List<PhotoNotes>>

    @Query("SELECT * FROM photo_notes_database")
    fun getAll(): LiveData<List<PhotoNotes>>

    @Query("SELECT * from photo_notes_database WHERE photoID = :key")
    suspend fun get(key: Long): PhotoNotes
}