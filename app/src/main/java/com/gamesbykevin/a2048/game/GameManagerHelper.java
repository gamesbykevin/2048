package com.gamesbykevin.a2048.game;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.GameActivity.MANAGER;
import static com.gamesbykevin.a2048.board.Block.START_X;
import static com.gamesbykevin.a2048.opengl.OpenGLRenderer.glText;
import static com.gamesbykevin.a2048.opengl.OpenGLSurfaceView.FPS;

/**
 * Created by Kevin on 7/3/2017.
 */

public class GameManagerHelper {

    /**
     * The duration we wait until we show the game over screen
     */
    protected static final int GAME_OVER_FRAMES_DELAY = (FPS * 2);

    /**
     * Do we want to reset the game?
     */
    public static boolean RESET = false;

    /**
     * Default size of board
     */
    public static final int DEFAULT_DIMENSIONS = 4;

    /**
     * Size of the board while playing easy
     */
    public static final int PUZZLE_DIMENSIONS_EASY = 4;

    /**
     * Size of the board while playing medium
     */
    public static final int PUZZLE_DIMENSIONS_MEDIUM = 5;

    /**
     * Size of the board while playing hard
     */
    public static final int PUZZLE_DIMENSIONS_HARD = 6;

    /**
     * Size of the board while playing easy
     */
    public static final int DIMENSIONS_EASY = 6;

    /**
     * Size of the board while playing medium
     */
    public static final int DIMENSIONS_MEDIUM = 5;

    /**
     * Size of the board while playing hard
     */
    public static final int DIMENSIONS_HARD = 4;

    /**
     * Render any text on screen using custom font
     * @param openGL Object used to render image
     */
    public static void drawText(GL10 openGL) {

        // Set to ModelView mode
        openGL.glMatrixMode(GL10.GL_MODELVIEW);           // Activate Model View Matrix
        openGL.glLoadIdentity();                            // Load Identity Matrix

        // enable texture + alpha blending
        // NOTE: this is required for text rendering! we could incorporate it into
        // the GLText class, but then it would be called multiple times (which impacts performance).
        openGL.glEnable(GL10.GL_TEXTURE_2D);              // Enable Texture Mapping
        openGL.glEnable(GL10.GL_BLEND);                   // Enable Alpha Blend
        openGL.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);  // Set Alpha Blend Function

        //TEST: render the entire font texture
        //gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);     // Set Color to Use
        //glText.drawTexture(100, 100);            // Draw the Entire Texture

        // TEST: render some strings with the font
        glText.begin(1.0f, 1.0f, 1.0f, 1.0f);         // (r, g, b) alpha
        glText.draw("Score:" + MANAGER.getBoard().getScore(), START_X, 25);
        glText.end();                                   // End Text Rendering

        if (MANAGER.GAME_OVER) {
            glText.begin(1.0f, 1.0f, 1.0f, 1.0f);         // (r, g, b) alpha
            glText.draw("Game Over", START_X, 25 + glText.getCharHeight());
            glText.end();
        }

        // disable texture + alpha
        openGL.glDisable(GL10.GL_BLEND);                  // Disable Alpha Blend
        openGL.glDisable(GL10.GL_TEXTURE_2D);             // Disable Texture Mapping
    }
}