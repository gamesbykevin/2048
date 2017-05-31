package com.gamesbykevin.a2048.game;

/**
 * Created by Kevin on 5/26/2017.
 */

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.a2048.GameActivity;
import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.board.Block;
import com.gamesbykevin.a2048.board.Board;

/**
 * The GameMananger class will keep all of our game object(s) logic
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

    //which direction do we want to merge
    private boolean north = false, south = false, east = false, west = false;

    /**
     * Default constructor
     */
    public GameManager(final GameActivity activity) {

        //store our activity reference
        this.activity = activity;

        //create a new game board
        this.board = new Board(
            BitmapFactory.decodeResource(activity.getResources(), R.drawable.blocks),
            BitmapFactory.decodeResource(activity.getResources(), R.drawable.border));
    }

    public boolean onTouchEvent(final int action, final float x, final float y) {

        //don't continue if we have already swiped in a direction
        if (north || south || west || east)
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

                    //if we didn't swipe enough, no need to continue
                    if (diffX < Board.BORDER_DIMENSIONS && diffY < Board.BORDER_DIMENSIONS)
                        return true;

                    //determine which way we are swiping
                    if (diffX > diffY) {

                        //if the x difference is greater than y then we are swiping horizontal
                        if (x > this.downX) {

                            //we are swiping right
                            MainActivity.logEvent("Swiping Right");

                            //flag east true
                            this.east = true;

                        } else {

                            //we are swiping left
                            MainActivity.logEvent("Swiping Left");

                            //flag west true
                            this.west = true;
                        }

                    } else {
                        //if the y difference is greater than x then we are swiping vertical
                        if (y > this.downY) {

                            //we are swiping down
                            MainActivity.logEvent("Swiping Down");

                            //flag south true
                            this.south = true;

                        } else {

                            //we are swiping up
                            MainActivity.logEvent("Swiping Up");

                            //flag north true
                            this.north = true;
                        }
                    }

                    //update the blocks accordingly on where we want to head
                    if (north) {

                    } else if (south) {

                    } else if (west) {

                    } else if (east) {

                    }
                }

                break;
        }

        return true;
    }

    /**
     * Update game objects
     */
    public void update() {
        this.board.update();
    }

    /**
     * Render the game objects
     * @param canvas Surface used for rendering pixels
     */
    public void draw(Canvas canvas) {
        try {
            this.board.draw(canvas);
        } catch (Exception e) {
            MainActivity.handleException(e);
        }
    }
}