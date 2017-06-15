package com.gamesbykevin.a2048.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.gamesbykevin.a2048.GameView;
import com.gamesbykevin.a2048.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kevin on 6/1/2017.
 */

public class OpenGLRenderer implements Renderer {

    private Context mContext;

    private int[] textures;

    public OpenGLRenderer(Context context) {
        this.mContext = context;
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
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        //create int array with the number of textures we want
        textures = new int[1];

        // Tell OpenGL to generate textures.
        gl.glGenTextures(1, textures, 0);

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fb);

        //bind the bitmap to the texture id
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        //Delete the textures.
        //gl.glDeleteTextures(1, textures, 0);

        //Set the background color to black ( rgba ).
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        FloatBuffer byteBuf = ByteBuffer.allocateDirect(textures.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
        textureBuffer.put(textureCoordinates);
        textureBuffer.position(0);

        /*
        //Enable Smooth Shading, default not really needed.
        gl.glShadeModel(GL10.GL_SMOOTH);

        //Depth buffer setup.
        gl.glClearDepthf(1.0f);

        //Enables depth testing.
        gl.glEnable(GL10.GL_DEPTH_TEST);

        //The type of depth testing to do.
        gl.glDepthFunc(GL10.GL_LEQUAL);

        //Really nice perspective calculations.
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        */
    }

    /**
     * Called for each redraw of the view
     * @param gl
     */
    public void onDrawFrame(GL10 gl) {

        // Clears the screen and depth buffer.
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Telling OpenGL to enable textures.
        gl.glEnable(GL10.GL_TEXTURE_2D);

        // Tell OpenGL where our texture is located.
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

        // Tell OpenGL to enable the use of UV coordinates.
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        // Telling OpenGL where our UV coordinates are.
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
    }

    /**
     *  Called if the geometry of the view changes.<br>
     *  For example when the device's screen orientation changes
     * @param gl
     * @param width
     * @param height
     */
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // Sets the current view port to the new size.
        gl.glViewport(0, 0, width, height);

        // Select the projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);

        // Reset the projection matrix
        gl.glLoadIdentity();

        // Calculate the aspect ratio of the window
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);

        // Select the modelview matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);

        // Reset the modelview matrix
        gl.glLoadIdentity();



        //portion of the image to render, this is the whole bitmap
        float textureCoordinates[] = {0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f };
    }
}