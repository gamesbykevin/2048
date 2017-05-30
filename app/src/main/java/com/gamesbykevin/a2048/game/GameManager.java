package com.gamesbykevin.a2048.game;

/**
 * Created by Kevin on 5/26/2017.
 */

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.a2048.GameActivity;
import com.gamesbykevin.a2048.GameView;
import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.board.Board;

/**
 * The GameMananger class will keep all of our game object(s) logic
 */
public class GameManager {

    //the game board
    private Board board;

    //store our activity reference
    private final GameActivity activity;

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