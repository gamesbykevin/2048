package com.gamesbykevin.a2048;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamesbykevin.a2048.game.GameManager;
import com.gamesbykevin.a2048.opengl.OpenGLSurfaceView;

import java.util.Random;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class GameActivity extends BaseActivity {

    //our open GL surface view
    private GLSurfaceView glSurfaceView;

    /**
     * Create a random object which the seed as the current time stamp
     */
    private static Random RANDOM;

    /**
     * Our game manager class
     */
    public static GameManager MANAGER;

    //has the activity been paused
    private boolean paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //call parent
        super.onCreate(savedInstanceState);

        //create our game manager
        MANAGER = new GameManager(this);

        //set the content view
        setContentView(R.layout.activity_game);

        //obtain our open gl surface view object for reference
        this.glSurfaceView = (OpenGLSurfaceView)findViewById(R.id.openglView);
    }

    /**
     * Get our random object.<br>
     * If object is null a new instance will be instantiated
     * @return Random object used to generate random events
     */
    public static Random getRandomObject() {

        //create the object if null
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
    protected void onStart() {

        //call parent
        super.onStart();
    }

    @Override
    protected void onDestroy() {

        //call parent
        super.onDestroy();

        //cleanup resources
    }

    @Override
    protected void onPause() {

        //call parent
        super.onPause();

        //flag paused true
        this.paused = true;

        //pause the game view
        glSurfaceView.onPause();

        //flag for recycling
        glSurfaceView = null;
    }

    @Override
    protected void onResume() {

        //call parent
        super.onResume();

        //if the game was previously paused create a new view
        if (this.paused) {

            //flag paused false
            this.paused = false;

            //create a new OpenGL surface view
            glSurfaceView = new OpenGLSurfaceView(this);

            //resume the game view
            glSurfaceView.onResume();

            //set the content view
            setContentView(glSurfaceView);

        } else {

            //resume the game view
            glSurfaceView.onResume();
        }
    }

    @Override
    public void onBackPressed() {

        //call parent
        super.onBackPressed();
    }
}