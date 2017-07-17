package com.gamesbykevin.a2048.game;

import android.text.TextUtils;

import com.gamesbykevin.a2048.activity.MainActivity;
import com.gamesbykevin.a2048.base.EntityItem;
import com.gamesbykevin.a2048.board.Board;
import com.gamesbykevin.a2048.util.StatDescription;
import com.gamesbykevin.a2048.util.UtilityHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.activity.GameActivity.MANAGER;
import static com.gamesbykevin.a2048.activity.GameActivity.STATS;
import static com.gamesbykevin.a2048.board.Block.VALUES;
import static com.gamesbykevin.a2048.level.Stats.MODE;
import static com.gamesbykevin.a2048.opengl.OpenGLRenderer.TEXTURES;
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

    //the description of the current level
    public static String LEVEL_DESC = "";

    public enum Difficulty {
        Easy, Medium, Hard
    }

    public enum Mode {
        Original, Puzzle, Challenge, Infinite
    }

    /**
     * How much time do we have remaining in challenge mode?
     */
    public static final long CHALLENGE_DURATION = 60000;

    /**
     * Value of a single block that indicates game over for original mode
     */
    public static final int ORIGINAL_MODE_GOAL_VALUE = VALUES[11];

    //object used to render number images
    private static StatDescription STAT_RECORD = new StatDescription();
    private static StatDescription STAT_CURRENT = new StatDescription();
    private static StatDescription STAT_TIME = new StatDescription();
    private static StatDescription STAT_LEVEL = new StatDescription();

    //total number of words we will be displaying
    public static final int TOTAL_WORD_TEXTURES = 5;

    //index for each word in the texture array
    public static final int TEXTURE_WORD_BEST_INDEX = 33;
    public static final int TEXTURE_WORD_LEVEL_INDEX = 34;
    public static final int TEXTURE_WORD_SCORE_INDEX = 35;
    public static final int TEXTURE_WORD_GAMEOVER_INDEX = 36;
    public static final int TEXTURE_WORD_TIME_INDEX = 37;

    //how do we resize
    private static final float RATIO = 0.33f;

    //where to render best
    private static final int X_COORD_BEST = 265;
    private static final int Y_COORD_BEST = 20;
    private static final int WIDTH_BEST = (int)(244 * RATIO);
    private static final int HEIGHT_BEST = (int)(101 * RATIO);

    //where to render score
    private static final int X_COORD_SCORE = 20;
    private static final int Y_COORD_SCORE = 20;
    private static final int WIDTH_SCORE = (int)(315 * RATIO);
    private static final int HEIGHT_SCORE = (int)(101 * RATIO);

    //where to render game over
    private static final int X_COORD_GAMEOVER = 40;
    private static final int Y_COORD_GAMEOVER = 625;
    private static final int WIDTH_GAMEOVER = 400;
    private static final int HEIGHT_GAMEOVER = 75;

    //where to render time
    private static final int X_COORD_TIME = 20;
    private static final int Y_COORD_TIME = 20;
    private static final int WIDTH_TIME = (int)(231 * RATIO);
    private static final int HEIGHT_TIME = (int)(101 * RATIO);

    //where to render current stats
    private static final int X_COORD_RESULTS = X_COORD_TIME;
    private static final int Y_COORD_RESULTS = Y_COORD_TIME + HEIGHT_TIME;

    //where to render previous record
    private static final int X_COORD_RECORD = X_COORD_BEST;
    private static final int Y_COORD_RECORD = Y_COORD_BEST + HEIGHT_BEST;

    //where to render level
    private static final int X_COORD_LEVEL = 20;
    private static final int Y_COORD_LEVEL = Y_COORD_RESULTS + (int)(HEIGHT_TIME * 1.3);
    private static final int WIDTH_LEVEL = (int)(297 * RATIO);
    private static final int HEIGHT_LEVEL = (int)(101 * RATIO);

    //object used to render texture words
    private static EntityItem entity = new EntityItem();

    public static void updateDisplayStats() {

        //set coordinates etc... for the text we are rendering based on game mode
        switch (MODE) {

            case Puzzle:
            case Original:

                STAT_CURRENT.setX(X_COORD_RESULTS);
                STAT_CURRENT.setAnchorX(STAT_CURRENT.getX());
                STAT_CURRENT.setY(Y_COORD_RESULTS);
                STAT_CURRENT.setDescription(0, true);

                STAT_RECORD.setX(X_COORD_RECORD);
                STAT_RECORD.setAnchorX(STAT_RECORD.getX());
                STAT_RECORD.setY(Y_COORD_RECORD);
                STAT_RECORD.setDescription(STATS.getLevel().getDuration(), true);

                //if puzzle render level #
                if (MODE == Mode.Puzzle) {
                    STAT_LEVEL.setX(X_COORD_LEVEL + WIDTH_LEVEL + 10);
                    STAT_LEVEL.setAnchorX(STAT_LEVEL.getX());
                    STAT_LEVEL.setY(Y_COORD_LEVEL + 3);
                    STAT_LEVEL.setDescription(STATS.getLevel().getTitle(), false);
                }
                break;

            case Challenge:
            case Infinite:

                STAT_CURRENT.setX(X_COORD_RESULTS);
                STAT_CURRENT.setAnchorX(STAT_CURRENT.getX());
                STAT_CURRENT.setY(Y_COORD_RESULTS);
                STAT_CURRENT.setDescription(MANAGER.getBoard().getScore(), false);

                //render the record as well
                STAT_RECORD.setX(X_COORD_RECORD);
                STAT_RECORD.setAnchorX(STAT_RECORD.getX());
                STAT_RECORD.setY(Y_COORD_RECORD);
                STAT_RECORD.setDescription(STATS.getLevel().getScore(), false);

                //if challenge render the remaining time
                if (MODE == Mode.Challenge) {
                    STAT_TIME.setX(X_COORD_LEVEL + WIDTH_LEVEL + 10);
                    STAT_TIME.setAnchorX(STAT_TIME.getX());
                    STAT_TIME.setY(Y_COORD_LEVEL + 3);
                    STAT_TIME.setDescription(0, true);
                }
                break;

            default:
                throw new RuntimeException("Mode not handled here: " + MODE.toString());
        }
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

        //enable texture, alpha, and alpha blending which supports transparency
        openGL.glEnable(GL10.GL_TEXTURE_2D);
        openGL.glEnable(GL10.GL_BLEND);
        openGL.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        //if game over, notify the user
        if (MANAGER.GAME_OVER)
            entity.render(openGL, X_COORD_GAMEOVER, Y_COORD_GAMEOVER, WIDTH_GAMEOVER, HEIGHT_GAMEOVER, TEXTURES[TEXTURE_WORD_GAMEOVER_INDEX]);

        switch (MODE) {

            case Puzzle:
            case Original:

                //render header text
                entity.render(openGL, X_COORD_TIME, Y_COORD_TIME, WIDTH_TIME, HEIGHT_TIME, TEXTURES[TEXTURE_WORD_TIME_INDEX]);

                //render current status
                STAT_CURRENT.setDescription(duration, true);
                STAT_CURRENT.render(openGL);

                //render best text
                entity.render(openGL, X_COORD_BEST, Y_COORD_BEST, WIDTH_BEST, HEIGHT_BEST, TEXTURES[TEXTURE_WORD_BEST_INDEX]);

                //render the record as well
                STAT_RECORD.render(openGL);

                //if puzzle render level #
                if (MODE == Mode.Puzzle) {

                    //render the header text
                    entity.render(openGL, X_COORD_LEVEL, Y_COORD_LEVEL, WIDTH_LEVEL, HEIGHT_LEVEL, TEXTURES[TEXTURE_WORD_LEVEL_INDEX]);

                    //render the level #
                    STAT_LEVEL.render(openGL);
                }
                break;

            case Challenge:
            case Infinite:

                //render header text
                entity.render(openGL, X_COORD_SCORE, Y_COORD_SCORE, WIDTH_SCORE, HEIGHT_SCORE, TEXTURES[TEXTURE_WORD_SCORE_INDEX]);

                //render current status
                STAT_CURRENT.setDescription(MANAGER.getBoard().getScore(), false);
                STAT_CURRENT.render(openGL);

                //render best text
                entity.render(openGL, X_COORD_BEST, Y_COORD_BEST, WIDTH_BEST, HEIGHT_BEST, TEXTURES[TEXTURE_WORD_BEST_INDEX]);

                //render the record as well
                STAT_RECORD.render(openGL);

                //if challenge render the remaining time
                if (MODE == Mode.Challenge) {

                    //render header text
                    entity.render(openGL, X_COORD_LEVEL, Y_COORD_LEVEL, WIDTH_LEVEL, HEIGHT_LEVEL, TEXTURES[TEXTURE_WORD_TIME_INDEX]);

                    //render the time remaining
                    STAT_TIME.setDescription((duration > CHALLENGE_DURATION) ? 0 : CHALLENGE_DURATION - duration, true);
                    STAT_TIME.render(openGL);
                }
                break;

            default:
                throw new RuntimeException("Mode not handled here: " + MODE.toString());
        }
    }
}