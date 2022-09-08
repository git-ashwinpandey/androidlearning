package com.example.savenotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private RecyclerView myRecyclerView;
    private MyAdapter mAdapter;
    private static final int ADD_NOTE_REQUEST_CODE = 0;
    private static final int EDIT_NOTE_REQUEST_CODE = 1;
    private String title, description;
    private NotesViewModel mViewModel;
    private EditText filterEditText;
    private int STORAGE_PERMISSION_CODE = 1;

    private SharedPreferences mPreferences;
    static String sharedPrefFile = "com.example.android.savenotes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        checkPermission();

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);



        mPreferences = getSharedPreferences(MainActivity.sharedPrefFile, MODE_PRIVATE);

        boolean checked = mPreferences.getBoolean("IS_CHECKED",false);
        if (checked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        switch (itemID) {
            case R.id.setting_overflow:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            title = data.getStringExtra(AddNotesActivity.TITLE_TAG);
            description = data.getStringExtra(AddNotesActivity.DESCRIPTION_TAG);
            Notes notes = new Notes(title, description);
            switch (requestCode) {
                case ADD_NOTE_REQUEST_CODE:
                    mViewModel.insert(notes);
                    break;
                case EDIT_NOTE_REQUEST_CODE:
                    notes.setId(data.getIntExtra(AddNotesActivity.ID_TAG, -1));
                    mViewModel.update(notes);
                    break;
            }
        }

    }

    private void showFilterPopup(final View v, final int pos) {
        PopupMenu popup = new PopupMenu(this, v);
        // Inflate the menu from xml
        popup.inflate(R.menu.popup_menu);
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_popup:
                        deleteNotes(pos);
                        return true;
                    case R.id.edit_popup:
                        editNotes(v, pos);
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show();
    }

    public void editNotes(View view, int position) {
        Intent intent = new Intent(view.getContext(), AddNotesActivity.class);
        Notes myNotes = mAdapter.getNotesAtPosition(position);
        intent.putExtra(AddNotesActivity.TITLE_TAG, myNotes.getTitle());
        intent.putExtra(AddNotesActivity.DESCRIPTION_TAG, myNotes.getContent());
        intent.putExtra(AddNotesActivity.ID_TAG, myNotes.getId());
        startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE);
    }

    public void deleteNotes(int position) {
        Notes myNotes = mAdapter.getNotesAtPosition(position);
        mViewModel.delete(myNotes);
    }

    public void checkPermission() {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            prepareViews();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission available", Toast.LENGTH_SHORT).show();
                prepareViews();
            } else {
                Toast.makeText(this, "App cannot function without Storage Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void prepareViews() {
        myRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new MyAdapter(this);
        myRecyclerView.setAdapter(mAdapter);
        myRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TO-DO
                Intent intent = new Intent(view.getContext(), AddNotesActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST_CODE);
            }
        });

        filterEditText = findViewById(R.id.searchTitle);

        mViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        mViewModel.getAllNotes().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {
                mAdapter.setNotes(notes);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteNotes(viewHolder.getAdapterPosition());
            }
        });

        helper.attachToRecyclerView(myRecyclerView);

        mAdapter.setOnClickListener(new MyAdapter.OnClickListener() {
            @Override
            public void onShortClick(View view, int position) {
                editNotes(view, position);
            }

            @Override
            public void onLongClick(View view, int position) {
                showFilterPopup(view, position);
            }
        });
    }

    public List<Notes> filterMyNotes(List<Notes> notes,String filter){
        List<Notes> myList = new ArrayList<>();
        for (Notes myNotes : notes){
            if (myNotes.getTitle().contains(filter)){
                myList.add(myNotes);
            }
        }
        return myList;
    }
}
