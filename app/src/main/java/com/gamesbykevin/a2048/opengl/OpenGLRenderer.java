package com.gamesbykevin.a2048.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.support.v7.app.WindowDecorActionBar;

import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.game.GameManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.GameActivity.MANAGER;
import static com.gamesbykevin.a2048.board.Block.ANIMATION_DIMENSIONS;

/**
 * Created by Kevin on 6/1/2017.
 */

public class OpenGLRenderer implements Renderer {

    //our activity reference
    private final Context activity;

    //get the ratio of the users screen compared to the default dimensions for the render
    private float scaleRenderX, scaleRenderY;

    //get the ratio of the users screen compared to the default dimensions for the motion event
    public float scaleMotionX = 0, scaleMotionY = 0;

    //maintain list of texture id's so we can access when rendering textures
    private int[] textures;

    public OpenGLRenderer(Context activity) {
        this.activity = activity;
    }

    public void onPause() {
        //do we do anything here?
    }

    public void onResume() {
        //re-load the textures if needed
    }

    /**
     * Called for each redraw of the view
     * @param gl Object used for rendering textures
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
        MANAGER.draw(gl, this.textures);
    }

    /**
     * Called once to set up the view's OpenGL ES environment
     * @param gl Open gl object for rendering
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //do we need to to anything here?
    }

    /**
     *  Called if the geometry of the view changes.<br>
     *  For example when the device's screen orientation changes
     * @param gl OpenGL object
     * @param width pixel width of surface
     * @param height pixel height of surface
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_ALPHA_TEST);

        //show alpha if greater than 0.01f
        gl.glAlphaFunc(GL10.GL_GREATER, 0.01f);

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        //store the ratio for the render
        this.scaleRenderX = width / (float)OpenGLSurfaceView.WIDTH;
        this.scaleRenderY = height / (float)OpenGLSurfaceView.HEIGHT;

        //store the ratio when touching the screen
        this.scaleMotionX = (float)OpenGLSurfaceView.WIDTH / width;
        this.scaleMotionY = (float)OpenGLSurfaceView.HEIGHT / height;

        //sets the current view port to the new size of the screen
        gl.glViewport(0, 0, width, height);

        //reset the projection matrix back to its default state
        gl.glLoadIdentity();

        //select the projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);

        //set rendering dimensions
        gl.glOrthof(0.0f, width, height, 0.0f, 1.0f, -1.0f);

        //select the model view matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);

        //enable 2d textures
        gl.glEnable(GL10.GL_TEXTURE_2D);

        //enable alpha blending with textures
        gl.glEnable(GL10.GL_BLEND);

        //add blend function for alpha
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        //load our textures
        loadTextures(gl);
    }

    public int loadTexture(Bitmap bitmap, GL10 gl, int[] textures, int index) {

        try {
            //our container to generate the textures
            gl.glGenTextures(1, textures, index);

            //bind the texture id
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[index]);

            if (true) {
                //we want smoother images
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
            } else {
                //not smoother images
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            }

            //allow any size texture
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);

            //allow any size texture
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

            //add bitmap to texture
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            //we no longer need the resource
            bitmap.recycle();

            if (textures[index] == 0) {
                throw new Exception("Error loading texture: " + index);
            } else {
                //display texture id
                MainActivity.logEvent("Texture loaded id: " + textures[index]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //return our texture id
        return textures[index];
    }

    private void loadTextures(GL10 gl) {

        //load 15 textures into our array
        this.textures = new int[15];

        //get the sprite sheet containing all our animations
        Bitmap spriteSheet = BitmapFactory.decodeResource(activity.getResources(), R.drawable.blocks);

        //load every texture that we need
        for (int i = 0; i < textures.length - 1; i++) {

            //retrieve the current bitmap
            Bitmap tmp = Bitmap.createBitmap(spriteSheet, i * ANIMATION_DIMENSIONS, 0, ANIMATION_DIMENSIONS, ANIMATION_DIMENSIONS);

            //load the individual texture
            loadTexture(tmp, gl, textures, i);
        }

        //load the texture
        loadTexture(BitmapFactory.decodeResource(activity.getResources(), R.drawable.border), gl, textures, textures.length - 1);
    }
}