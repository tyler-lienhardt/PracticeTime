package com.tylerlienhardt.practicetime;

import android.app.Activity;
import android.content.Context;
import android.media.MediaActionSound;
import android.media.MediaPlayer;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Tyler on 4/27/2018.
 */

public class Metronome extends Thread{

    double bpm;
    int measure, counter;

    boolean isRunning = false;

    Exercise exercise;
    Context context;
    MediaPlayer tickSound;
    
    ImageButton metroPlayButton;

    public Metronome(Exercise exercise, Context context){
        this.exercise = exercise;
        this.context = context;

        // sets the rest of the parameters deriving from exercise
        setExercise(exercise);

        tickSound = MediaPlayer.create(context, R.raw.temp_click);
        
        metroPlayButton = ((Activity) context).findViewById(R.id.metro_play_button);
    }

    @Override
    public void run() {
        isRunning = true;
        updatePlayButtonImage();

        while(isRunning){
            try {
                sleep((long)(1000*(60.0/bpm)));
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
            counter++;

//            if (counter%measure==0){
//                tickSound.start();
//            }else{
//                tickSound.start();
//            }

            //in case isRunning is set to false while thread is sleeping,
            //this prevents one last tick sound from being played after pause is pressed
            if(isRunning) {
                tickSound.start();
            }

            System.out.println("TICK");
        }
    }

    public void stopRunning() {
        isRunning = false;
        updatePlayButtonImage();
        tickSound.pause();
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

}
