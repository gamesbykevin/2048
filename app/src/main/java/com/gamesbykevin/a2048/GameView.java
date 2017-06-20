package com.gamesbykevin.a2048;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gamesbykevin.a2048.game.GameManager;

import java.util.Calendar;

import static com.gamesbykevin.a2048.MainActivity.DEBUG;


/**
 * Created by Kevin on 5/24/2017.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    //area where game play is rendered
    private final SurfaceHolder holder;

    //our canvas to render image(s)
    private Canvas canvas;

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
    public static final int FPS = 30;

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

    public GameView(GameActivity activity) {

        //call parent constructor
        super(activity);

        //get the holder surface for rendering
        this.holder = getHolder();

        //create a new instance of game manager
        this.manager = new GameManager(activity);
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

    /**
     * Pause the game. Stop the main game thread
     */
    public void pause() {

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
     * When game is resumed start thread again
     */
    public void resume() {
        this.running = true;

        //create the thread
        this.thread = new Thread(this);

        //start the thread
        this.thread.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        //surface shouldn't change as we are keeping same orientation
    }

    /**
     * When the surface is created we need make calculations based on screen size.
     * @param holder Object containing the surface
     */
    public void surfaceCreated(SurfaceHolder holder) {
        //store the ratio for the motion event
        this.scaleMotionX = (float)WIDTH / getWidth();
        this.scaleMotionY = (float)HEIGHT / getHeight();

        //store the ratio for the render
        this.scaleRenderX = getWidth() / (float)WIDTH;
        this.scaleRenderY = getHeight() / (float)HEIGHT;
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        //we handle pause game in GameActivity, no need to do anything here
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

            //get the canvas
            this.canvas = this.holder.lockCanvas();

            //make sure the canvas is available
            if (this.canvas != null) {

                //make sure no other threads are accessing the holder
                synchronized (this.holder) {

                    //store the canvas state
                    final int savedState = this.canvas.save();

                    //scale to the screen size
                    this.canvas.scale(scaleRenderX, scaleRenderY);

                    //do our drawing
                    this.canvas.drawColor(Color.BLACK);

                    //render game objects
                    //this.manager.draw(this.canvas);

                    //restore the canvas state
                    this.canvas.restoreToCount(savedState);
                }

                //let go and render image
                this.holder.unlockCanvasAndPost(this.canvas);
            }
        } catch (Exception e) {
            MainActivity.handleException(e);
        }

        //track time after draw
        this.postDraw = System.currentTimeMillis();
    }
}