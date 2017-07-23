package com.gamesbykevin.a2048.game;

import com.gamesbykevin.a2048.base.EntityItem;
import com.gamesbykevin.a2048.board.Board;
import com.gamesbykevin.a2048.game.GameManager.Step;
import com.gamesbykevin.a2048.util.StatDescription;
import com.gamesbykevin.a2048.util.UtilityHelper;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.activity.GameActivity.MANAGER;
import static com.gamesbykevin.a2048.activity.GameActivity.STATS;
import static com.gamesbykevin.a2048.board.Block.VALUES;
import static com.gamesbykevin.a2048.game.GameManager.STEP;
import static com.gamesbykevin.a2048.level.Stats.DIFFICULTY;
import static com.gamesbykevin.a2048.level.Stats.MODE;
import static com.gamesbykevin.a2048.opengl.OpenGLRenderer.TEXTURES;
import static com.gamesbykevin.a2048.opengl.OpenGLSurfaceView.FPS;
import static com.gamesbykevin.a2048.opengl.OpenGLSurfaceView.HEIGHT;
import static com.gamesbykevin.a2048.opengl.OpenGLSurfaceView.WIDTH;

/**
 * Created by Kevin on 7/3/2017.
 */

public class GameManagerHelper {

    /**
     * The duration we wait until we show the game over screen
     */
    protected static final int GAME_OVER_FRAMES_DELAY = (FPS * 2);

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
    public static final int TOTAL_WORD_TEXTURES = 18;

    //index for each word in the texture array
    public static final int TEXTURE_WORD_BEST_INDEX = 33;
    public static final int TEXTURE_WORD_LEVEL_INDEX = 34;
    public static final int TEXTURE_WORD_SCORE_INDEX = 35;
    public static final int TEXTURE_WORD_GAMEOVER_INDEX = 36;
    public static final int TEXTURE_WORD_TIME_INDEX = 37;
    public static final int TEXTURE_BACKGROUND_INDEX = 38;
    public static final int TEXTURE_WORD_WIN_INDEX = 39;
    public static final int TEXTURE_MODE_ORIGINAL_INDEX = 40;
    public static final int TEXTURE_RULES_ORIGINAL_INDEX = 41;
    public static final int TEXTURE_MODE_PUZZLE_INDEX = 42;
    public static final int TEXTURE_RULES_PUZZLE_INDEX = 43;
    public static final int TEXTURE_MODE_CHALLENGE_INDEX = 44;
    public static final int TEXTURE_RULES_CHALLENGE_INDEX = 45;
    public static final int TEXTURE_MODE_INFINITE_INDEX = 46;
    public static final int TEXTURE_RULES_INFINITE_INDEX = 47;
    public static final int TEXTURE_DIFFICULTY_EASY_INDEX = 48;
    public static final int TEXTURE_DIFFICULTY_MEDIUM_INDEX = 49;
    public static final int TEXTURE_DIFFICULTY_HARD_INDEX = 50;

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

    //where to render game over
    private static final int X_COORD_WIN = 90;
    private static final int Y_COORD_WIN = 625;
    private static final int WIDTH_WIN = 299;
    private static final int HEIGHT_WIN = 84;

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

    //resize below differently
    private static final float RATIO_DESC = 0.85f;

    //where to render game mode
    private static final int X_COORD_MODE = 15;
    private static final int Y_COORD_MODE = Y_COORD_GAMEOVER - 15;
    private static final int WIDTH_MODE_ORIGINAL = (int)(298 * RATIO_DESC);
    private static final int HEIGHT_MODE_ORIGINAL = (int)(56 * RATIO_DESC);
    private static final int WIDTH_MODE_PUZZLE = (int)(267 * RATIO_DESC);
    private static final int HEIGHT_MODE_PUZZLE = (int)(56 * RATIO_DESC);
    private static final int WIDTH_MODE_CHALLENGE = (int)(345 * RATIO_DESC);
    private static final int HEIGHT_MODE_CHALLENGE = (int)(56 * RATIO_DESC);
    private static final int WIDTH_MODE_INFINITE = (int)(272 * RATIO_DESC);
    private static final int HEIGHT_MODE_INFINITE = (int)(56 * RATIO_DESC);

    //where to render game difficulty
    private static final int X_COORD_DIFFICULTY = X_COORD_MODE;
    private static final int Y_COORD_DIFFICULTY = Y_COORD_MODE + HEIGHT_MODE_ORIGINAL;
    private static final int WIDTH_DIFFICULTY_EASY = (int)(311 * RATIO_DESC);
    private static final int HEIGHT_DIFFICULTY_EASY = (int)(56 * RATIO_DESC);
    private static final int WIDTH_DIFFICULTY_MEDIUM = (int)(356 * RATIO_DESC);
    private static final int HEIGHT_DIFFICULTY_MEDIUM = (int)(56 * RATIO_DESC);
    private static final int WIDTH_DIFFICULTY_HARD = (int)(316 * RATIO_DESC);
    private static final int HEIGHT_DIFFICULTY_HARD = (int)(56 * RATIO_DESC);

    //where to render game rules
    private static final int X_COORD_RULES = X_COORD_MODE;
    private static final int Y_COORD_RULES = Y_COORD_DIFFICULTY + HEIGHT_MODE_ORIGINAL;
    private static final int WIDTH_RULES_ORIGINAL = (int)(511 * RATIO_DESC);
    private static final int HEIGHT_RULES_ORIGINAL = (int)(56 * RATIO_DESC);
    private static final int WIDTH_RULES_PUZZLE = (int)(523 * RATIO_DESC);
    private static final int HEIGHT_RULES_PUZZLE = (int)(56 * RATIO_DESC);
    private static final int WIDTH_RULES_CHALLENGE = (int)(561 * RATIO_DESC);
    private static final int HEIGHT_RULES_CHALLENGE = (int)(57 * RATIO_DESC);
    private static final int WIDTH_RULES_INFINITE = (int)(480 * RATIO_DESC);
    private static final int HEIGHT_RULES_INFINITE = (int)(56 * RATIO_DESC);

    //object used to render texture words
    private static EntityItem entity = new EntityItem();

    //did we win
    public static boolean WIN = false;

    public static void updateDisplayStats() {

        //set coordinates etc... for the text we are rendering based on game mode
        switch (MODE) {

            case Puzzle:
            case Original:

                STAT_CURRENT.setX(X_COORD_RESULTS);
                STAT_CURRENT.setAnchorX(STAT_CURRENT.getX());
                STAT_CURRENT.setY(Y_COORD_RESULTS);
                STAT_CURRENT.setDescription(0, true, true);

                STAT_RECORD.setX(X_COORD_RECORD);
                STAT_RECORD.setAnchorX(STAT_RECORD.getX());
                STAT_RECORD.setY(Y_COORD_RECORD);
                STAT_RECORD.setDescription(STATS.getLevel().getDuration(), true, true);

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
                STAT_CURRENT.setDescription(MANAGER.getBoard().getScore(), false, true);

                //render the record as well
                STAT_RECORD.setX(X_COORD_RECORD);
                STAT_RECORD.setAnchorX(STAT_RECORD.getX());
                STAT_RECORD.setY(Y_COORD_RECORD);
                STAT_RECORD.setDescription(STATS.getLevel().getScore(), false, true);

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

        //game over will vary depending on the game mode
        switch (mode) {

            case Puzzle:

                //if there is one block left, the game is over
                if (board.getBlocks().size() == 1) {
                    WIN = true;
                    return true;
                } else {
                    return false;
                }

            case Challenge:

                //if time is expired or if no valid moves exist, the game is over
                if (CHALLENGE_DURATION - board.getDuration() <= 0 || !board.hasMove()) {

                    //game is over
                    WIN = false;

                    //set time to 00:00:00
                    board.setDuration(CHALLENGE_DURATION);

                    //yes, the game is over
                    return true;

                } else {

                    //if time hasn't expired, the game is not over
                    return false;
                }

            case Infinite:

                //if no more moves game over
                if (!board.hasMove()) {
                    WIN = false;
                    return true;
                } else {
                    return false;
                }

            /**
             * Original the game continues until there are no more moves available
             */
            case Original:

                //if we don't have any more moves the game is over
                if (!board.hasMove()) {
                    WIN = false;
                    return true;
                }

                //if one block has 2048 the game is over
                if (board.hasValue(ORIGINAL_MODE_GOAL_VALUE)) {
                    WIN = true;
                    return true;
                }

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
    public static void drawBackground(GL10 openGL) {
        entity.render(openGL, 0, 0, WIDTH, HEIGHT, TEXTURES[TEXTURE_BACKGROUND_INDEX]);
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
        if (STEP == Step.GameOver) {
            if (WIN) {
                entity.render(openGL, X_COORD_WIN, Y_COORD_WIN, WIDTH_WIN, HEIGHT_WIN, TEXTURES[TEXTURE_WORD_WIN_INDEX]);
            } else {
                entity.render(openGL, X_COORD_GAMEOVER, Y_COORD_GAMEOVER, WIDTH_GAMEOVER, HEIGHT_GAMEOVER, TEXTURES[TEXTURE_WORD_GAMEOVER_INDEX]);
            }
        } else {

            //display the mode and rules
            switch (MODE) {
                case Original:
                    entity.render(openGL, X_COORD_MODE, Y_COORD_MODE, WIDTH_MODE_ORIGINAL, HEIGHT_MODE_ORIGINAL, TEXTURES[TEXTURE_MODE_ORIGINAL_INDEX]);
                    entity.render(openGL, X_COORD_RULES, Y_COORD_RULES, WIDTH_RULES_ORIGINAL, HEIGHT_RULES_ORIGINAL, TEXTURES[TEXTURE_RULES_ORIGINAL_INDEX]);
                    break;

                case Puzzle:
                    entity.render(openGL, X_COORD_MODE, Y_COORD_MODE, WIDTH_MODE_PUZZLE, HEIGHT_MODE_PUZZLE, TEXTURES[TEXTURE_MODE_PUZZLE_INDEX]);
                    entity.render(openGL, X_COORD_RULES, Y_COORD_RULES, WIDTH_RULES_PUZZLE, HEIGHT_RULES_PUZZLE, TEXTURES[TEXTURE_RULES_PUZZLE_INDEX]);
                    break;

                case Challenge:
                    entity.render(openGL, X_COORD_MODE, Y_COORD_MODE, WIDTH_MODE_CHALLENGE, HEIGHT_MODE_CHALLENGE, TEXTURES[TEXTURE_MODE_CHALLENGE_INDEX]);
                    entity.render(openGL, X_COORD_RULES, Y_COORD_RULES, WIDTH_RULES_CHALLENGE, HEIGHT_RULES_CHALLENGE, TEXTURES[TEXTURE_RULES_CHALLENGE_INDEX]);
                    break;

                case Infinite:
                    entity.render(openGL, X_COORD_MODE, Y_COORD_MODE, WIDTH_MODE_INFINITE, HEIGHT_MODE_INFINITE, TEXTURES[TEXTURE_MODE_INFINITE_INDEX]);
                    entity.render(openGL, X_COORD_RULES, Y_COORD_RULES, WIDTH_RULES_INFINITE, HEIGHT_RULES_INFINITE, TEXTURES[TEXTURE_RULES_INFINITE_INDEX]);
                    break;

                default:
                    throw new RuntimeException("Mode not handled here: " + MODE.toString());
            }

            //display the difficulty
            switch (DIFFICULTY) {
                case Easy:
                    entity.render(openGL, X_COORD_DIFFICULTY, Y_COORD_DIFFICULTY, WIDTH_DIFFICULTY_EASY, HEIGHT_DIFFICULTY_EASY, TEXTURES[TEXTURE_DIFFICULTY_EASY_INDEX]);
                    break;

                case Medium:
                    entity.render(openGL, X_COORD_DIFFICULTY, Y_COORD_DIFFICULTY, WIDTH_DIFFICULTY_MEDIUM, HEIGHT_DIFFICULTY_MEDIUM, TEXTURES[TEXTURE_DIFFICULTY_MEDIUM_INDEX]);
                    break;

                case Hard:
                    entity.render(openGL, X_COORD_DIFFICULTY, Y_COORD_DIFFICULTY, WIDTH_DIFFICULTY_HARD, HEIGHT_DIFFICULTY_HARD, TEXTURES[TEXTURE_DIFFICULTY_HARD_INDEX]);
                    break;

                default:
                    throw new RuntimeException("Difficulty not handled here: " + DIFFICULTY.toString());
            }
        }


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