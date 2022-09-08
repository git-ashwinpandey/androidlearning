package com.example.photonotes.addnotes

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photonotes.database.PhotoDatabase
import com.example.photonotes.database.PhotoNotes
import com.example.photonotes.repository.PhotoNotesRepository
import kotlinx.coroutines.launch

class AddViewModel(private val noteKey: Long, private val application: Application) : ViewModel() {

    private val database = PhotoDatabase.getInstance(application)
    private val repository = PhotoNotesRepository(database)

    private var _selectedPhoto = MutableLiveData<PhotoNotes>()
    val selectedPhoto: LiveData<PhotoNotes>
        get() = _selectedPhoto

    init {
        if (noteKey != -1L) {
            loadFromDatabase()
            Log.i("NOTEKEY", noteKey.toString())
        }
    }

    fun saveToDatabase(title: String, description: String, date: String,photo: ByteArray) {
        if (noteKey == -1L) {
            val notes = PhotoNotes(title, description, date,photo)
            saveNew(notes)
        } else {
            val notes = PhotoNotes(title, description, date, photo, photoID = noteKey)
            updateExisting(notes)
        }
    }

    private fun saveNew(notes: PhotoNotes) {
        viewModelScope.launch {
            repository.insert(notes)
        }
    }

    private fun updateExisting(notes: PhotoNotes) {
        viewModelScope.launch {
            repository.update(notes)
        }
    }

    private fun loadFromDatabase(){
        viewModelScope.launch {
            _selectedPhoto.value = repository.get(noteKey)
        }
    }

}