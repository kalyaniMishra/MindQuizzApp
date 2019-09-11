package com.example.kalyani.mindquizzapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    RelativeLayout rl;
    TextView view;
    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if((getIntent().getFlags()& Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)!=0){
            finish();
            return;
        }
        rl=findViewById(R.id.rl);
        view=findViewById(R.id.textView);
        player=MediaPlayer.create(this,R.raw.song1);
        if (!player.isLooping())
            player.start();
        TranslateAnimation animation=new TranslateAnimation(0.0f,1500.0f,0.0f,0.0f);
        animation.setDuration(10000);
        animation.setRepeatCount(1);

        animation.setFillAfter(false);
        view.startAnimation(animation);
        view.setShadowLayer(1,3,3, Color.BLACK);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,First.class));
            }
        },8000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player.isPlaying())
            player.stop();
    }
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.release();
    }
}
