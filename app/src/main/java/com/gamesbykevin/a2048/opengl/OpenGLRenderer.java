package com.gamesbykevin.a2048.opengl;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.gamesbykevin.a2048.GameActivity;
import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.game.GameManager;

import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.MainActivity.DEBUG;

/**
 * Created by Kevin on 6/1/2017.
 */

public class OpenGLRenderer implements Renderer {


    private final Context activity;

    private Sprite sprite1, sprite2;

    //get the ratio of the users screen compared to the default dimensions for the render
    private float scaleRenderX, scaleRenderY;

    public OpenGLRenderer(Context activity) {

        this.activity = activity;
    }

    public void onPause() {

    }

    public void onResume() {

    }

    /**
     * Called once to set up the view's OpenGL ES environment
     * @param gl
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        this.sprite1 = new Sprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.blocks), gl);
        this.sprite2 = new Sprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.yt), gl);

        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_ALPHA_TEST);

        //show alpha if greater than 0.01f
        gl.glAlphaFunc(GL10.GL_GREATER, 0.01f);

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Called for each redraw of the view
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {

        //clears the screen and depth buffer.
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        //reset the projection matrix
        gl.glLoadIdentity();

        //scale to our game dimensions to match the users screen
        gl.glScalef(scaleRenderX, scaleRenderY, 0.0f);



        //render game objects

        sprite1.draw(gl, 0, 0, OpenGLSurfaceView.WIDTH, OpenGLSurfaceView.WIDTH);
        sprite2.draw(gl, 240, 600, 64, 64);
    }

    /**
     *  Called if the geometry of the view changes.<br>
     *  For example when the device's screen orientation changes
     * @param gl
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        //store the ratio for the render
        this.scaleRenderX = width / (float)OpenGLSurfaceView.WIDTH;
        this.scaleRenderY = height / (float)OpenGLSurfaceView.HEIGHT;

        //sets the current view port to the new size of the screen
        gl.glViewport(0, 0, width, height);

        //reset the projection matrix back to its default state
        gl.glLoadIdentity();

        //select the projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);

        //set dimensions
        gl.glOrthof(0.0f, width, height, 0.0f, 1.0f, -1.0f);

        //select the model view matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);

        //enable 2d textures
        gl.glEnable(GL10.GL_TEXTURE_2D);

        //enable alpha blending with textures
        gl.glEnable(GL10.GL_BLEND);

        //add blend function for alpha
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    }
}