package com.example.androidutilities;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabs;
    private TabAdapter tabAdapter;
    NotificationManager mNotificationManager;
    public static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        createNotificationChannel();
        tabAdapter = new TabAdapter(this,getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(tabAdapter);
        tabs = findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(viewPager);

        tabs.getTabAt(0).setIcon(R.drawable.ic_action_name);
        tabs.getTabAt(1).setIcon(R.drawable.ic_timer);
        tabs.getTabAt(2).setIcon(R.drawable.ic_stopwatch);
    }

    public void createNotificationChannel() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "Utility Timer";
            String description = "Manages notification for Timer";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL_ID,channelName,importance);
            channel.setDescription(description);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

}
