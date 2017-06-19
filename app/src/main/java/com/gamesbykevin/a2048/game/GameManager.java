package com.gamesbykevin.a2048.game;

/**
 * Created by Kevin on 5/26/2017.
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gamesbykevin.a2048.GameActivity;
import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.board.Block;
import com.gamesbykevin.a2048.board.Board;
import com.gamesbykevin.a2048.board.BoardHelper;

/**
 * The GameMananger class will keep all of our game object(s) logic
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

    //object used to render the score
    private Paint paint;

    /**
     * Default constructor
     */
    public GameManager(final Context activity) {

        //store our activity reference
        this.activity = activity;

        //create a new game board
        this.board = new Board(
            BitmapFactory.decodeResource(activity.getResources(), R.drawable.blocks),
            BitmapFactory.decodeResource(activity.getResources(), R.drawable.border));
    }

    public boolean onTouchEvent(final int action, final float x, final float y) {

        //don't continue if we are already merging
        if (merge != null)
            return true;

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
                    if (diffX < Board.BORDER_DIMENSIONS / 2 && diffY < Board.BORDER_DIMENSIONS / 2)
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

                    //update the blocks accordingly on where we want to head
                    switch (this.merge) {

                        case West:
                            BoardHelper.mergeWest(getBoard());
                            break;

                        case East:
                            BoardHelper.mergeEast(getBoard());
                            break;

                        case North:
                            BoardHelper.mergeNorth(getBoard());
                            break;

                        case South:
                            BoardHelper.mergeSouth(getBoard());
                            break;
                    }

                    //if all blocks are already at their target, then nothing will happen, go again
                    if (getBoard().hasTarget())
                        this.merge = null;
                }

                break;
        }

        return true;
    }

    /**
     * Update game objects
     */
    public void update() {

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

        getBoard().update();
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
     * @param canvas Surface used for rendering pixels
     */
    public void draw(Canvas canvas) {

        try {

            /*
            //render the board
            getBoard().draw(canvas);

            if (this.paint == null) {
                this.paint = new Paint();
                this.paint.setStyle(Paint.Style.FILL);
                this.paint.setTextSize(48f);
                this.paint.setColor(Color.WHITE);
            }

            //render the current score
            canvas.drawText("Score: " + getBoard().getScore(), Block.START_X, 75, paint);

            if (getBoard().isGameover()) {
                canvas.drawText("Game Over", Block.START_X, 700, paint);
            }
            */
        } catch (Exception e) {
            MainActivity.handleException(e);
        }
    }
}