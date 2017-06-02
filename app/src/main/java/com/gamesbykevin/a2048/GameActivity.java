package com.gamesbykevin.a2048;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.gamesbykevin.a2048.opengl.GLSurf;

import java.util.Random;

public class GameActivity extends BaseActivity {

    //our game view where action takes place
    private GameView gameView;

    //our open GL surface view
    private GLSurfaceView glSurfaceView;

    /**
     * Create a random object which the seed as the current time stamp
     */
    public static Random RANDOM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the content view
        setContentView(R.layout.activity_game);

        //get the current timestamp
        final long time = System.nanoTime();

        //print the random seed
        MainActivity.logEvent("Random seed: " + time);

        //create our Random object
        RANDOM = new Random(time);

        //obtain our opengl surface view object for reference
        this.glSurfaceView = (GLSurf)findViewById(R.id.openglView);

        //create our game view object
        //this.gameView = new GameView(this);

        //add callback to game view so we can capture motion events
        //getGameView().getHolder().addCallback(getGameView());

        //make it the activity view
        //setContentView(getGameView());
    }

    /**
     * Get the game view
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
        //getGameView().pause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //resume the game view
        //getGameView().resume();
        glSurfaceView.onResume();
    }

    @Override
    public void onBackPressed() {
        //don't do anything in case user hit by accident
        //return;

        super.onBackPressed();
    }
}