package com.tylerlienhardt.practicetime;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tyler on 4/9/2018.
 */

public class Timer extends CountDownTimer{
    private long startTime;
    private long remainingTime;
    private boolean isCounting;
    private Context context;
    private TextView timerDisplay;
    private ImageButton timerPlayButton;

    public Timer (long startTime, long interval, Context context) {
        super(startTime, interval);
        this.startTime = startTime;
        this.remainingTime = startTime;
        this.context = context;

        timerDisplay = ((Activity) context).findViewById(R.id.timer_display);
        timerPlayButton = ((Activity) context).findViewById(R.id.timer_play_button);
    }

    public static String timeToString(long millisUntilFinished) {
        int seconds = (int)millisUntilFinished / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;

        return String.format("%d:%02d", minutes, seconds);
    }

    public static long stringToTime(String timeString) {

        String testString = "11:22";
        Scanner scanner = new Scanner(testString);
        scanner.useDelimiter(":");

        long minutes = scanner.nextLong();
        long seconds = scanner.nextLong();

        System.out.println("MINUTES = " + minutes + " .... SECONDS = " + seconds);

        return 0;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timerDisplay.setText(timeToString(millisUntilFinished));
        remainingTime = millisUntilFinished;

    }

    @Override
    public void onFinish() {
        Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
    }

    public long getStartTime() {
        return startTime;
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
        timerPlayButton.setImageResource(android.R.drawable.ic_media_pause);
        isCounting = true;
    }

    public void cancelTimer() {
        cancel();
        timerPlayButton.setImageResource(android.R.drawable.ic_media_play);
        isCounting = false;
    }
}
