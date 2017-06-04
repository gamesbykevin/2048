package com.gamesbykevin.a2048.opengl;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import com.gamesbykevin.a2048.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Created by Kevin on 6/1/2017.
 */

public class GLRenderer implements Renderer {

    //our square that we want to render
    private Sprite sprite;

    private Context mContext;

    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    public GLRenderer(Context context) {
        this.mContext = context;
    }

    public void onPause() {

    }

    public void onResume() {

    }

    /**
     * Called once to set up the view's OpenGL ES environment
     * @param unused
     * @param config
     */
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color to black and full transparency
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //now that the surface is created, lets create our sprite image
        this.sprite = new Sprite(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fb));
    }

    /**
     * Called for each redraw of the view
     * @param unused
     */
    public void onDrawFrame(GL10 unused) {

        //Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //draw the square
        this.sprite.draw(mtrxProjectionAndView);
    }

    /**
     *  Called if the geometry of the view changes.<br>
     *  For example when the device's screen orientation changes
     * @param unused
     * @param width
     * @param height
     */
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Clear our matrices
        for(int i=0; i<mtrxProjectionAndView.length; i++)
        {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0f, 480, 0.0f, 800, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
    }
}