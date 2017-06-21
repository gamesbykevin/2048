package com.gamesbykevin.a2048;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.gamesbykevin.a2048.opengl.OpenGLSurfaceView;

import java.util.Random;

public class GameActivity extends BaseActivity {

    //our open GL surface view
    private GLSurfaceView glSurfaceView;

    /**
     * Create a random object which the seed as the current time stamp
     */
    private static Random RANDOM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //call parent
        super.onCreate(savedInstanceState);

        //set the content view
        setContentView(R.layout.activity_game);

        //obtain our open gl surface view object for reference
        this.glSurfaceView = (OpenGLSurfaceView)findViewById(R.id.openglView);


        //create our game view object
        //this.gameView = new GameView(this);

        //add callback to game view so we can capture motion events
        //getGameView().getHolder().addCallback(getGameView());

        //make it the activity view
        //setContentView(getGameView());
    }

    /**
     * Get our random object.<br>
     * If object is null a new instance will be instantiated
     * @return Random object used to generate random events
     */
    public static Random getRandomObject() {
        if (RANDOM == null) {

            //get the current timestamp
            final long time = System.nanoTime();

            //create our Random object
            RANDOM = new Random(time);

            //print the random seed
            MainActivity.logEvent("Random seed: " + time);
        }

        return RANDOM;
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