package com.gamesbykevin.a2048.game;

/**
 * Created by Kevin on 5/26/2017.
 */

import android.view.MotionEvent;

import com.gamesbykevin.a2048.GameActivity;
import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.board.Board;
import com.gamesbykevin.a2048.board.BoardHelper;
import com.gamesbykevin.a2048.ui.LevelItems;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.game.GameManagerHelper.DIMENSIONS_EASY;
import static com.gamesbykevin.a2048.game.GameManagerHelper.DIMENSIONS_HARD;
import static com.gamesbykevin.a2048.game.GameManagerHelper.DIMENSIONS_MEDIUM;
import static com.gamesbykevin.a2048.game.GameManagerHelper.GAME_OVER_FRAMES_DELAY;
import static com.gamesbykevin.a2048.game.GameManagerHelper.PUZZLE_DIMENSIONS_EASY;
import static com.gamesbykevin.a2048.game.GameManagerHelper.PUZZLE_DIMENSIONS_HARD;
import static com.gamesbykevin.a2048.game.GameManagerHelper.PUZZLE_DIMENSIONS_MEDIUM;
import static com.gamesbykevin.a2048.game.GameManagerHelper.RESET;
import static com.gamesbykevin.a2048.opengl.OpenGLRenderer.LOADED;

/**
 * The Game Manager class will keep all of our game object(s) logic
 */
public class GameManager {

    //the game board
    private Board board;

    //store our activity reference
    private final GameActivity activity;

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
     * The pixel length the user needs to swipe a motion event in order to detect up/down/left/right
     */
    private static final int SWIPE_DISTANCE = 16;

    //keep track so we know when to display the game over screen
    private int frames = 0;

    public enum Difficulty {
        Easy, Medium, Hard
    }

    public enum Mode {
        Original, Puzzle, Challenge
    }

    //the stats for our level items
    private final LevelItems levelItems;

    //is the game over
    public static boolean GAME_OVER = false;

    /**
     * Default constructor
     */
    public GameManager(final GameActivity activity) throws RuntimeException {

        //store our activity reference
        this.activity = activity;

        //create a new instance of our level items
        this.levelItems = new LevelItems(activity);

        //size of the board
        final int dimensions;

        //puzzle mode will have a different size board
        boolean puzzle = activity.hasSetting(R.string.mode_file_key, Mode.class, Mode.Puzzle);

        //figure out the size of the board based on difficulty and game mode
        switch ((Difficulty)activity.getObjectValue(R.string.difficulty_file_key, Difficulty.class)) {
            case Easy:
                dimensions = puzzle ? PUZZLE_DIMENSIONS_EASY : DIMENSIONS_EASY;
                break;

            case Medium:
                dimensions = puzzle ? PUZZLE_DIMENSIONS_MEDIUM : DIMENSIONS_MEDIUM;
                break;

            case Hard:
                dimensions = puzzle ? PUZZLE_DIMENSIONS_HARD : DIMENSIONS_HARD;
                break;

            default:
                throw new RuntimeException("Difficulty not managed");
        }

        //create a new game board
        this.board = new Board(dimensions, dimensions);

        //default false
        GAME_OVER = false;
    }

    public LevelItems getLevelItems() {
        return this.levelItems;
    }

    public void onResume() {
        //do anything here?
    }

    public boolean onTouchEvent(final int action, final float x, final float y) {

        //don't continue if we are in the process of merging
        if (flagMerge || merge != null)
            return true;

        //if the game is over we can't continue
        if (GAME_OVER)
            return true;

        //can't continue if we are resetting the game or not ready yet
        if (RESET || activity.getStep() != GameActivity.Step.Ready)
            return true;

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
                    if (diffX < SWIPE_DISTANCE && diffY < SWIPE_DISTANCE)
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

        //do we want to reset the game?
        if (RESET) {

            //flag false
            RESET = false;

            //reset the board to restart
            getBoard().reset();

            //default false
            GAME_OVER = false;

            //if puzzle mode, generate board with the specified seed
            if (activity.hasSetting(R.string.mode_file_key, Mode.class, Mode.Puzzle)) {

                //generate random board
                BoardHelper.generatePuzzle(
                    getBoard(),
                    getLevelItems().getLevelItem(Mode.Puzzle,
                    (Difficulty)activity.getObjectValue(R.string.difficulty_file_key, Difficulty.class)).getSeed()
                );

            } else {

                //every other mode will have 1 level
                getLevelItems().setLevelIndex(0);
            }

            //no need to continue at this point
            return;
        }

        //if we aren't ready, check if the textures have loaded
        if (activity.getStep() == GameActivity.Step.Loading) {

            //check if the textures have loaded
            if (LOADED) {

                if (activity.hasSetting(R.string.mode_file_key, Mode.class, Mode.Original)) {
                    activity.setStep(GameActivity.Step.Ready);
                } else if (activity.hasSetting(R.string.mode_file_key, Mode.class, Mode.Puzzle)) {
                    activity.setStep(GameActivity.Step.LevelSelect);
                } else if (activity.hasSetting(R.string.mode_file_key, Mode.class, Mode.Challenge)) {
                    activity.setStep(GameActivity.Step.Ready);
                } else {
                    throw new RuntimeException("Mode is not handled");
                }
            }

            //no need to continue at this point
            return;
        }

        //do we need to check for a merge
        if (flagMerge && this.merge != null) {

            //update the blocks accordingly on where we want to head
            BoardHelper.merge(getBoard(), this.merge, true);

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

                //we don't spawn blocks for puzzle mode
                if (!activity.hasSetting(R.string.mode_file_key, Mode.class, Mode.Puzzle)) {

                    //spawn a new block
                    getBoard().spawn();
                }

                //check if the game is over
                GAME_OVER = BoardHelper.isGameOver(getBoard(), (Mode)activity.getObjectValue(R.string.mode_file_key, Mode.class));

                //if the game is over
                if (GAME_OVER) {

                    //vibrate the phone
                    activity.vibrate();

                    //reset frames timer
                    frames = 0;

                    //notify if game over
                    MainActivity.logEvent("GAME OVER!!!!!!!!!!!");

                    //mark level completed
                    getLevelItems().update(
                        (Mode)activity.getObjectValue(R.string.mode_file_key, Mode.class),
                        (Difficulty)activity.getObjectValue(R.string.difficulty_file_key, Difficulty.class)
                    );
                }
            }

            //update the state of the blocks on the board no matter what
            getBoard().update();

            //if the game is over, track the time elapsed
            if (GAME_OVER) {

                //keep counting if enough time has not yet passed
                if (!canShowGameOverScreen()) {

                    //keep track of frames elapsed
                    frames++;

                    //if we are now ready to display go ahead and do it
                    if (canShowGameOverScreen())
                        activity.setStep(GameActivity.Step.GameOver);
                }
            }
        }
    }

    /**
     * Can we show the game over screen?
     * @return true if game over and enough time has elapsed, false otherwise
     */
    public boolean canShowGameOverScreen() {
        return (GAME_OVER && frames >= GAME_OVER_FRAMES_DELAY);
    }

    /**
     * Get the game board
     * @return The board containing all blocks in play
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
            //only render when ready, or game over to prevent screen flicker
            if (activity.getStep() != GameActivity.Step.Ready && activity.getStep() != GameActivity.Step.GameOver)
                return;

            //assign the appropriate textures
            getBoard().assignTextures(textures);

            //draw everything on the board
            getBoard().draw(gl);

            //draw our text on-screen
            GameManagerHelper.drawText(gl);

        } catch (Exception e) {
            MainActivity.handleException(e);
        }
    }
}