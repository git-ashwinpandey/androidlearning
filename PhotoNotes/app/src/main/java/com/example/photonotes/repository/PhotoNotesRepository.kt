package com.example.photonotes.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.photonotes.database.PhotoDatabase
import com.example.photonotes.database.PhotoNotes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoNotesRepository(private val database: PhotoDatabase) {

    suspend fun insert(notes: PhotoNotes) {
        withContext(Dispatchers.IO) {
            database.photoDatabaseDao.insert(notes)
        }
    }

    suspend fun update(notes: PhotoNotes) {
        withContext(Dispatchers.IO) {
            database.photoDatabaseDao.update(notes)
        }
    }

    suspend fun delete(notes: PhotoNotes) {
        withContext(Dispatchers.IO) {
            database.photoDatabaseDao.delete(notes)
        }
    }

    suspend fun get(key: Long): PhotoNotes {
        var notes: PhotoNotes
        withContext(Dispatchers.IO) {
            notes = database.photoDatabaseDao.get(key)
        }
        return notes
    }

    fun getAll(): LiveData<List<PhotoNotes>> {
        return database.photoDatabaseDao.getAll()
    }

    fun searchList(search: String): LiveData<List<PhotoNotes>> {
        Log.i("reposearch", search)
        return database.photoDatabaseDao.searchTitle(search)
    }
}