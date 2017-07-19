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

import static com.gamesbykevin.a2048.activity.GameActivity.Screen;
import static com.gamesbykevin.a2048.activity.GameActivity.STATS;
import static com.gamesbykevin.a2048.game.GameManagerHelper.GAME_OVER_FRAMES_DELAY;
import static com.gamesbykevin.a2048.game.GameManagerHelper.ORIGINAL_MODE_GOAL_VALUE;
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

    public enum Step {
        Start,
        Reset,
        Loading,
        GameOver,
        InitiateMerge,
        Merging,
        Updating
    }

    //what step are we on?
    public static Step STEP = Step.Loading;

    /**
     * The pixel length the user needs to swipe a motion event in order to detect up/down/left/right
     */
    private static final int SWIPE_DISTANCE = 16;

    //keep track so we know when to display the game over screen
    private int frames = 0;

    /**
     * Default constructor
     */
    public GameManager(final GameActivity activity) throws RuntimeException {

        //store our activity reference
        this.activity = activity;

        //create a new game board
        this.board = new Board(activity);
    }

    public void onResume() {
        //do anything here?
    }

    public boolean onTouchEvent(final int action, final float x, final float y) {

        //don't continue if we aren't ready yet
        if (STEP != Step.Updating)
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

                    //set the next step to initiate the merge
                    STEP = Step.InitiateMerge;
                }
                break;
        }

        return true;
    }

    /**
     * Update game objects
     */
    public void update() throws Exception {

        switch (STEP) {

            //we are loading
            case Loading:

                //if the textures have finished loading
                if (LOADED) {

                    switch (MODE) {

                        case Puzzle:

                            //if loaded display level select screen
                            activity.setScreen(Screen.LevelSelect);

                            //go to start step
                            STEP = Step.Start;
                            break;

                        case Original:
                        case Challenge:
                        case Infinite:

                            //display loading screen while we reset
                            activity.setScreen(Screen.Loading);

                            //reset the board
                            STEP = Step.Reset;
                            break;

                        default:
                            throw new RuntimeException("Mode: " + MODE.toString() + " is not handled");
                    }

                }
                break;

            //don't do anything
            case Start:
                break;

            //we are resetting the board
            case Reset:

                //reset the board to restart
                getBoard().reset();

                //if puzzle mode, generate board with the specified seed
                if (MODE == Mode.Puzzle)
                    BoardHelper.generatePuzzle(getBoard(), STATS.getLevel().getSeed());

                //update the text displayed on the screen
                GameManagerHelper.updateDisplayStats();

                //after resetting, next step is updating
                STEP = Step.Updating;

                //we can go to ready now
                activity.setScreen(Screen.Ready);
                break;

            case InitiateMerge:

                //continue to update the board
                getBoard().update();

                //update the blocks accordingly on where we want to head
                BoardHelper.merge(getBoard(), this.merge, true);

                //we can go to updating
                STEP = Step.Merging;
                break;

            case Merging:

                //continue to update the board
                getBoard().update();

                //check if the blocks are at their target
                if (getBoard().hasTarget()) {

                    //now that the blocks are at their target we can spawn (if applicable)
                    if (MODE != Mode.Puzzle)
                        getBoard().spawn();

                    //continue updating
                    STEP = Step.Updating;
                }
                break;

            case Updating:

                //if the game is over, move to the next step
                if (GameManagerHelper.isGameOver(getBoard(), MODE)) {

                    //move to game over step
                    STEP = Step.GameOver;

                    //vibrate the phone
                    activity.vibrate();

                    //reset frames timer
                    frames = 0;

                    //display message
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

                //continue to update the board
                getBoard().update();
                break;

            case GameOver:

                //continue to update the board
                getBoard().update();

                //keep counting if enough time has not yet passed
                if (frames < GAME_OVER_FRAMES_DELAY) {

                    //keep track of frames elapsed
                    frames++;

                    //if we are now ready to display go ahead and do it
                    if (frames >= GAME_OVER_FRAMES_DELAY)
                        activity.setScreen(Screen.GameOver);
                }
                break;
        }
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

            //make sure we can render
            if (!canRender())
                return;

            //render background
            GameManagerHelper.drawBackground(openGL);

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

    public static boolean canRender() {

        switch (STEP) {

            //only render in open gl for these steps
            case Merging:
            case Updating:
            case GameOver:
            case InitiateMerge:
                return true;

            //don't display for these
            case Loading:
            case Reset:
            case Start:
                return false;

            default:
                throw new RuntimeException("Step not handled here: " + STEP.toString());
        }
    }
}