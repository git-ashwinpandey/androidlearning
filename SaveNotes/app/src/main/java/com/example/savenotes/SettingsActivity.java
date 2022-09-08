package com.example.savenotes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class SettingsActivity extends AppCompatActivity {
    private Button mBackup,mDeleteBackup;
    private Switch mDarkSwitch;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mBackup = findViewById(R.id.backup);
        mDeleteBackup = findViewById(R.id.deleteBackup);



        mBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = new File(Environment.getExternalStorageDirectory(), "SaveNotes/Backup");
                new DirectoryCleaner(dir).clean();
                dir.delete();
                copyFileOrDirectory("/storage/emulated/0/SaveNotes/","/storage/emulated/0/SaveNotes/");
                File oldfolder = new File("/storage/emulated/0/SaveNotes/","SaveNotes");
                File newfolder = new File("/storage/emulated/0/SaveNotes/","Backup");
                oldfolder.renameTo(newfolder);
            }
        });

        mDeleteBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = new File(Environment.getExternalStorageDirectory(), "SaveNotes/Backup");
                new DirectoryCleaner(dir).clean();
                dir.delete();
            }
        });

        mDarkSwitch = findViewById(R.id.switch1);

        mDarkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    storePreference(true);
                } else {
                    // The toggle is disabled
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    storePreference(false);
                }
            }
        });

        mPreferences = getSharedPreferences(MainActivity.sharedPrefFile, MODE_PRIVATE);
        boolean checked = mPreferences.getBoolean("IS_CHECKED",false);
        mDarkSwitch.setChecked(checked);
    }

    public static void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String[] files = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public class DirectoryCleaner {
        private final File mFile;

        public DirectoryCleaner(File file) {
            mFile = file;
        }

        public void clean() {
            if (null == mFile || !mFile.exists() || !mFile.isDirectory()) return;
            for (File file : mFile.listFiles()) {
                delete(file);
            }
        }

        private void delete(File file) {
            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    delete(child);
                }
            }
            file.delete();

        }
    }

    private void storePreference(Boolean checked) {
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        if (checked){
            preferencesEditor.putBoolean("IS_CHECKED", true);
        } else {
            preferencesEditor.putBoolean("IS_CHECKED", false);
        }
        preferencesEditor.apply();
    }
}