package com.tylerlienhardt.practicetime;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tyler on 4/9/2018.
 */

public class Timer extends CountDownTimer {
    private Exercise exercise;
    private long startTime;
    private boolean isCounting = false;
    private Context context;
    private TextView timerDisplay;
    private ImageButton timerPlayButton;

    MediaPlayer dingPlayer;

    public Timer (long StartTime, Exercise exercise, Context context) {
        super(StartTime, 500);
        this.exercise = exercise;
        this.startTime = startTime;
        this.context = context;

        timerDisplay = ((Activity) context).findViewById(R.id.timer_display);
        timerPlayButton = ((Activity) context).findViewById(R.id.timer_play_button);

        dingPlayer = MediaPlayer.create(context, R.raw.ding);

    }

    public void startTimer() {
        start();
        isCounting = true;
        updatePlayButtonImage();
    }


    @Override
    public void onTick(long millisUntilFinished) {
        timerDisplay.setText(timeToString(millisUntilFinished));
        exercise.setRemainingTime(millisUntilFinished);
    }

    public void cancelTimer() {
        cancel();
        isCounting = false;
        updatePlayButtonImage();
    }

    @Override
    public void onFinish() {
        timerDisplay.setText("0:00");
        dingPlayer.start();

        ((MainActivity) context).notifyTimerFinished();
    }

    public static String timeToString(long millisUntilFinished) {
        int seconds = (int)millisUntilFinished / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;

        return String.format("%d:%02d", minutes, seconds);
    }

    public static long stringToTime(String timeString) {

        Scanner scanner = new Scanner(timeString);
        scanner.useDelimiter(":");

        long minutes = scanner.nextLong() * 60000;
        long seconds = scanner.nextLong() * 1000;

        return minutes + seconds;
    }

    public void updatePlayButtonImage() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isCounting == true) {
                    timerPlayButton.setImageResource(R.drawable.pause_button);
                }
                else if (isCounting == false) {
                    timerPlayButton.setImageResource(R.drawable.play_button);
                }
            }
        });
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean isCounting() {
        return isCounting;
    }

    public void setCounting(boolean counting) {
        isCounting = counting;
    }

}
