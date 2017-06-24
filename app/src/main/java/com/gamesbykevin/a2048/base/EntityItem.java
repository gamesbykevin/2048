package com.gamesbykevin.a2048.base;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.opengl.GLUtils;

import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.androidframework.base.Entity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kevin on 6/19/2017.
 */

public class EntityItem extends com.gamesbykevin.androidframework.base.Entity {

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;

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

    private int textureId;

    public EntityItem() {
        super();

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
    }

    public void setTextureId(final int textureId) {
        this.textureId = textureId;
    }

    public int getTextureId() {
        return this.textureId;
    }

    public void render(GL10 gl) {

        float x, y, w, h;

        x = (float)getX();
        y = (float)getY();
        w = (float)getWidth();
        h = (float)getHeight();

        //use for quick transformations so it will only apply to this object
        gl.glPushMatrix();

        //assign render coordinates
        gl.glTranslatef(x, y, 0.0f);

        //assign dimensions
        gl.glScalef(w, h, 0.0f);

        //enable texture rendering
        gl.glEnable(GL10.GL_TEXTURE_2D);

        //assign texture we want to use
        gl.glBindTexture(GL10.GL_TEXTURE_2D, getTextureId());

        //enable client state for our render
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        //provide our array of vertex coordinates
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        //coordinates on texture we want to render
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        //render our texture based on the texture and vertex coordinates
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

        //after rendering remove the transformation since we only needed it for this object
        gl.glPopMatrix();

        //now that we are done, undo client state to prevent any render issues
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
}