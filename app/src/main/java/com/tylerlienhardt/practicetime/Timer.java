package com.tylerlienhardt.practicetime;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

/**
 * Created by Tyler on 4/9/2018.
 */

public class Timer extends CountDownTimer{
    private long startTime;
    private long remainingTime;
    private boolean isCounting;
    private Context context;
    private String displayString;
    private TextView timerDisplay;

    public Timer (long startTime, long interval, Context context) {
        super(startTime, interval);
        this.startTime = startTime;
        this.remainingTime = startTime;
        this.context = context;


        timerDisplay = ((Activity) context).findViewById(R.id.timer_display);
    }

    public static String timeToString(long millisUntilFinished) {
        int seconds = (int)millisUntilFinished / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;

        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timerDisplay.setText(timeToString(millisUntilFinished));
        remainingTime = millisUntilFinished;

    }

    @Override
    public void onFinish() {
        Toast.makeText(context, "TIME'S UP MOTHERFUCKER", Toast.LENGTH_SHORT).show();
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public boolean isCounting() {
        return isCounting;
    }

    public void setCounting(boolean counting) {
        isCounting = counting;
    }

    public void startTimer() {
        start();
        isCounting = true;
    }

    public void cancelTimer() {
        cancel();
        isCounting = false;
    }
}
