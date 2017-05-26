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

        //add callback to game view so we can capture motion events
        getGameView().getHolder().addCallback(getGameView());

        //make it the activity view
        setContentView(getGameView());
    }

    /**
     * Get the gamem view
     * @return Our object containing game mechanics / rendering etc...
     */
    private GameView getGameView() {
        return this.gameView;
    }

    @Override
    protected void onStart()
    {
        //call parent functionality
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //cleanup resources
    }

    @Override
    protected void onPause() {
        super.onPause();

        //pause the game view
        getGameView().pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //resume the game view
        getGameView().resume();
    }

    @Override
    public void onBackPressed() {
        //don't do anything in case user hit by accident
        //return;

        super.onBackPressed();
    }
}