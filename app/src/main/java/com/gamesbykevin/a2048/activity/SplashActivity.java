package com.gamesbykevin.a2048.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.level.Stats;

public class SplashActivity extends BaseActivity implements Runnable {

    /**
     * The amount of time to display the splash screen (in milliseconds)
     */
    public static final long SPLASH_DELAY = 2500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onResume() {
        super.onResume();

        //start our thread
        //new Thread(this).start();

        //create a new instance of our level items
        GameActivity.STATS = new Stats(this);

        //delay a couple seconds before going to main page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }

        }, SPLASH_DELAY);
    }

    /**
     * Our thread will create a new static object for our stats, then navigate to the new activity
     */
    public void run() {

        //create a new instance of our level items
        GameActivity.STATS = new Stats(this);

        //start the new activity
        startActivity(new Intent(SplashActivity.this, MainActivity.class));

        //finish this current activity
        finish();
    }

    @Override
    public void onBackPressed() {

        //don't allow user to press back button
        return;
    }
}