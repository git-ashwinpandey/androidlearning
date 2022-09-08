package com.example.androidutilities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleStopWatch extends Fragment {
    private CountDownTimer countDownTimer;
    private TextView mShowCount;
    private Button mStartPause,mReset;
    private Boolean isRunning,canResume;
    private EditText mMinutes,mSeconds;
    private long minutes,seconds,totalRunTime,timeRemaining;

    public SimpleStopWatch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_simple_stop_watch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mShowCount = view.findViewById(R.id.showCount);
        mStartPause = view.findViewById(R.id.startPause);
        mReset = view.findViewById(R.id.reset);

        mMinutes = view.findViewById(R.id.minutes);
        mSeconds = view.findViewById(R.id.seconds);

        mReset.setVisibility(View.GONE);

        setBoolean(false,false);

        mStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning){
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        if (savedInstanceState != null){
            timeRemaining = savedInstanceState.getLong("TIME");
            isRunning = savedInstanceState.getBoolean("IS_RUNNING");
            canResume = savedInstanceState.getBoolean("CAN_RESUME");
            if (isRunning){
                startTimer();
            } else {
                updateTimer(timeRemaining);
            }
        }
    }

    private void startTimer() {
        if (!canResume){
            minutes = Integer.parseInt(mMinutes.getText().toString());
            seconds = Integer.parseInt(mSeconds.getText().toString());
            totalRunTime = minutes*60*1000 + seconds*1000;
            timeRemaining = totalRunTime;
        }
        mReset.setVisibility(View.GONE);
        mShowCount.setBackgroundResource(R.drawable.textview_back);
        isRunning = true;
        mStartPause.setText(R.string.pause);

        countDownTimer = new CountDownTimer(timeRemaining,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimer(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                mShowCount.setText(R.string.timer_up);
                mStartPause.setText(R.string.start);
                mReset.setVisibility(View.VISIBLE);
                setBoolean(false,false);
            }
        }.start();
    }

    private void pauseTimer() {
        mStartPause.setText(R.string.resume);
        mReset.setVisibility(View.VISIBLE);
        countDownTimer.cancel();
        setBoolean(false,true);
    }

    private void resetTimer() {
        mShowCount.setText("");
        mStartPause.setText(R.string.start);
        timeRemaining = totalRunTime;
        setBoolean(false,false);
        mReset.setVisibility(View.GONE);
        mShowCount.setBackgroundResource(0);
    }

    private void updateTimer(long saveTimer) {
        timeRemaining = saveTimer;
        int updateMin = (int) (saveTimer/1000) / 60;
        int updateSec = (int) (saveTimer/1000) % 60;
        String textString = String.format(Locale.getDefault(),"%02d:%02d",updateMin,updateSec);
        mShowCount.setText(textString);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        canResume = true;
        outState.putLong("TIME",timeRemaining);
        outState.putBoolean("IS_RUNNING",isRunning);
        outState.putBoolean("CAN_RESUME",canResume);
        super.onSaveInstanceState(outState);
    }

    private void setBoolean(Boolean isRunning, Boolean canResume){
        this.isRunning = isRunning;
        this.canResume = canResume;
    }
}
