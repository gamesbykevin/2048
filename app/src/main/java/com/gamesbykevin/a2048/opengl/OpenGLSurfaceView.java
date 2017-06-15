package com.gamesbykevin.a2048.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by Kevin on 6/1/2017.
 */

public class OpenGLSurfaceView extends GLSurfaceView {

    /**
     * The version of open GL we are using
     */
    public static final int OPEN_GL_VERSION = 1;

    /**
     * Our object where we render our pixel data
     */
    private final OpenGLRenderer openGlRenderer;

    public OpenGLSurfaceView(Context context) {

        //call overloaded constructor
        this(context, null);
    }

    public OpenGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Create an OpenGL ES 1.0 context.
        setEGLContextClientVersion(OPEN_GL_VERSION);

        //create a new instance of our renderer
        this.openGlRenderer = new OpenGLRenderer(context);

        //set the renderer for drawing on the gl surface view
        setRenderer(this.openGlRenderer);

        //set render mode to only draw when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onPause() {

        //call parent function
        super.onPause();

        //also pause our render
        openGlRenderer.onPause();
    }

    @Override
    public void onResume() {

        //call parent function
        super.onResume();

        //also resume our render
        openGlRenderer.onResume();
    }
}