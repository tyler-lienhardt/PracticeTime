package com.tylerlienhardt.practicetime;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.ImageButton;

/**
 * Created by Tyler on 4/27/2018.
 */

public class Metronome extends Thread{

    double bpm;
    int measure;
    int counter;

    boolean isRunning = false;

    Exercise exercise;
    Context context;


    SoundPool tickPool;
    int lowTickID;
    int highTickID;
    AudioManager audioManager;
    
    ImageButton metroPlayButton;

    public Metronome(Exercise exercise, Context context){
        this.exercise = exercise;
        this.context = context;
        this.measure = exercise.getMeasure();
        counter = measure; //to start on a high click

        // sets the rest of the parameters deriving from exercise
        setExercise(exercise);

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        tickPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        lowTickID = tickPool.load(context, R.raw.seiko_low, MAX_PRIORITY);
        highTickID = tickPool.load(context, R.raw.seiko_high, MAX_PRIORITY);
        
        metroPlayButton = ((Activity) context).findViewById(R.id.metro_play_button);

        //for metronome accuracy
        this.setPriority(MAX_PRIORITY);
    }

    @Override
    public void run() {
        isRunning = true;
        updatePlayButtonImage();

        while(isRunning){
            try {
                sleep((long)(1000 * (60.0 / bpm)));
            }catch(InterruptedException e) {
                e.printStackTrace();
            }

            //in case isRunning is set to false while thread is sleeping,
            //this prevents one last tick sound from being played after pause is pressed
            if(isRunning) {
                if (counter % measure == 0){
                    tickPool.play(highTickID, 1, 1, 10, 0, 1);
                }else{
                    tickPool.play(lowTickID, 1, 1, 10, 0, 1);
                }
            }

            counter++;
        }
    }

    public void stopRunning() {
        isRunning = false;
        updatePlayButtonImage();
    }

    public void adjustTempo(int change) {
        bpm = bpm + change;
        exercise.setTempo(exercise.getTempo() + change);
    }

    public void updatePlayButtonImage() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRunning == true) {
                    metroPlayButton.setImageResource(R.drawable.pause_button);
                }
                else if (isRunning == false) {
                    metroPlayButton.setImageResource(R.drawable.play_button);
                }
            }
        });
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
        this.bpm = exercise.getTempo();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void updateMeasure() {
        this.measure = exercise.getMeasure();
    }

}
