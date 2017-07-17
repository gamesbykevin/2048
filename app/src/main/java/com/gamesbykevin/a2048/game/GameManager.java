package com.gamesbykevin.a2048.game;

/**
 * Created by Kevin on 5/26/2017.
 */
import android.view.MotionEvent;

import com.gamesbykevin.a2048.activity.GameActivity;
import com.gamesbykevin.a2048.board.Board;
import com.gamesbykevin.a2048.board.BoardHelper;

import javax.microedition.khronos.opengles.GL10;

import com.gamesbykevin.a2048.game.GameManagerHelper.Mode;
import com.gamesbykevin.a2048.services.GooglePlayServicesHelper;
import com.gamesbykevin.a2048.util.UtilityHelper;

import static com.gamesbykevin.a2048.activity.GameActivity.STATS;
import static com.gamesbykevin.a2048.game.GameManagerHelper.GAME_OVER_FRAMES_DELAY;
import static com.gamesbykevin.a2048.game.GameManagerHelper.ORIGINAL_MODE_GOAL_VALUE;
import static com.gamesbykevin.a2048.game.GameManagerHelper.RESET;
import static com.gamesbykevin.a2048.level.Stats.MODE;
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

    /**
     * The pixel length the user needs to swipe a motion event in order to detect up/down/left/right
     */
    private static final int SWIPE_DISTANCE = 16;

    //keep track so we know when to display the game over screen
    private int frames = 0;

    //is the game over
    public static boolean GAME_OVER = false;

    /**
     * Default constructor
     */
    public GameManager(final GameActivity activity) throws RuntimeException {

        //store our activity reference
        this.activity = activity;

        //create a new game board
        this.board = new Board();

        //default false
        GAME_OVER = false;
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

                            //merging east
                            this.merge = Merge.East;

                        } else {

                            //merging west
                            this.merge = Merge.West;

                        }
                    } else {

                        //if the y difference is greater than x then we are swiping vertical
                        if (y > this.downY) {

                            //merging south
                            this.merge = Merge.South;

                        } else {

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
            if (MODE == Mode.Puzzle) {

                //generate random board
                BoardHelper.generatePuzzle(
                    getBoard(),
                    STATS.getLevel().getSeed()
                );
            }

            //no need to continue at this point
            return;
        }

        //store the game over status
        boolean gameOver = GAME_OVER;

        //if we aren't ready, check if the textures have loaded
        if (activity.getStep() == GameActivity.Step.Loading) {

            //check if the textures have loaded
            if (LOADED) {

                //update the text displayed on the screen
                GameManagerHelper.updateDisplayStats();

                switch (MODE) {

                    case Puzzle:
                        activity.setStep(GameActivity.Step.LevelSelect);
                        break;

                    case Original:
                    case Challenge:
                    case Infinite:
                        activity.setStep(GameActivity.Step.Ready);
                        break;

                    default:
                        throw new RuntimeException("Mode: " + MODE.toString() + " is not handled");
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
                if (MODE != Mode.Puzzle) {

                    //spawn a new block
                    getBoard().spawn();
                }
            }

            //check if the game is over
            GAME_OVER = GameManagerHelper.isGameOver(getBoard(), MODE);

            //if the game wasn't over previously, but now is
            if (!gameOver && GAME_OVER) {

                //vibrate the phone
                activity.vibrate();

                //reset frames timer
                frames = 0;

                //UtilityHelper.logEvent("GAME OVER!!!");

                //update our achievements
                GooglePlayServicesHelper.checkCompletedGame(activity, getBoard());

                //did we beat a personal best
                boolean record = false;

                //if original mode, make sure they have 2048 value
                if (MODE == Mode.Original) {

                    //make sure they solved the board to check if we beat a record
                    if (getBoard().hasValue(ORIGINAL_MODE_GOAL_VALUE))
                        record = STATS.markComplete(getBoard().getDuration(), getBoard().getScore());
                } else {
                    //did we set a record
                    record = STATS.markComplete(getBoard().getDuration(), getBoard().getScore());
                }

                //if puzzle mode, check if we completed a level quickly
                if (MODE == Mode.Puzzle)
                    GooglePlayServicesHelper.checkAchievementsPuzzleTime(activity, getBoard());

                //if we beat a previous best, check achievements
                if (record)
                    GooglePlayServicesHelper.checkAchievementsNewRecord(activity);
            }

            //update the state of the blocks on the board no matter what
            getBoard().update();

            //check if any new blocks are created for achievements
            GooglePlayServicesHelper.checkAchievementsNewBlocks(activity);

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
     * @param openGL Surface used for rendering pixels
     */
    public void draw(GL10 openGL, final int[] textures) {

        try {
            //don't display if we are resetting
            if (RESET || !LOADED)
                return;

            //only render when ready, or game over to prevent screen flicker
            if (activity.getStep() != GameActivity.Step.Ready && activity.getStep() != GameActivity.Step.GameOver)
                return;

            //assign the appropriate textures
            getBoard().assignTextures(textures);

            //draw everything on the board
            getBoard().draw(openGL);

            //draw our text on-screen
            GameManagerHelper.drawText(openGL, getBoard().getDuration());

        } catch (Exception e) {
            UtilityHelper.handleException(e);
        }
    }
}