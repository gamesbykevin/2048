package com.gamesbykevin.a2048.opengl;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

import com.gamesbykevin.a2048.GameView;
import com.gamesbykevin.a2048.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.opengl.ShaderHelper.fs_Image;
import static com.gamesbykevin.a2048.opengl.ShaderHelper.fs_SolidColor;
import static com.gamesbykevin.a2048.opengl.ShaderHelper.loadShader;
import static com.gamesbykevin.a2048.opengl.ShaderHelper.sp_Image;
import static com.gamesbykevin.a2048.opengl.ShaderHelper.sp_SolidColor;
import static com.gamesbykevin.a2048.opengl.ShaderHelper.vs_Image;
import static com.gamesbykevin.a2048.opengl.ShaderHelper.vs_SolidColor;


/**
 * Created by Kevin on 6/1/2017.
 */

public class GLRenderer implements Renderer {

    private ArrayList<Sprite> textures = new ArrayList<>();

    private Context mContext;

    //our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    public static float uvs[];
    public static FloatBuffer uvBuffer;

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
        Sprite sprite1 = new Sprite(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fb));
        Sprite sprite2 = new Sprite(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.yt));

        this.textures.add(sprite1);
        this.textures.add(sprite2);

        // Create our UV coordinates.
        uvs = new float[] {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        //the texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);

        //create and assign our shaders etc...
        setupShader();
    }

    private void setupShader() {

        // Create the shaders, solid color
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vs_SolidColor);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fs_SolidColor);

        sp_SolidColor = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(sp_SolidColor, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(sp_SolidColor, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(sp_SolidColor);                  // creates OpenGL ES program executables

        // Create the shaders, images
        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vs_Image);
        fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fs_Image);

        sp_Image = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(sp_Image, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(sp_Image, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(sp_Image);                  // creates OpenGL ES program executables

        //set our shader program
        GLES20.glUseProgram(sp_Image);
    }

    /**
     * Called for each redraw of the view
     * @param unused
     */
    public void onDrawFrame(GL10 unused) {

        //Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //draw the square
        for (Sprite sprite : textures) {
            sprite.draw(mtrxProjectionAndView);
        }
    }

    /**
     *  Called if the geometry of the view changes.<br>
     *  For example when the device's screen orientation changes
     * @param unused
     * @param width
     * @param height
     */
    public void onSurfaceChanged(GL10 unused, int width, int height) {

        //clear our matrices
        for(int i = 0; i < mtrxProjectionAndView.length; i++)
        {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        //Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0f, GameView.WIDTH, 0.0f, GameView.HEIGHT, 0, 50);

        //Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        //Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
    }
}