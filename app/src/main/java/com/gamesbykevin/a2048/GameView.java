package com.gamesbykevin.a2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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

    public GameView(GameActivity activity) {
        super(activity);

        //get the holder surface for rendering
        this.holder = getHolder();
    }

    @Override
    public void run() {

        //do we continue to loop
        while (running) {

            try {

                //update the game state
                update();

                //render the image
                draw();

                //control the game speed
                control();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Update the game state
     */
    private void update() {

    }

    /**
     * Make sure we maintain game speed
     * @throws InterruptedException
     */
    private void control() throws InterruptedException {
        this.thread.sleep(17);
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
            e.printStackTrace();
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
    }

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        Log.i("2048", "Action: " + event.getAction());

        //return true because we want to keep receiving touch events
        return true;
    }

    /**
     * Render the game image to screen surface
     */
    private void draw() {
        try {

            //get the canvas
            this.canvas = this.holder.lockCanvas();

            //make sure the canvas is available
            if (this.canvas != null) {

                //make sure no other threads are accessing the holder
                synchronized (this.holder) {

                    //store the canvas state
                    final int savedState = this.canvas.save();

                    //do our drawing
                    this.canvas.drawColor(Color.RED);

                    //restore the canvas state
                    this.canvas.restoreToCount(savedState);
                }

                //let go and render image
                this.holder.unlockCanvasAndPost(this.canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}