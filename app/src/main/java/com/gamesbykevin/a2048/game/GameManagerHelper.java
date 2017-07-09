package com.gamesbykevin.a2048.game;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.GameActivity.MANAGER;
import static com.gamesbykevin.a2048.GameActivity.STATS;
import static com.gamesbykevin.a2048.board.Block.START_X;
import static com.gamesbykevin.a2048.level.Stats.MODE;
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

    //text where we render our score
    public static final String TEXT_SCORE = "Score:";

    //game over message
    public static final String TEXT_GAME_OVER = "Game Over";

    //the description of the current level
    public static String LEVEL_DESC = "";

    //best score
    public static int SCORE = 0;

    //best time
    public static String TIME_DESC = "";

    public enum Difficulty {
        Easy,
        Medium,
        Hard
    }

    public enum Mode {
        Original,
        Puzzle,
        Challenge
    }

    public static void updateDisplayStats() {

        //load the best level stats
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(STATS.getLevel().getDuration());

        TIME_DESC = new SimpleDateFormat("mm:ss:SSS").format(cal.getTime());
        LEVEL_DESC = STATS.getLevel().getTitle();
        SCORE = STATS.getLevel().getScore();
    }

    /**
     * Render any text on screen using custom font
     * @param openGL Object used to render image
     */
    public static void drawText(GL10 openGL) {

        //Set to ModelView mode
        openGL.glMatrixMode(GL10.GL_MODELVIEW);
        openGL.glLoadIdentity();

        //enable texture, alpha, and alpha blending
        openGL.glEnable(GL10.GL_TEXTURE_2D);
        openGL.glEnable(GL10.GL_BLEND);
        openGL.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        //TEST: render the entire font texture
        //gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);     // Set Color to Use

        //get ready to render text in the specified color (r, g, b) alpha
        glText.begin(1.0f, 1.0f, 1.0f, 1.0f);

        //draw the entire font as a texture of the specified size
        //glText.drawTexture(100, 100);

        if (MANAGER.GAME_OVER)
            glText.draw(TEXT_GAME_OVER, START_X, 25 + glText.getCharHeight());

        if (MODE == Mode.Puzzle) {

            glText.draw("Best: " + TIME_DESC, START_X, 25 + (glText.getCharHeight() * 2));
            glText.draw("Level " + LEVEL_DESC, START_X, 25 + (glText.getCharHeight() * 3));

        } else {
            glText.draw(TEXT_SCORE + MANAGER.getBoard().getScore(), START_X, 25);
            glText.draw("High Score: " + SCORE, START_X, 25 + (glText.getCharHeight() * 2));
        }

        //end text rendering
        glText.end();

        // disable texture + alpha
        openGL.glDisable(GL10.GL_BLEND);
        openGL.glDisable(GL10.GL_TEXTURE_2D);
    }
}