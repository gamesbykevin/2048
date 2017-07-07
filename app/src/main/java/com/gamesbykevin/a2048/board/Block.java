package com.gamesbykevin.a2048.board;

import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.base.EntityItem;
import com.gamesbykevin.a2048.opengl.OpenGLSurfaceView;
import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.base.Entity;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.board.Board.BORDER_THICKNESS;

/**
 * Created by Kevin on 5/29/2017.
 */

public class Block extends EntityItem {

    //what is the value of this block
    private int value = 0;

    //where are we targeting to go?
    private Cell target;

    /**
     * Size of animation on sprite sheet
     */
    public static final int ANIMATION_DIMENSIONS = 90;

    /**
     * How fast do the blocks move?
     */
    protected static final float VELOCITY = 0.25f;

    /**
     * No velocity
     */
    protected static final float VELOCITY_NONE = 0.0f;

    //the size of each block on this board
    public static int BLOCK_DIMENSIONS;

    /**
     * The starting coordinate of the north-west block
     */
    public static int START_X = 48;

    /**
     * The starting coordinate of the north-west block
     */
    public static int START_Y = 125;

    /**
     * The max dimensions allowed for this block
     */
    public static int DIMENSIONS_MAX;

    /**
     * How fast do we expand/collapse
     */
    public static float DIMENSION_CHANGE_VELOCITY;

    //did we already expand and collapse the block
    private boolean expand, collapse;

    //the different values for the blocks
    public static final int[] VALUES = {0, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192};

    /**
     * Default constructor
     */
    protected Block() {

        //call parent constructor
        super();

        //create new target
        this.target = new Cell();

        //assign the size at which we will render the  block
        setWidth(BLOCK_DIMENSIONS);
        setHeight(BLOCK_DIMENSIONS);

        //expand at first
        setExpand(true);

        //we don't need to collapse yet
        setCollapse(false);
    }

    /**
     * Update the block location based on (x,y) velocity etc...
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
        } else {

            //if expand, let's update the block size
            if (hasExpand()) {

                //if collapsing
                if (hasCollapse()) {

                    //shrink the block size
                    setWidth(getWidth() - DIMENSION_CHANGE_VELOCITY);
                    setHeight(getHeight() - DIMENSION_CHANGE_VELOCITY);

                    //if we hit our collapse limit, we are done
                    if (getWidth() <= BLOCK_DIMENSIONS || getHeight() <= BLOCK_DIMENSIONS) {

                        //set the block size
                        setWidth(BLOCK_DIMENSIONS);
                        setHeight(BLOCK_DIMENSIONS);

                        //stop collapsing
                        setCollapse(false);

                        //stop expanding
                        setExpand(false);
                    }

                } else {

                    //if not collapsing expand the block size
                    setWidth(getWidth() + DIMENSION_CHANGE_VELOCITY);
                    setHeight(getHeight() + DIMENSION_CHANGE_VELOCITY);

                    //if we hit our expand limit, start collapsing
                    if (getWidth() >= DIMENSIONS_MAX || getHeight() >= DIMENSIONS_MAX) {

                        //set the block size
                        setWidth(DIMENSIONS_MAX);
                        setHeight(DIMENSIONS_MAX);

                        //start collapsing
                        setCollapse(true);
                    }
                }
            }
        }
    }

    /**
     * Assign the block to expand
     * @param expand true if we want the block to expand, false otherwise
     */
    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    /**
     * Assign the block to collapse
     * @param collapse true if we want the block to collapse, false otherwise
     */
    public void setCollapse(boolean collapse) {
        this.collapse = collapse;
    }

    /**
     * Is the block able to expand?
     * @return true = yes, false = no
     */
    public boolean hasExpand() {
        return this.expand;
    }

    /**
     * Is the block able to collapse?
     * @return true = yes, false = no
     */
    public boolean hasCollapse() {
        return this.collapse;
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
     * Do we have our target location?
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
        block.setX(START_X + (block.getCol() * (double)(BLOCK_DIMENSIONS + BORDER_THICKNESS)));
        block.setY(START_Y + (block.getRow() * (double)(BLOCK_DIMENSIONS + BORDER_THICKNESS)));
    }

    /**
     * Update our block instance
     * @param tmp The block containing the attributes we want to assign
     */
    protected void updateBlock(Block tmp) {

        //update the entity position (col, row)
        setCol(tmp);
        setRow(tmp);

        //update the dimensions as well
        setWidth(tmp);
        setHeight(tmp);

        //calculate the x,y render coordinates
        updateCoordinates();

        //match the value that we are rendering
        setValue(tmp.getValue());

        //assign the correct animation
        setTextureId(tmp.getTextureId());
    }

    /**
     * Make sure the block is assigned the correct texture for rendering
     * @param textures Array of texture ids resembling each image to be rendered
     */
    public void assignTextures(final int[] textures) {


        //check each value until we find our texture
        for (int i = 0; i < VALUES.length; i++) {

            //if we have a match
            if (VALUES[i] == getValue()) {

                //assign the texture
                setTextureId(textures[i]);

                //we are done
                return;
            }
        }

        //we did not find our value and are not able to assign a texture
        throw new RuntimeException("Value not handled here :" + getValue());
    }

    @Override
    public void render(GL10 gl) {

        //store the coordinate
        final double x = getX();
        final double y = getY();

        //calculate the middle location
        double middleX = getX() + (BLOCK_DIMENSIONS / 2);
        double middleY = getY() + (BLOCK_DIMENSIONS / 2);

        //offset the coordinate
        super.setX(middleX - (getWidth() / 2));
        super.setY(middleY - (getHeight() / 2));

        //render the block
        super.render(gl);

        //restore coordinates
        super.setX(x);
        super.setY(y);
    }
}