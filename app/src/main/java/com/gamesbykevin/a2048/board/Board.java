package com.gamesbykevin.a2048.board;

/**
 * Created by Kevin on 5/26/2017.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Cell;

import com.gamesbykevin.a2048.board.Block.AnimationKey;
import com.gamesbykevin.androidframework.base.Entity;

import java.util.ArrayList;
import java.util.List;

import static com.gamesbykevin.a2048.board.Block.ANIMATION_DIMENSIONS;
import static com.gamesbykevin.a2048.board.Block.START_X;
import static com.gamesbykevin.a2048.board.Block.START_Y;

/**
 * The game board where the action takes place
 */
public class Board {

    //keep track of the list of blocks on the board
    private List<Block> blocks;

    //this entity will be used for all the block rendering
    private Block block;

    //the border of the board
    private Entity border;

    //the background of each block
    private Entity background;

    /**
     * Dimensions of the border
     */
    private static final int BORDER_DIMENSIONS = 102;

    /**
     * The thickness of the border outline
     */
    protected static final int BORDER_THICKNESS = 6;

    /**
     * Default constructor
     */
    public Board(final Bitmap spriteSheet, final Bitmap borderImage) {

        //create new array list to contain all the blocks
        this.blocks = new ArrayList<>();

        //create a new entity
        this.block = new Block();

        //map out all of the animations so we can use for rendering
        for (AnimationKey key : AnimationKey.values()) {

            //create animation at location x-coordinate
            Animation anim = new Animation(spriteSheet, key.getX(), 0, ANIMATION_DIMENSIONS, ANIMATION_DIMENSIONS);

            //add the animation to our sprite sheet for our entity
            this.block.getSpritesheet().add(key, anim);
        }

        //create border and set default values
        this.border = new Entity();
        this.border.setWidth(BORDER_DIMENSIONS);
        this.border.setHeight(BORDER_DIMENSIONS);
        this.border.getSpritesheet().add("Default", new Animation(borderImage));

        //create the background and set default values
        this.background = new Entity();
        this.background.setWidth(ANIMATION_DIMENSIONS);
        this.background.setHeight(ANIMATION_DIMENSIONS);
        this.background.getSpritesheet().add("Default", new Animation(spriteSheet, 0, 0, ANIMATION_DIMENSIONS, ANIMATION_DIMENSIONS));

        addBlock(0,0);
        addBlock(1,1);
        addBlock(0,4);
    }

    /**
     *
     * @param col
     * @param row
     */
    public void addBlock(int col, int row) {
       Block block = new Block();

        //assign the location
        block.setCol(col);
        block.setRow(row);

        block.setValue(2);

        //add block to the list
        this.blocks.add(block);
    }

    /**
     *
     * @param col
     * @param row
     * @return
     */
    public boolean hasBlock(final int col, final int row) {

        //check all the blocks
        for (int i = 0; i < blocks.size(); i++) {
            Cell cell = blocks.get(i);

            //if the column and row matches, return true
            if ((int)cell.getCol() == col && (int)cell.getRow() == row)
                return true;
        }

        //we didn't find a match
        return false;
    }

    /**
     * Update game objects
     */
    public void update() {

        //update all the blocks
        for (int i = 0; i < blocks.size(); i++) {

        }
    }

    /**
     * Render the game objects
     * @param canvas Surface used for rendering pixels
     */
    public void draw(Canvas canvas) throws Exception {

        //render the background first
        for (int col = 0; col < 5; col++) {
            for (int row = 0; row < 5; row++) {

                this.background.setCol(col);
                this.background.setRow(row);

                Block.updateCoordinates(this.background);

                this.background.render(canvas);
            }
        }

        //render all the blocks
        for (int i = 0; i < blocks.size(); i++) {

            final Block tmp = blocks.get(i);

            //update the entity position (col, row)
            block.setCol(tmp);
            block.setRow(tmp);

            //calculate the x,y render coordinates
            block.updateCoordinates();

            //match the value that we are rendering
            block.setValue(tmp.getValue());

            //assign the correct animation
            block.assignAnimation();

            //render the block
            block.render(canvas);
        }

        //render the borders on top of the blocks
        for (int col = 0; col < 5; col++) {
            for (int row = 0; row < 5; row++) {

                //assign the border coordinates
                border.setX((START_X - BORDER_THICKNESS) + (col * (double)(BORDER_DIMENSIONS - BORDER_THICKNESS)));
                border.setY((START_Y - BORDER_THICKNESS) + (row * (double)(BORDER_DIMENSIONS - BORDER_THICKNESS)));

                //render the border at the current location
                border.render(canvas);
            }
        }
    }
}