package com.gamesbykevin.a2048.board;

/**
 * Created by Kevin on 5/26/2017.
 */


import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.base.EntityItem;
import com.gamesbykevin.androidframework.base.Cell;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.board.Block.ANIMATION_DIMENSIONS;
import static com.gamesbykevin.a2048.board.Block.START_X;
import static com.gamesbykevin.a2048.board.Block.START_Y;
import static com.gamesbykevin.a2048.GameActivity.getRandomObject;

/**
 * The game board where the action takes place
 */
public class Board {

    //keep track of the list of blocks on the board
    private List<Block> blocks;

    //this entity will be used for all the block rendering
    private Block block;

    //the border of the board
    private EntityItem border;

    //the background of each block
    private EntityItem background;

    /**
     * Dimensions of the border
     */
    public static final int BORDER_DIMENSIONS = 102;

    /**
     * The thickness of the border outline
     */
    protected static final int BORDER_THICKNESS = 6;

    /**
     * Number of columns on our board
     */
    protected static final int COLUMNS = 4;

    /**
     * Number of rows on our board
     */
    protected static final int ROWS = 4;

    //what is our score
    private int score = 0;

    //is the game over
    private boolean gameover = false;

    /**
     * Default constructor
     */
    public Board() {//final Bitmap spriteSheet, final Bitmap borderImage) {

        //create new array list to contain all the blocks
        this.blocks = new ArrayList<>();

        //create a new entity
        this.block = new Block();

        //create border and set default values
        this.border = new EntityItem();
        this.border.setWidth(BORDER_DIMENSIONS);
        this.border.setHeight(BORDER_DIMENSIONS);

        //create the background and set default values
        this.background = new EntityItem();
        this.background.setWidth(ANIMATION_DIMENSIONS);
        this.background.setHeight(ANIMATION_DIMENSIONS);

        //create some default blocks
        spawn();
    }

    /**
     *
     * @return
     */
    public int getScore() {
        return this.score;
    }

    /**
     *
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     *
     * @return
     */
    public List<Block> getBlocks() {
        return this.blocks;
    }

    /**
     * Get a block at the desired location
     * @param col Column
     * @param row Row
     * @return The block at the specified location, if none found null is returned
     */
    protected Block getBlock(int col, int row) {

        //check each block in the list
        for (int i = 0; i < getBlocks().size(); i++) {
            if (getBlocks().get(i).hasLocation(col, row))
                return getBlocks().get(i);
        }

        return null;
    }

    /**
     * Create a block and place at a random place on the board that is available.<br>
     * If the board is empty we will spawn 2 blocks, else we spawn 1 block
     */
    public void spawn() {

        //create the list that will contain our available blocks
        List<Cell> available = new ArrayList<>();

        //check every location to see what is available
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {
                if (!hasBlock(col, row)) {
                    available.add(new Cell(col, row));
                }
            }
        }

        //pick a random index
        int index = getRandomObject().nextInt(available.size());

        //if the board is empty we will spawn 2 blocks
        if (this.blocks.isEmpty()) {

            //spawn one block at this random location
            addBlock(available.get(index), getRandomObject().nextBoolean() ? 2 : 4);

            //remove from our list of available spawn points
            available.remove(index);

            //choose a new random spot
            index = getRandomObject().nextInt(available.size());

            //spawn one block at a new random location again
            addBlock(available.get(index), getRandomObject().nextBoolean() ? 2 : 4);
        } else {

            //spawn one block at this random location
            addBlock(available.get(index), getRandomObject().nextBoolean() ? 2 : 4);
        }

        //now that the new block has  spawned, check if the game is over
        this.gameover = BoardHelper.isGameOver(this);
    }

    /**
     * Add a block to the board
     * @param cell The desired position
     * @param value The value of the block
     */
    public void addBlock(Cell cell, int value) {
       addBlock((int)cell.getCol(), (int)cell.getRow(), value);
    }

    /**
     *
     * @param col
     * @param row
     * @param value
     */
    public void addBlock(int col, int row, int value) {
       Block block = new Block();

        //assign the location
        block.setCol(col);
        block.setRow(row);

        //make the current location the target as well
        block.setColTarget(col);
        block.setRowTarget(row);

        //assign the block value as well
        block.setValue(value);

        //add block to the list
        getBlocks().add(block);
    }

    /**
     * Do we have a block at this location?
     * @param col Column
     * @param row Row
     * @return true if a block exists at the specified space, false otherwise
     */
    public boolean hasBlock(final int col, final int row) {

        //check all the blocks
        for (int i = 0; i < getBlocks().size(); i++) {
            Cell cell = getBlocks().get(i);

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

        //are all of the blocks at their target
        final boolean hasTarget = hasTarget();

        //update all the blocks
        for (int i = 0; i < getBlocks().size(); i++) {
            getBlocks().get(i).update();
        }

        //if we weren't at the target but am now, we can update the merged blocks
        if (!hasTarget && hasTarget()) {
            BoardHelper.updateMerged(this);

            //how do we check to see if the game is over?
            if (BoardHelper.isGameOver(this)) {

                //flag game over true
                this.gameover = true;

                MainActivity.logEvent("GAME OVER!!!!!!!!!!!");
            }
        }
    }

    /**
     * Is the game over?
     * @return true = yes, otherwise false
     */
    public boolean isGameover() {
        return this.gameover;
    }

    /**
     * Have all the blocks completed expanding/collapsing
     * @return true if finished, false otherwise
     */
    public boolean hasCompletedChange() {

        //check every block
        for (int i = 0; i < getBlocks().size(); i++) {

            //if block is expanding, return false
            if (getBlocks().get(i).hasExpand())
                return false;

            //if block is collapsing, return false
            if (getBlocks().get(i).hasCollapse())
                return false;
        }

        //if all completed return true
        return true;
    }

    /**
     * Are we at our target
     * @return true if all blocks are at their target, false otherwise
     */
    public boolean hasTarget() {

        //check every block
        for (int i = 0; i < getBlocks().size(); i++) {

            //if one block doesn't have the target we can't continue
            if (!getBlocks().get(i).hasTarget())
                return false;
        }

        //all blocks are at their target we return true
        return true;
    }

    /**
     * Make sure each object on the board has the correct texture id
     * @param textures Array of texture ids for each image to be rendered
     */
    public void assignTextures(final int[] textures) {

        //assign texture id for the background
        this.background.setTextureId(textures[0]);

        //assign texture id for the border
        this.border.setTextureId(textures[textures.length - 1]);

        //assign texture id for each block
        for (int i = 0; i < getBlocks().size(); i++) {
            final Block tmp = getBlocks().get(i);

            switch (tmp.getValue()) {
                case 2:
                default:
                    tmp.setTextureId(textures[1]);
                    break;

                case 4:
                    tmp.setTextureId(textures[2]);
                    break;

                case 8:
                    tmp.setTextureId(textures[3]);
                    break;

                case 16:
                    tmp.setTextureId(textures[4]);
                    break;

                case 32:
                    tmp.setTextureId(textures[5]);
                    break;

                case 64:
                    tmp.setTextureId(textures[6]);
                    break;

                case 128:
                    tmp.setTextureId(textures[7]);
                    break;

                case 256:
                    tmp.setTextureId(textures[8]);
                    break;

                case 512:
                    tmp.setTextureId(textures[9]);
                    break;

                case 1024:
                    tmp.setTextureId(textures[10]);
                    break;

                case 2048:
                    tmp.setTextureId(textures[11]);
                    break;

                case 4096:
                    tmp.setTextureId(textures[12]);
                    break;

                case 8192:
                    tmp.setTextureId(textures[13]);
                    break;
            }
        }
    }

    /**
     * Render the game objects
     * @param gl Surface used for rendering pixels
     */
    public void draw(GL10 gl) throws Exception {

        //render the background tile first
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {

                this.background.setCol(col);
                this.background.setRow(row);

                Block.updateCoordinates(this.background);

                this.background.render(gl);
            }
        }

        //render all the blocks that aren't expanding/collapsing
        for (int i = 0; i < getBlocks().size(); i++) {

            final Block tmp = getBlocks().get(i);

            //skip blocks that are in transition
            if (tmp.hasExpand() || tmp.hasCollapse())
                continue;

            //update block attributes
            updateBlock(tmp);

            //render the block
            block.render(gl);
        }

        //render the borders on top of the blocks
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {

                //assign the border coordinates
                border.setX((START_X - BORDER_THICKNESS) + (col * (double) (BORDER_DIMENSIONS - BORDER_THICKNESS)));
                border.setY((START_Y - BORDER_THICKNESS) + (row * (double) (BORDER_DIMENSIONS - BORDER_THICKNESS)));

                //render the border at the current location
                border.render(gl);
            }
        }

        //now render all the blocks that are expanding/collapsing
        for (int i = 0; i < getBlocks().size(); i++) {

            final Block tmp = getBlocks().get(i);

            //skip blocks that aren't in transition
            if (!tmp.hasExpand() && !tmp.hasCollapse())
                continue;

            //update block attributes
            updateBlock(tmp);

            //render the block
            block.render(gl);
        }
    }

    /**
     * Update our block instance
     * @param tmp The block containing the attributes we want to assign
     */
    private void updateBlock(final Block tmp) {

        //update the entity position (col, row)
        block.setCol(tmp);
        block.setRow(tmp);

        //update the dimensions as well
        block.setWidth(tmp);
        block.setHeight(tmp);

        //calculate the x,y render coordinates
        block.updateCoordinates();

        //match the value that we are rendering
        block.setValue(tmp.getValue());

        //assign the correct animation
        block.setTextureId(tmp.getTextureId());
    }
}