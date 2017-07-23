package com.gamesbykevin.a2048.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;

import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.board.Block;
import com.gamesbykevin.a2048.util.StatDescription;
import com.gamesbykevin.a2048.util.UtilityHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.activity.GameActivity.MANAGER;
import static com.gamesbykevin.a2048.board.Block.ANIMATION_DIMENSIONS;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_BACKGROUND_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_DIFFICULTY_EASY_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_DIFFICULTY_HARD_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_DIFFICULTY_MEDIUM_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_MODE_CHALLENGE_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_MODE_INFINITE_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_MODE_ORIGINAL_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_MODE_PUZZLE_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_RULES_CHALLENGE_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_RULES_INFINITE_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_RULES_ORIGINAL_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_RULES_PUZZLE_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_WORD_BEST_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_WORD_GAMEOVER_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_WORD_LEVEL_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_WORD_SCORE_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_WORD_TIME_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TEXTURE_WORD_WIN_INDEX;
import static com.gamesbykevin.a2048.game.GameManagerHelper.TOTAL_WORD_TEXTURES;
import static com.gamesbykevin.a2048.opengl.OpenGLSurfaceView.HEIGHT;
import static com.gamesbykevin.a2048.opengl.OpenGLSurfaceView.WIDTH;
import static com.gamesbykevin.a2048.util.StatDescription.TOTAL_CHARACTERS;

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
    public static int[] TEXTURES;

    /**
     * Have all textures been loaded?
     */
    public static boolean LOADED = false;

    public OpenGLRenderer(Context activity) {

        //store context reference
        this.activity = activity;

        //flag the textures loaded as false
        LOADED = false;
    }

    public void onPause() {
        //do we do anything here?
    }

    public void onResume() {
        //re-load the textures if needed?
    }

    /**
     * Called once to set up the view's OpenGL ES environment
     * @param gl Open gl object for rendering
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //display the version of open gl
        //UtilityHelper.logEvent("OpenGL Version: " + gl.glGetString(GL10.GL_VERSION));
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

        //flag that we have not yet loaded the textures
        LOADED = false;

        //store the ratio for the render
        this.scaleRenderX = width / (float) WIDTH;
        this.scaleRenderY = height / (float) HEIGHT;

        //store the ratio when touching the screen
        this.scaleMotionX = (float) WIDTH / width;
        this.scaleMotionY = (float) HEIGHT / height;

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

        //enable smooth shading
        gl.glEnableClientState(GL10.GL_SMOOTH);

        //load our textures
        loadTextures(gl);

        //flag that we have loaded the textures
        LOADED = true;
    }

    public int loadTexture(Bitmap bitmap, GL10 gl, int[] textures, int index) {

        try {

            //our container to generate the textures
            gl.glGenTextures(1, textures, index);

            //bind the texture id
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[index]);

            if (false) {
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
                //UtilityHelper.logEvent("Texture loaded id: " + textures[index]);
            }

        } catch (Exception e) {
            UtilityHelper.handleException(e);
        }

        //return our texture id
        return textures[index];
    }

    private void loadTextures(GL10 gl) {

        //load textures into our array
        TEXTURES = new int[Block.VALUES.length + TOTAL_CHARACTERS + TOTAL_WORD_TEXTURES];

        //get the sprite sheet containing all our animations
        Bitmap spriteSheet = BitmapFactory.decodeResource(activity.getResources(), R.drawable.blocks);

        //temp bitmap image reference
        Bitmap tmp = null;

        //load every texture that we need
        for (int i = 0; i < Block.VALUES.length; i++) {

            //retrieve the current bitmap
            tmp = Bitmap.createBitmap(spriteSheet, i * ANIMATION_DIMENSIONS, 0, ANIMATION_DIMENSIONS, ANIMATION_DIMENSIONS);

            //load the individual texture
            loadTexture(tmp, gl, TEXTURES, i);
        }

        Bitmap numberSheet = BitmapFactory.decodeResource(activity.getResources(), R.drawable.numbers);

        for (int i = 0; i < TOTAL_CHARACTERS; i++) {
            tmp = Bitmap.createBitmap(
                numberSheet,
                i * StatDescription.ANIMATION_WIDTH,
                0,
                StatDescription.ANIMATION_WIDTH,
                StatDescription.ANIMATION_HEIGHT
            );

            //load the individual texture
            loadTexture(tmp, gl, TEXTURES, i + Block.VALUES.length);
        }

        //load the word textures
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.best);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_WORD_BEST_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.level);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_WORD_LEVEL_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.score);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_WORD_SCORE_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.game_over);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_WORD_GAMEOVER_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.time);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_WORD_TIME_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.background);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_BACKGROUND_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.win);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_WORD_WIN_INDEX);


        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.mode_original);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_MODE_ORIGINAL_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.rules_original);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_RULES_ORIGINAL_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.mode_puzzle);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_MODE_PUZZLE_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.rules_puzzle);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_RULES_PUZZLE_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.mode_challenge);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_MODE_CHALLENGE_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.rules_challenge);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_RULES_CHALLENGE_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.mode_infinite);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_MODE_INFINITE_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.rules_infinite);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_RULES_INFINITE_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.difficulty_easy);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_DIFFICULTY_EASY_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.difficulty_medium);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_DIFFICULTY_MEDIUM_INDEX);
        tmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.difficulty_hard);
        loadTexture(tmp, gl, TEXTURES, TEXTURE_DIFFICULTY_HARD_INDEX);
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
        MANAGER.draw(gl, TEXTURES);
    }
}