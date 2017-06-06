package com.gamesbykevin.a2048.opengl;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.gamesbykevin.a2048.GameView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import static com.gamesbykevin.a2048.opengl.GLRenderer.uvBuffer;
import static com.gamesbykevin.a2048.opengl.ShaderHelper.fs_Image;
import static com.gamesbykevin.a2048.opengl.ShaderHelper.fs_SolidColor;
import static com.gamesbykevin.a2048.opengl.ShaderHelper.loadShader;
import static com.gamesbykevin.a2048.opengl.ShaderHelper.sp_Image;
import static com.gamesbykevin.a2048.opengl.ShaderHelper.sp_SolidColor;
import static com.gamesbykevin.a2048.opengl.ShaderHelper.vs_Image;
import static com.gamesbykevin.a2048.opengl.ShaderHelper.vs_SolidColor;

/**
 * Created by Kevin on 6/3/2017.
 */

public class Sprite {

    // Geometric variables
    public static float vertices[];
    public static short indices[];
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;

    //each texture id is unique
    private final int textureId;

    public Sprite(Bitmap bitmap) {

        //set the triangle coordinates
        setupTriangle();

        //Generate Textures, if more needed, alter these numbers.
        int[] textureNames = new int[1];
        GLES20.glGenTextures(1, textureNames, 0);

        //bind the texture in open gl
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        //load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        //since the bitmap has been loaded into open gl recycle it.
        bitmap.recycle();

        //throw exception if the texture was not loaded successfully
        if (textureNames[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }

        //store the textureId reference accordingly
        this.textureId = textureNames[0];
    }

    public void setupTriangle() {

        int x, y;

        switch (new Random().nextInt(4)) {
            case 0:
                x = 0;
                y = GameView.HEIGHT;
                break;

            case 1:
                x = GameView.WIDTH - 100;
                y = GameView.HEIGHT;
                break;

            case 2:
                x = 0;
                y = 100;
                break;

            case 3:
            default:
                x = GameView.WIDTH - 100;
                y = 100;
                break;
        }


        //this will flip the image
        if (new Random().nextBoolean()) {

            //display image normal
            vertices = new float[]{
                x, y, 0f,
                x, y - 100, 0f,
                x + 100, y - 100, 0f,
                x + 100, y, 0f
            };

        } else {

            //display image upside down, facing the opposite direction
            vertices = new float[]{
                x + 100, y - 100, 0f,
                x + 100, y, 0f,
                x, y, 0f,
                x, y - 100, 0f
            };

        }

        //order of vertex rendering
        if (indices == null)
            indices = new short[] {0, 1, 2, 0, 2, 3};

        //the vertex buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);
    }

    public void draw(float[] m) {

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(sp_Image, "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(sp_Image, "a_texCoord");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);

        // Prepare the texture coordinates
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);

        //get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(sp_Image, "uMVPMatrix");

        //apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        //get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation(sp_Image, "s_texture" );

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        //set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i(mSamplerLoc, 0);

        //bind the correct texture before we draw it
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.textureId);

        //Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }
}