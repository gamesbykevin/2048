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
    protected static final int START_X = 8;

    /**
     * The starting coordinate of the north-west block
     */
    protected static final int START_Y = 100;

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
    protected Cell getTarget() {
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
     *
     * @param block
     */
    protected static void updateCoordinates(final Entity block) {
        block.setX(START_X + (block.getCol() * (double)(ANIMATION_DIMENSIONS + BORDER_THICKNESS)));
        block.setY(START_Y + (block.getRow() * (double)(ANIMATION_DIMENSIONS + BORDER_THICKNESS)));
    }
}
