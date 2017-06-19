package com.gamesbykevin.a2048.opengl;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLUtils;

import com.gamesbykevin.a2048.MainActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kevin on 6/18/2017.
 */

public class Sprite {

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private int texture[] = new int[1];

    private float[] vertices = {
        0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 1.0f, 0.0f
    };

    private float[] textures = {
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 0.0f,
        1.0f, 1.0f
    };

    public Sprite(Bitmap bitmap, GL10 gl) {

        //our container to generate the textures
        gl.glGenTextures(1, texture, 0);

        MainActivity.logEvent("Texture id: " + texture[0]);

        //bind the texture id
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);

        if (true) {
            //we want smoother images
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        } else {
            //not smoother images
            //gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            //gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        }

        //allow any size texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);

        //allow any size texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        //create our vertices buffer
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        //create our texture buffer
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        textureBuffer = tbb.asFloatBuffer();
        textureBuffer.put(textures);
        textureBuffer.position(0);

        //we no longer need the resource
        bitmap.recycle();
    }

    public void draw(GL10 gl, float x, float y, float w, float h) {

        //assign render coordinates
        gl.glTranslatef(x, y, 0.0f);

        //assign dimensions
        gl.glScalef(w, h, 0.0f);

        //assign texture we want to use
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);

        //enable client state for our render
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        //provide our array of vertex coordinates
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        //coordinates on texture we want to render
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        //render our texture based on the texture and vertex coordinates
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

        //now that we are done, undo client state to prevent any render issues
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
}