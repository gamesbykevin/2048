package com.gamesbykevin.a2048.game;

import com.gamesbykevin.a2048.base.EntityItem;
import com.gamesbykevin.a2048.board.Board;
import com.gamesbykevin.a2048.util.StatDescription;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.GameActivity.MANAGER;
import static com.gamesbykevin.a2048.GameActivity.STATS;
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

    //best score
    public static int SCORE = 0;

    //best time
    public static String TIME_DESC = "";

    public enum Difficulty {
        Easy, Medium, Hard
    }

    public enum Mode {
        Original, Puzzle, Challenge, Infinite
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

    //object used to render number images
    private static StatDescription DESCRIPTION = new StatDescription();

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

        //enable texture, alpha, and alpha blending which supports transparency
        openGL.glEnable(GL10.GL_TEXTURE_2D);
        openGL.glEnable(GL10.GL_BLEND);
        openGL.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        //if game over, notify the user
        if (MANAGER.GAME_OVER) {
            entity.setX(X_COORD_GAMEOVER);
            entity.setY(Y_COORD_GAMEOVER);
            entity.setWidth(WIDTH_GAMEOVER);
            entity.setHeight(HEIGHT_GAMEOVER);
            entity.setTextureId(TEXTURES[TEXTURE_WORD_GAMEOVER_INDEX]);
            entity.render(openGL);
        }

        switch (MODE) {

            case Puzzle:
            case Original:
                entity.setX(X_COORD_TIME);
                entity.setY(Y_COORD_TIME);
                entity.setWidth(WIDTH_TIME);
                entity.setHeight(HEIGHT_TIME);
                entity.setTextureId(TEXTURES[TEXTURE_WORD_TIME_INDEX]);
                entity.render(openGL);

                DESCRIPTION.setX(X_COORD_RESULTS);
                DESCRIPTION.setY(Y_COORD_RESULTS);
                DESCRIPTION.setDescription(DATE_FORMAT.format(duration), TEXTURES);
                DESCRIPTION.render(openGL);

                //render best text
                entity.setX(X_COORD_BEST);
                entity.setY(Y_COORD_BEST);
                entity.setWidth(WIDTH_BEST);
                entity.setHeight(HEIGHT_BEST);
                entity.setTextureId(TEXTURES[TEXTURE_WORD_BEST_INDEX]);
                entity.render(openGL);

                //render the record as well
                DESCRIPTION.setX(X_COORD_RECORD);
                DESCRIPTION.setY(Y_COORD_RECORD);
                DESCRIPTION.setDescription(TIME_DESC, TEXTURES);
                DESCRIPTION.render(openGL);

                //if puzzle render level #
                if (MODE == Mode.Puzzle) {
                    entity.setX(X_COORD_LEVEL);
                    entity.setY(Y_COORD_LEVEL);
                    entity.setWidth(WIDTH_LEVEL);
                    entity.setHeight(HEIGHT_LEVEL);
                    entity.setTextureId(TEXTURES[TEXTURE_WORD_LEVEL_INDEX]);
                    entity.render(openGL);

                    DESCRIPTION.setX(X_COORD_LEVEL + WIDTH_LEVEL + 10);
                    DESCRIPTION.setY(Y_COORD_LEVEL + 3);
                    DESCRIPTION.setDescription(LEVEL_DESC, TEXTURES);
                    DESCRIPTION.render(openGL);
                }
                break;

            case Challenge:
            case Infinite:
                entity.setX(X_COORD_SCORE);
                entity.setY(Y_COORD_SCORE);
                entity.setWidth(WIDTH_SCORE);
                entity.setHeight(HEIGHT_SCORE);
                entity.setTextureId(TEXTURES[TEXTURE_WORD_SCORE_INDEX]);
                entity.render(openGL);

                DESCRIPTION.setX(X_COORD_RESULTS);
                DESCRIPTION.setY(Y_COORD_RESULTS);
                DESCRIPTION.setDescription(MANAGER.getBoard().getScore(), TEXTURES);
                DESCRIPTION.render(openGL);

                //render best text
                entity.setX(X_COORD_BEST);
                entity.setY(Y_COORD_BEST);
                entity.setWidth(WIDTH_BEST);
                entity.setHeight(HEIGHT_BEST);
                entity.setTextureId(TEXTURES[TEXTURE_WORD_BEST_INDEX]);
                entity.render(openGL);

                //render the record as well
                DESCRIPTION.setX(X_COORD_RECORD);
                DESCRIPTION.setY(Y_COORD_RECORD);
                DESCRIPTION.setDescription(SCORE, TEXTURES);
                DESCRIPTION.render(openGL);

                //if challenge render the remaining time
                if (MODE == Mode.Challenge) {

                    entity.setX(X_COORD_LEVEL);
                    entity.setY(Y_COORD_LEVEL);
                    entity.setWidth(WIDTH_LEVEL);
                    entity.setHeight(HEIGHT_LEVEL);
                    entity.setTextureId(TEXTURES[TEXTURE_WORD_TIME_INDEX]);
                    entity.render(openGL);

                    DESCRIPTION.setX(X_COORD_LEVEL + WIDTH_LEVEL + 10);
                    DESCRIPTION.setY(Y_COORD_LEVEL + 3);

                    if (duration > CHALLENGE_DURATION) {
                        DESCRIPTION.setDescription(DATE_FORMAT.format(0), TEXTURES);
                    } else {
                        DESCRIPTION.setDescription(DATE_FORMAT.format(CHALLENGE_DURATION - duration), TEXTURES);
                    }
                    DESCRIPTION.render(openGL);
                }
                break;
        }
    }
}