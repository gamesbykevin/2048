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

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    //area where game play is rendered
    private final SurfaceHolder holder;

    //our canvas to render image(s)
    private Canvas canvas;

    public GameView(GameActivity activity) {
        super(activity);

        this.holder = getHolder();

    }

    public void render() {
        try {
            this.canvas = this.holder.lockCanvas();


            //make sure no other threads are accessing the holder
            synchronized (this.holder) {
                //if the canvas object was obtained, render
                draw(this.canvas);
            }

            this.holder.unlockCanvasAndPost(this.canvas);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    @Override
    public void onDraw(Canvas canvas) {
        draw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            super.draw(canvas);

            //store the canvas state
            final int savedState = canvas.save();

            //red background
            canvas.drawColor(Color.RED);

            canvas.drawText("Hi", 100, 100, new Paint());

            this.canvas.restoreToCount(savedState);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}