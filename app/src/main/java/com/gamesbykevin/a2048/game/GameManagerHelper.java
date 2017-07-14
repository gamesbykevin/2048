package com.gamesbykevin.a2048.game;

import com.gamesbykevin.a2048.board.Block;
import com.gamesbykevin.a2048.board.Board;
import com.gamesbykevin.a2048.board.BoardHelper;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.GameActivity.MANAGER;
import static com.gamesbykevin.a2048.GameActivity.STATS;
import static com.gamesbykevin.a2048.board.Block.START_X;
import static com.gamesbykevin.a2048.board.Block.VALUES;
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
        Challenge,
        Infinite
    }

    /**
     * How do we want to display the time
     */
    private static final String TIME_FORMAT = "mm:ss:SSS";

    /**
     * Object to format the time
     */
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(TIME_FORMAT);

    /**
     * How much time do we have remaining in challenge mode
     */
    public static final long CHALLENGE_DURATION = 60000;

    /**
     * Value of a single block that indicates game over for original mode
     */
    public static final int ORIGINAL_MODE_GOAL_VALUE = VALUES[11];

    public static void updateDisplayStats() {

        //load the best level stats
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(STATS.getLevel().getDuration());

        TIME_DESC = DATE_FORMAT.format(cal.getTime());
        LEVEL_DESC = STATS.getLevel().getTitle();
        SCORE = STATS.getLevel().getScore();
    }

    /**
     * Is the game over?
     * @param board The board so we can check if the game is over
     * @param mode The game mode so we know how to check if the game is over
     * @return true if the game is over per rules of the specified mode, false otherwise
     */
    public static boolean isGameOver(Board board, Mode mode) {

        switch (mode) {

            case Puzzle:

                //if there is one block left, the game is over
                return (board.getBlocks().size() == 1);

            case Challenge:

                //if time is expired, the game is over
                if (CHALLENGE_DURATION - board.getDuration() <= 0) {

                    //set time to 00:00:00
                    board.setDuration(CHALLENGE_DURATION);

                    //yes, the game is over
                    return true;

                } else {

                    //if time hasn't expired, the game is not over
                    return false;
                }

            case Infinite:

                //continue until no more moves are left
                return (!board.hasMove());

            /**
             * Original the game continues until there are no more moves available
             */
            case Original:

                //if we don't have any more moves the game is over
                if (!board.hasMove())
                    return true;

                //if one block has 2048 the game is over
                if (board.hasValue(ORIGINAL_MODE_GOAL_VALUE))
                    return true;

                //the game isn't over
                return false;

            default:
                throw new RuntimeException("Mode: " + mode.toString() + " does not have game over logic implemented.");
        }
    }


    /**
     * Render any text on screen using custom font
     * @param openGL Object used to render image
     */
    public static void drawText(GL10 openGL, long duration) {

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

        //starting y-coordinate
        int y = 25 + (int)glText.getCharHeight();

        if (MANAGER.GAME_OVER)
            glText.draw(TEXT_GAME_OVER, START_X, y);

        switch (MODE) {

            case Puzzle:
                y += glText.getCharHeight();
                glText.draw("Time: " + DATE_FORMAT.format(duration), START_X, y);
                y += glText.getCharHeight();
                glText.draw("Best: " + TIME_DESC, START_X, y);
                y += glText.getCharHeight();
                glText.draw("Level " + LEVEL_DESC, START_X, y);
                break;

            case Original:
                y += glText.getCharHeight();
                glText.draw("Time: " + DATE_FORMAT.format(duration), START_X, y);
                y += glText.getCharHeight();
                glText.draw("Best: " + TIME_DESC, START_X, y);
                break;

            case Challenge:
                y += glText.getCharHeight();

                if (duration > CHALLENGE_DURATION) {
                    glText.draw("Time: " + DATE_FORMAT.format(CHALLENGE_DURATION - CHALLENGE_DURATION), START_X, y);
                } else {
                    glText.draw("Time: " + DATE_FORMAT.format(CHALLENGE_DURATION - duration), START_X, y);
                }
                y += glText.getCharHeight();
                glText.draw(TEXT_SCORE + MANAGER.getBoard().getScore(), START_X, y);
                y += glText.getCharHeight();
                glText.draw("High Score: " + SCORE, START_X, y);
                break;

            case Infinite:
                y += glText.getCharHeight();
                glText.draw(TEXT_SCORE + MANAGER.getBoard().getScore(), START_X, y);
                y += glText.getCharHeight();
                glText.draw("High Score: " + SCORE, START_X, y);
                break;
        }

        //end text rendering
        glText.end();

        // disable texture + alpha
        openGL.glDisable(GL10.GL_BLEND);
        openGL.glDisable(GL10.GL_TEXTURE_2D);
    }
}