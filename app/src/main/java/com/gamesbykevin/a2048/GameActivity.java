package com.gamesbykevin.a2048;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;

public class GameActivity extends AppCompatActivity {

    //our game view where action takes place
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //create our game view object
        this.gameView = new GameView(this);

        this.gameView.getHolder().addCallback(this.gameView);

        //make it the activity view
        setContentView(this.gameView);
    }

    @Override
    protected void onStart()
    {
        //call parent functionality
        super.onStart();

        this.gameView.render();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //cleanup resources
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        //don't do anything in case user hit by accident
        //return;

        super.onBackPressed();
    }
}
