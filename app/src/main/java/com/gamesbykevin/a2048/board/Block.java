package com.gamesbykevin.a2048.board;

import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.base.Entity;

import static com.gamesbykevin.a2048.board.Board.BORDER_THICKNESS;

/**
 * Created by Kevin on 5/29/2017.
 */

public class Block extends Entity {

    //what is the value of this block
    private int value = 0;

    //where are we targeting to go
    private Cell target;

    /**
     * Size of animation on sprite sheet
     */
    protected static final int ANIMATION_DIMENSIONS = 90;

    /**
     * The starting coordinate of the north-west block
     */
    public static final int START_X = 48;

    /**
     * The starting coordinate of the north-west block
     */
    public static final int START_Y = 125;

    /**
     * List of possible animations for the block
     */
    protected enum AnimationKey {
        Empty(0),
        Two(90),
        Four(180),
        Eight(270),
        Sixteen(360),
        ThirtyTwo(450),
        SixtyFour(540),
        OneHundredTwentyEight(630),
        TwoHundredFiftySix(720),
        FiveHundredTwelve(810),
        OneThousandTwentyFour(900),
        TwoThousandFortyEight(990),
        FourThousandNinetySix(1080),
        EightThousandOneHundredNinetyTwo(1170);

        //the x-coordinate where the animation takes place
        private int x;

        private AnimationKey(int x) {
            this.x = x;
        }

        /**
         *
         * @return
         */
        protected int getX() {
            return this.x;
        }
    }

    /**
     * How fast do the blocks move?
     */
    protected static final float VELOCITY = 0.5f;

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
     *
     * @param value
     */
    protected void setValue(int value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    protected int getValue() {
        return this.value;
    }

    /**
     *
     * @param col
     */
    protected void setColTarget(double col) {
        this.target.setCol(col);
    }

    /**
     *
     * @param row
     */
    protected void setRowTarget(double row) {
        this.target.setRow(row);
    }

    /**
     *
     * @return
     */
    protected boolean hasTarget() {
        return getTarget().hasLocation(this);
    }

    /**
     *
     * @return
     */
    protected Cell getTarget() {
        if (this.target == null)
            this.target = new Cell();

        return this.target;
    }

    /**
     *
     * @throws Exception
     */
    protected void assignAnimation() throws Exception {

        //assign animation based on the value
        switch (getValue()) {

           case 0:
               getSpritesheet().setKey(AnimationKey.Empty);
               break;

           case 2:
               getSpritesheet().setKey(AnimationKey.Two);
               break;

           case 4:
               getSpritesheet().setKey(AnimationKey.Four);
               break;

           case 8:
               getSpritesheet().setKey(AnimationKey.Eight);
               break;

           case 16:
               getSpritesheet().setKey(AnimationKey.Sixteen);
               break;

           case 32:
               getSpritesheet().setKey(AnimationKey.ThirtyTwo);
               break;

           case 64:
               getSpritesheet().setKey(AnimationKey.SixtyFour);
               break;

           case 128:
               getSpritesheet().setKey(AnimationKey.OneHundredTwentyEight);
               break;

           case 256:
               getSpritesheet().setKey(AnimationKey.TwoHundredFiftySix);
               break;

           case 512:
               getSpritesheet().setKey(AnimationKey.FiveHundredTwelve);
               break;

           case 1024:
               getSpritesheet().setKey(AnimationKey.OneThousandTwentyFour);
               break;

           case 2048:
               getSpritesheet().setKey(AnimationKey.TwoThousandFortyEight);
               break;

           case 4096:
               getSpritesheet().setKey(AnimationKey.FourThousandNinetySix);
               break;

           case 8192:
               getSpritesheet().setKey(AnimationKey.EightThousandOneHundredNinetyTwo);
               break;

            default:
                throw new Exception("The current value is not assigned: " + getValue());
        }
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
