package com.gamesbykevin.a2048.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.gamesbykevin.a2048.GameActivity;
import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.game.GameManager;

import java.util.Calendar;

import static com.gamesbykevin.a2048.MainActivity.DEBUG;

/**
 * Created by Kevin on 6/1/2017.
 */

public class OpenGLSurfaceView extends GLSurfaceView implements Runnable {

    /**
     * The version of open GL we are using
     */
    public static final int OPEN_GL_VERSION = 1;

    /**
     * Our object where we render our pixel data
     */
    private final OpenGLRenderer openGlRenderer;

    //our game mechanics will run on this thread
    private Thread thread;

    //keep our thread running
    private volatile boolean running = true;

    //track the time to keep a steady game speed
    private long previous;

    private long previousUpdate;
    private long previousDraw;
    private long postUpdate;
    private long postDraw;

    /**
     * Frames per second
     */
    public static final int FPS = 60;

    /**
     * The duration of each frame (milliseconds)
     */
    public static final long FRAME_DURATION = (long)(1000.0d / FPS);

    /**
     * How many milliseconds per second
     */
    public static final long MILLISECONDS_PER_SECOND = 1000L;

    //count the number of frames for debugging purposes
    private int frames = 0;

    //keep track of time for debug purposes
    private Calendar calendar = Calendar.getInstance();

    /**
     * Default dimensions this game was designed for
     */
    public static final int WIDTH = 480;

    /**
     * Default dimensions this game was designed for
     */
    public static final int HEIGHT = 800;

    //get the ratio of the users screen compared to the default dimensions for the motion event
    private float scaleMotionX, scaleMotionY;

    //get the ratio of the users screen compared to the default dimensions for the render
    private float scaleRenderX, scaleRenderY;

    //manage game specific objects
    private GameManager manager;

    private final Context activity;

    public OpenGLSurfaceView(Context activity) {

        //call overloaded constructor
        this(activity, null);
    }

    public OpenGLSurfaceView(Context activity, AttributeSet attrs) {

        //call parent constructor
        super(activity, attrs);

        //store our activity reference
        this.activity = activity;

        //create a new instance of game manager
        this.manager = new GameManager(activity);

        //create an OpenGL ES 1.0 context.
        setEGLContextClientVersion(OPEN_GL_VERSION);

        //create a new instance of our renderer
        this.openGlRenderer = new OpenGLRenderer(activity, this.manager);

        //set the renderer for drawing on the gl surface view
        setRenderer(this.openGlRenderer);

        //set render mode to only draw when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    /**
     * If the game is paused stop our thread
     */
    @Override
    public void onPause() {

        //call parent function
        super.onPause();

        //also pause our render
        this.openGlRenderer.onPause();

        //flag that we don't want our thread to continue running
        this.running = false;

        try {
            //wait for thread to finish
            this.thread.join();
        } catch (Exception e) {
            MainActivity.handleException(e);
        }
    }

    /**
     * Once we resume start a new thread again
     */
    @Override
    public void onResume() {

        //call parent function
        super.onResume();

        //also resume our render
        this.openGlRenderer.onResume();

        //flag running true
        this.running = true;

        //create the thread
        this.thread = new Thread(this);

        //start the thread
        this.thread.start();
    }

    @Override
    public void run() {

        //do we continue to loop
        while (running) {

            try {

                //get the current time
                this.previous = System.currentTimeMillis();

                //update the game state
                update();

                //render the image
                draw();

                //control the game speed
                control();

            } catch (Exception e) {
                MainActivity.handleException(e);
            }
        }
    }

    /**
     * Make sure we maintain game speed
     * @throws InterruptedException
     */
    private void control() throws InterruptedException {

        //calculate how much time had passed
        final long duration = System.currentTimeMillis() - this.previous;

        //we want each loop to have the same duration to maintain fps
        long remaining = FRAME_DURATION - duration;

        //log event id this loop is running slow
        if (remaining <= 0) {
            MainActivity.logEvent("Slow: " + remaining);
            MainActivity.logEvent("Update duration: " + (this.postUpdate - this.previousUpdate));
            MainActivity.logEvent("Draw   duration: " + (this.postDraw - this.previousDraw));
        }

        //make sure we sleep at least 1 millisecond
        if (remaining < 1)
            remaining = 1;

        //make sure time remaining is a valid number
        remaining = (remaining <= 0) ? 1 : remaining;

        //sleep the thread to maintain a steady game speed
        this.thread.sleep(remaining);

        //if debugging track performance
        if (DEBUG)
            trackProgress();
    }

    /**
     * Track progress for debugging purposes
     */
    private void trackProgress() {

        //keep track of the frames
        this.frames++;

        //if 1 second has passed, print fps counter
        if (System.currentTimeMillis() - calendar.getTimeInMillis() >= MILLISECONDS_PER_SECOND) {

            //print progress
            MainActivity.logEvent("FPS: " + frames);

            //reset timer for next update
            calendar = Calendar.getInstance();

            //reset frame count
            this.frames = 0;
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {

        try
        {
            //adjust the coordinates where touch event occurred
            final float x = event.getRawX() * scaleMotionX;
            final float y = event.getRawY() * scaleMotionY;

            //update game accordingly
            this.manager.onTouchEvent(event.getAction(), x, y);
        }
        catch (Exception e)
        {
            MainActivity.handleException(e);
        }

        MainActivity.logEvent("Action: " + event.getAction());
        MainActivity.logEvent("Action Masked: " + event.getActionMasked());

        //return true to keep receiving touch events
        return true;
    }

    /**
     * Update the game state
     */
    private void update() {

        //track time before update
        this.previousUpdate = System.currentTimeMillis();

        //update game logic here
        this.manager.update();

        //track time after update
        this.postUpdate = System.currentTimeMillis();
    }

    /**
     * Render the game image to screen surface
     */
    private void draw() {

        //track time before draw
        this.previousDraw = System.currentTimeMillis();

        try {

            //render game objects
            //this.manager.draw(this.canvas);
            requestRender();

        } catch (Exception e) {
            MainActivity.handleException(e);
        }

        //track time after draw
        this.postDraw = System.currentTimeMillis();
    }
}