package com.tylerlienhardt.practicetime;

/**
 * Created by Tyler on 3/31/2018.
 */

public class Exercise {
    private String name;
    private String youTubeLink;
    private String timeDisplay;

    private int tempo;

    private long startTime;
    private long remainingTime;

    public Exercise(String name, int tempo, long startTime) {
        this.name = name;
        this.tempo = tempo;
        this.startTime = startTime;
        this.remainingTime = startTime;
    }

    public void resetRemainingTime() {
        remainingTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;

        //resets the timer
        this.remainingTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYouTubeLink() {
        return youTubeLink;
    }

    public void setYouTubeLink(String youTubeLink) {
        this.youTubeLink = youTubeLink;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public void changeTempo(int change) {
        tempo = tempo + change;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }
}
