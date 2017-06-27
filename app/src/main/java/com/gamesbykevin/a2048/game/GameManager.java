package com.gamesbykevin.a2048.game;

/**
 * Created by Kevin on 5/26/2017.
 */

import android.content.Context;
import android.view.MotionEvent;

import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.board.Board;
import com.gamesbykevin.a2048.board.BoardHelper;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.board.Block.BLOCK_DIMENSIONS;
import static com.gamesbykevin.a2048.board.Block.START_X;
import static com.gamesbykevin.a2048.opengl.OpenGLRenderer.glText;

/**
 * The Game Mananger class will keep all of our game object(s) logic
 */
public class GameManager {

    //the game board
    private Board board;

    //store our activity reference
    private final Context activity;

    //keep track of the pressed down coordinates
    private float downX, downY;

    //was down pressed
    private boolean pressed = false;

    /**
     * Different ways we can merge
     */
    public enum Merge {
        North, South, East, West
    }

    //which way are we merging
    private Merge merge = null;

    //do we want to try and merge
    private boolean flagMerge = false;

    //the name of our font file
    public static final String FONT_FILE_NAME = "font.ttf";

    //the font size of the text
    public static final int FONT_SIZE = 36;

    /**
     * Default constructor
     */
    public GameManager(final Context activity) {

        //store our activity reference
        this.activity = activity;

        //create a new game board
        this.board = new Board(5, 5);
    }

    public boolean onTouchEvent(final int action, final float x, final float y) {

        //don't continue if we are in the process of merging
        if (flagMerge)
            return true;

        //don't continue if we are already merging
        if (merge != null)
            return true;

        //if the game is over we can't continue
        if (getBoard().isGameover())
            return true;

        //if blocks are currently expanding/collapsing we can't do anything yet
        //if (!getBoard().hasCompletedChange())
        //    return true;

        //determine the appropriate action
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:

                //if we have not yet pressed down on the screen
                if (!pressed) {

                    //flag pressed as true
                    pressed = true;

                    //store our coordinates
                    this.downX = x;
                    this.downY = y;
                }

                break;

            case MotionEvent.ACTION_UP:

                //make sure we pressed down first
                if (pressed) {

                    //flag pressed as false
                    pressed = false;

                    //calculate the x-coordinate difference
                    float diffX = (x > this.downX) ? x - this.downX : this.downX - x;

                    //calculate the y-coordinate difference
                    float diffY = (y > this.downY) ? y - this.downY : this.downY - y;

                    //if we didn't swipe long enough, don't continue
                    if (diffX < BLOCK_DIMENSIONS / 2 && diffY < BLOCK_DIMENSIONS / 2)
                        return true;

                    //determine which way we are swiping
                    if (diffX > diffY) {

                        //if the x difference is greater than y then we are swiping horizontal
                        if (x > this.downX) {

                            //we are swiping right
                            MainActivity.logEvent("Swiping Right");

                            //merging east
                            this.merge = Merge.East;

                        } else {

                            //we are swiping left
                            MainActivity.logEvent("Swiping Left");

                            //merging west
                            this.merge = Merge.West;

                        }
                    } else {

                        //if the y difference is greater than x then we are swiping vertical
                        if (y > this.downY) {

                            //we are swiping down
                            MainActivity.logEvent("Swiping Down");

                            //merging south
                            this.merge = Merge.South;

                        } else {

                            //we are swiping up
                            MainActivity.logEvent("Swiping Up");

                            //merging north
                            this.merge = Merge.North;
                        }
                    }

                    //flag that we want to merge next game update
                    flagMerge = true;
                }

                break;
        }

        return true;
    }

    /**
     * Update game objects
     */
    public void update() throws Exception {

        //do we need to check for a merge
        if (flagMerge) {

            //update the blocks accordingly on where we want to head
            BoardHelper.merge(getBoard(), this.merge);

            //after merging, if all blocks are already at the target nothing will happen
            if (getBoard().hasTarget())
                this.merge = null;

            //flag false now that we are complete
            flagMerge = false;

        } else {

            //if the blocks are at their target, we can merge again and spawn a new block
            if (this.merge != null && getBoard().hasTarget()) {

                //we can merge again
                this.merge = null;

                //spawn a new block
                getBoard().spawn();

                //if the game is over vibrate the phone
                if (getBoard().isGameover()) {
                    //activity.vibrate();
                }
            }

            //update the state of the blocks on the board
            getBoard().update();
        }
    }

    /**
     *
     * @return
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Render the game objects
     * @param gl Surface used for rendering pixels
     */
    public void draw(GL10 gl, final int[] textures) {

        try {

            //assign the appropriate textures
            getBoard().assignTextures(textures);

            //draw everything on the board
            getBoard().draw(gl);

            //draw our text on-screen
            drawText(gl);

        } catch (Exception e) {
            MainActivity.handleException(e);
        }
    }

    /**
     * Render any text on screen using custom font
     * @param gl Object used to render image
     */
    public void drawText(GL10 gl) {

        // Set to ModelView mode
        gl.glMatrixMode( GL10.GL_MODELVIEW );           // Activate Model View Matrix
        gl.glLoadIdentity();                            // Load Identity Matrix

        // enable texture + alpha blending
        // NOTE: this is required for text rendering! we could incorporate it into
        // the GLText class, but then it would be called multiple times (which impacts performance).
        gl.glEnable( GL10.GL_TEXTURE_2D );              // Enable Texture Mapping
        gl.glEnable( GL10.GL_BLEND );                   // Enable Alpha Blend
        gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );  // Set Alpha Blend Function

        //TEST: render the entire font texture
        //gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);     // Set Color to Use
        //glText.drawTexture(100, 100);            // Draw the Entire Texture

        // TEST: render some strings with the font
        glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         // (r, g, b) alpha
        glText.draw( "Score:" + getBoard().getScore(), START_X, 25);
        glText.end();                                   // End Text Rendering

        if (getBoard().isGameover()) {
            glText.begin( 1.0f, 1.0f, 1.0f, 1.0f );         // (r, g, b) alpha
            glText.draw("Game Over", START_X, 25 + glText.getCharHeight());
            glText.end();
        }

        // disable texture + alpha
        gl.glDisable( GL10.GL_BLEND );                  // Disable Alpha Blend
        gl.glDisable( GL10.GL_TEXTURE_2D );             // Disable Texture Mapping
    }
}