package com.example.photonotes.overview

import android.app.Application
import androidx.lifecycle.*
import com.example.photonotes.database.PhotoDAO
import com.example.photonotes.database.PhotoDatabase
import com.example.photonotes.database.PhotoNotes
import com.example.photonotes.repository.PhotoNotesRepository
import kotlinx.coroutines.launch

class OverviewViewModel(application: Application): ViewModel() {
    private val database = PhotoDatabase.getInstance(application)
    private val repository = PhotoNotesRepository(database)

    private var searchQuery = MutableLiveData<String>()


    val allNotes: LiveData<List<PhotoNotes>> = repository.getAll()
    val searchNotes : LiveData<List<PhotoNotes>> = repository.searchList(searchQuery.value.toString())



    fun getAll(): LiveData<List<PhotoNotes>> {
        return allNotes
    }

    fun insert(notes: PhotoNotes) {
        viewModelScope.launch {
            repository.insert(notes)
        }
    }

    fun delete(notes: PhotoNotes) {
        viewModelScope.launch {
            repository.delete(notes)
        }
    }

    fun update(notes: PhotoNotes) {
        viewModelScope.launch {
            repository.update(notes)
        }
    }

    fun setQuery(search: String) {
        searchQuery.value = search
    }

    fun getQuery() : String {
        return searchQuery.value!!
    }
}