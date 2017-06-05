package com.gamesbykevin.a2048.opengl;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Created by Kevin on 6/4/2017.
 */

public class MySprite {

    public int x, y;
    private int w, h;

    int textureId = -1;

    private MySprite(Bitmap bmp, int textureId) {
        this.w = bmp.getWidth();
        this.h = bmp.getHeight();
        this.textureId = textureId;
    }

    public static MySprite createGLSprite(Bitmap bmp) {
        if (bmp == null)
            return null;

        MySprite ms = new MySprite(bmp, createGlTexture());
        Log.d("G1", "image id = " + ms.textureId);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        bmp.recycle();
        return ms;
    }

    private static int createGlTexture() {
        // Generate Textures, if more needed, alter these numbers.
        final int[] textureHandles = new int[1];
        GLES20.glGenTextures(1, textureHandles, 0);

        if (textureHandles[0] != 0) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[0]);
            return textureHandles[0];
        } else {
            throw new RuntimeException("Error loading texture.");
        }
    }
}
