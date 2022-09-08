package com.example.androidutilities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;


public class SimpleTimer extends Fragment implements TimePickerDialog.OnTimeSetListener {
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private TextView mTextView;
    private int hour, min;
    private TimePicker mPicker;


    public SimpleTimer() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_simple_timer, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mNotificationManager = (NotificationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.NOTIFICATION_SERVICE);
        }

        mPicker = view.findViewById(R.id.timePicker);
        mTextView = view.findViewById(R.id.showTime);
        alarmMgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Button mButton = view.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNotificationTimer(v.getContext());
            }
        });

        Button mTime = view.findViewById(R.id.selectTime);
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DialogFragment newFragment = new TimePickerFragment();
                //newFragment.show(getChildFragmentManager(),"time_picker");
                //timePick();
            }
        });

        Button mCancel = view.findViewById(R.id.cancelAlarm);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmMgr != null) {
                    alarmMgr.cancel(alarmIntent);
                    mTextView.setText("");
                }
            }
        });

        if (savedInstanceState != null) {
            //TO-DO
        }
    }

    private void setNotificationTimer(Context context) {
        hour = mPicker.getHour();
        min = mPicker.getMinute();
        Calendar k = Calendar.getInstance(TimeZone.getDefault());
        k.set(Calendar.HOUR_OF_DAY,hour);
        k.set(Calendar.MINUTE,min);
        k.set(Calendar.SECOND,0);
        mTextView.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(k.getTime()));
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.i("testing",String.valueOf(k.getTimeInMillis()));
        Log.i("testing",String.valueOf(SystemClock.elapsedRealtime()));
        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),60000, alarmIntent);
        Log.i("testing",String.valueOf(alarmMgr.getNextAlarmClock().getTriggerTime()));

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        Log.i("testing", String.valueOf(c.get(Calendar.HOUR_OF_DAY)));
    }

    /*private void timePick() {
        TimePickerDialog.OnTimeSetListener timer = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //set time in your text view
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, 0);
                        Log.i("testing",String.valueOf(c.get(Calendar.HOUR_OF_DAY))+" onclick");
                        setNotificationTimer(c);
                    }
                };
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        new TimePickerDialog(getContext(),timer,hour,minute,true).show();

    }*/
}
