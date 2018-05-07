package com.tylerlienhardt.practicetime;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Tyler on 4/29/2018.
 */

public class SoundBank extends Thread{
    Context context;
    MediaPlayer player;

    public SoundBank (Context context) {
        this.context = context;

        player = MediaPlayer.create(context, R.raw.ding);
    }

    public void playDing() {
        player.start();
    }

    public void run(){
        playDing();
    }
}
