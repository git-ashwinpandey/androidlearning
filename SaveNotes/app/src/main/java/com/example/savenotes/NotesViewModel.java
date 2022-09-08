package com.example.savenotes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {
    private NotesRepository mRepository;
    private LiveData<List<Notes>> mAllNotes;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NotesRepository(application);
        mAllNotes = mRepository.getAllNotes();

    }

    public LiveData<List<Notes>> getAllNotes(){
        return mAllNotes;
    }

    public void insert(Notes notes){
        mRepository.insert(notes);
    }

    public void delete(Notes notes){
        mRepository.delete(notes);
    }

    public void update(Notes notes){
        mRepository.update(notes);
    }

}
