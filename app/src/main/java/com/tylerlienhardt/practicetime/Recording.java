package com.tylerlienhardt.practicetime;

/**
 * Created by Tyler on 4/20/2018.
 */

public class Recording {

    String name;
    String date;

    public Recording (String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateString() {
        return date;
    }

}
