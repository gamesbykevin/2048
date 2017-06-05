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
    public static float uvs[];
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    public FloatBuffer uvBuffer;

    public Sprite(Bitmap bitmap) {

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

        // Set our shader programm
        GLES20.glUseProgram(sp_Image);

        //set the triangle coordinates
        setupTriangle();

        //set the texture data
        setupImage(bitmap);
    }

    public void setupImage(final Bitmap bitmap) {

        // Create our UV coordinates.
        uvs = new float[] {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
        };

        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);

        //Generate Textures, if more needed, alter these numbers.
        int[] textureNames = new int[1];
        GLES20.glGenTextures(1, textureNames, 0);

        //bind the texture to the texture name
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // We are done using the bitmap so we should recycle it.
        bitmap.recycle();
    }

    /**
     * Our square image will consist of 2 triangles
     */
    public void setupTriangle() {

        switch (new Random().nextInt(4)) {
            case 0:
                // We have to create the vertices of our triangle.
                vertices = new float[]{
                        0f, GameView.HEIGHT, 0f,
                        0f, GameView.HEIGHT - 100, 0f,
                        100f, GameView.HEIGHT - 100, 0f,
                        100f, GameView.HEIGHT, 0f
                };
                break;

            case 1:
                vertices = new float[]{
                        GameView.WIDTH - 100, GameView.HEIGHT, 0f,
                        GameView.WIDTH - 100, GameView.HEIGHT - 100, 0f,
                        GameView.WIDTH, GameView.HEIGHT - 100, 0f,
                        GameView.WIDTH, GameView.HEIGHT, 0f
                };
                break;

            case 2:
                // We have to create the vertices of our triangle.
                vertices = new float[]{
                        0f, 100, 0f,
                        0f, 0, 0f,
                        100f, 0, 0f,
                        100f, 100, 0f
                };
                break;

            case 3:
            default:
                vertices = new float[]{
                        GameView.WIDTH - 100, 100, 0f,
                        GameView.WIDTH - 100, 0, 0f,
                        GameView.WIDTH, 0, 0f,
                        GameView.WIDTH, 100, 0f
                };
                break;
        }

                /*
            10.0f, 200f, 0.0f, // north west corner
            10.0f, 100f, 0.0f, // south west corner
            210f, 100f, 0.0f, // south east corner
            210f, 200f, 0.0f, // north east corner
            */

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

        // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(sp_Image, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation(sp_Image, "s_texture" );

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i(mSamplerLoc, 0);

        //Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }
}