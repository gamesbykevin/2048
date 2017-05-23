package com.gamesbykevin.a2048;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    /**
     * The amount of time to display the splash screen (in milliseconds)
     */
    public static final long SPLASH_DELAY = 2500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //delay a couple seconds before going to main page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }

        }, SPLASH_DELAY);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}