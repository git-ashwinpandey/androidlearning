package com.example.savenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.Inet4Address;

public class AddNotesActivity extends AppCompatActivity {
    private EditText mEditTitle, mEditContent;
    public static String TITLE_TAG = "com.example.savenotes.title_tag";
    public static String DESCRIPTION_TAG = "com.example.savenotes.desc_tag";
    public static String ID_TAG = "com.example/savenotes.id_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DarkTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        Toolbar myToolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mEditTitle = findViewById(R.id.title_edit_text);
        mEditContent = findViewById(R.id.content_edit_text);

        Intent intent = getIntent();
        if (intent.hasExtra(ID_TAG)) {
            setTitle("Edit Note");
            mEditTitle.setText(intent.getStringExtra(TITLE_TAG));
            mEditContent.setText(intent.getStringExtra(DESCRIPTION_TAG));
        } else {
            setTitle("Add Notes");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_option) {
            //TO-DO
            String title = mEditTitle.getText().toString();
            String description = mEditContent.getText().toString();

            if (title.trim().isEmpty() || description.trim().isEmpty()) {
                Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            } else {
                Intent receiveIntent = new Intent();
                receiveIntent.putExtra(TITLE_TAG, title);
                receiveIntent.putExtra(DESCRIPTION_TAG, description);
                int id = getIntent().getIntExtra(ID_TAG, -1);
                if (id != -1) {
                    receiveIntent.putExtra(ID_TAG, id);
                }
                setResult(RESULT_OK, receiveIntent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
