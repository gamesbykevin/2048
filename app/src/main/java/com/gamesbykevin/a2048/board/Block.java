package com.gamesbykevin.a2048.board;

import com.gamesbykevin.a2048.base.EntityItem;
import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.base.Entity;

import static com.gamesbykevin.a2048.board.Board.BORDER_THICKNESS;

/**
 * Created by Kevin on 5/29/2017.
 */

public class Block extends EntityItem {

    //what is the value of this block
    private int value = 0;

    //where are we targeting to go
    private Cell target;

    /**
     * Size of animation on sprite sheet
     */
    public static final int ANIMATION_DIMENSIONS = 90;

    /**
     * The starting coordinate of the north-west block
     */
    public static final int START_X = 48;

    /**
     * The starting coordinate of the north-west block
     */
    public static final int START_Y = 125;

    /**
     * How fast do the blocks move?
     */
    protected static final float VELOCITY = 0.25f;

    /**
     * No velocity
     */
    protected static final float VELOCITY_NONE = 0.0f;

    /**
     * Default constructor
     */
    protected Block() {

        //call parent constructor
        super();

        //create new target
        this.target = new Cell();

        //assign the size at which we will render the  block
        setWidth(ANIMATION_DIMENSIONS);
        setHeight(ANIMATION_DIMENSIONS);
    }

    /**
     * Update the block location based on (x,y) velocity
     */
    protected void update() {

        //if we are not at our target update
        if (!hasTarget()) {

            //if the column does not match, we need to move horizontal
            if (getCol() != getTarget().getCol()) {
                if (getCol() > getTarget().getCol()) {
                    setDX(-VELOCITY);
                } else {
                    setDX(VELOCITY);
                }
            }

            //if the row does not match we need to move vertical
            if (getRow() != getTarget().getRow()) {
                if (getRow() > getTarget().getRow()) {
                    setDY(-VELOCITY);
                } else {
                    setDY(VELOCITY);
                }
            }

            //update our location
            setCol(getCol() + getDX());
            setRow(getRow() + getDY());

            //check horizontal velocity
            if (getDX() > 0) {
                if (getCol() >= getTarget().getCol()) {
                    setCol(getTarget().getCol());
                    setDX(VELOCITY_NONE);
                }

            } else if (getDX() < 0) {
                if (getCol() <= getTarget().getCol()) {
                    setCol(getTarget().getCol());
                    setDX(VELOCITY_NONE);
                }
            }

            //check vertical velocity
            if (getDY() > 0) {
                if (getRow() >= getTarget().getRow()) {
                    setRow(getTarget().getRow());
                    setDY(VELOCITY_NONE);
                }
            } else if (getDY() < 0) {
                if (getRow() <= getTarget().getRow()) {
                    setRow(getTarget().getRow());
                    setDY(VELOCITY_NONE);
                }
            }
        }
    }

    /**
     * Assign the block value
     * @param value The desired value 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, etc...
     */
    protected void setValue(int value) {
        this.value = value;
    }

    /**
     * Retrieve the block value
     * @return The assigned block value 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, etc...
     */
    protected int getValue() {
        return this.value;
    }

    /**
     * Assign the column target
     * @param col The desired location for our block
     */
    protected void setColTarget(double col) {
        this.target.setCol(col);
    }

    /**
     * Assign the row target
     * @param row The desired location for our block
     */
    protected void setRowTarget(double row) {
        this.target.setRow(row);
    }

    /**
     * Do we have our target location
     * @return true if the target location matches our own, false otherwise
     */
    protected boolean hasTarget() {
        return getTarget().hasLocation(this);
    }

    /**
     * Get the target
     * @return The location where we want the block to head at
     */
    protected Cell getTarget() {
        if (this.target == null)
            this.target = new Cell();

        return this.target;
    }

    /**
     * Assign the (x,y) rendering coordinates based on the current (col, row)
     */
    protected void updateCoordinates() {
        updateCoordinates(this);
    }

    /**
     * Assign the (x,y) rendering coordinates of an object
     * @param block The object we want to update the render coordinates, preferably a Block
     */
    protected static void updateCoordinates(final Entity block) {
        block.setX(START_X + (block.getCol() * (double)(ANIMATION_DIMENSIONS + BORDER_THICKNESS)));
        block.setY(START_Y + (block.getRow() * (double)(ANIMATION_DIMENSIONS + BORDER_THICKNESS)));
    }
}
