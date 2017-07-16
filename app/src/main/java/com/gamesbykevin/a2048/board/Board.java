package com.gamesbykevin.a2048.board;

/**
 * Created by Kevin on 5/26/2017.
 */

import com.gamesbykevin.a2048.base.EntityItem;

import static com.gamesbykevin.a2048.board.BoardHelper.SPAWN_VALUE_1;
import static com.gamesbykevin.a2048.board.BoardHelper.SPAWN_VALUE_2;
import static com.gamesbykevin.a2048.game.GameManager.GAME_OVER;
import static com.gamesbykevin.a2048.level.Stats.MODE;
import static com.gamesbykevin.a2048.opengl.OpenGLSurfaceView.DIRTY_FLAG;

import com.gamesbykevin.a2048.game.GameManagerHelper;
import com.gamesbykevin.androidframework.base.Cell;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.GameActivity.getRandomObject;
import static com.gamesbykevin.a2048.board.Block.BLOCK_DIMENSIONS;
import static com.gamesbykevin.a2048.board.Block.VALUES;
import static com.gamesbykevin.a2048.opengl.OpenGLSurfaceView.FRAME_DURATION;

/**
 * The game board where the action takes place
 */
public class Board {

    //keep track of the list of blocks on the board
    private List<Block> blocks;

    //this entity will be used for all the block rendering
    private Block block;

    //the background of each block
    private EntityItem background;

    /**
     * The pixel thickness of the border outline
     */
    protected static final int BORDER_THICKNESS = 3;

    /**
     * Number of columns on our board
     */
    protected static final int DEFAULT_COLUMNS = 4;

    /**
     * Number of rows on our board
     */
    protected static final int DEFAULT_ROWS = 4;

    //the amount of space on each side of the board
    protected static int PADDING = 10;

    //what is our score
    private int score = 0;

    //how long have we been playing
    private long duration = 0;

    //the size of the board
    private int cols, rows;

    /**
     * Default constructor
     */
    public Board() {
        this(DEFAULT_COLUMNS, DEFAULT_ROWS);
    }

    /**
     * Create a board with the specified dimensions
     */
    public Board(final int cols, final int rows) {

        //set the size of the board
        setCols(cols);
        setRows(rows);

        //figure out the pixel dimensions, etc...
        BoardHelper.assignLogistics(this);

        //create new array list to contain all the blocks
        this.blocks = new ArrayList<>();

        //create a new entity
        this.block = new Block();

        //create the background and set default values
        this.background = new EntityItem();
        this.background.setWidth(BLOCK_DIMENSIONS);
        this.background.setHeight(BLOCK_DIMENSIONS);

        //reset the board
        reset();
    }

    /**
     * Reset the board and spawn blocks etc...
     */
    public void reset() {

        //flag render
        DIRTY_FLAG = true;

        //clear our blocks
        getBlocks().clear();

        //spawn the default blocks
        spawn();

        //reset the score as well
        setScore(0);

        //reset the duration
        setDuration(0);
    }

    /**
     * Assign the columns
     * @param cols The desired number of columns
     */
    public void setCols(int cols) {
        this.cols = cols;
    }

    /**
     * Assign the rows
     * @param rows The desired number of rows
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Get the columns
     * @return The total number of columns on the board
     */
    public int getCols() {
        return this.cols;
    }

    /**
     * Get the rows
     * @return The total number of rows on the board
     */
    public int getRows() {
        return this.rows;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(final long duration) {
        this.duration = duration;
    }

    /**
     * Get the score
     * @return Our point score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Assign the score
     * @param score The desired score
     */
    public void setScore(int score) {

        //flag since score is changed
        if (this.score != score)
            DIRTY_FLAG = true;

        //assign score
        this.score = score;
    }

    /**
     * Get the blocks
     * @return The blocks in play on the board
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
    public Block getBlock(int col, int row) {

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
        for (int col = 0; col < getCols(); col++) {
            for (int row = 0; row < getRows(); row++) {
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
            addBlock(available.get(index), getRandomObject().nextBoolean() ? SPAWN_VALUE_1 : SPAWN_VALUE_2);

            //remove from our list of available spawn points
            available.remove(index);

            //choose a new random spot
            index = getRandomObject().nextInt(available.size());

            //spawn one block at a new random location again
            addBlock(available.get(index), getRandomObject().nextBoolean() ? SPAWN_VALUE_1 : SPAWN_VALUE_2);

        } else {

            //spawn one block at this random location
            addBlock(available.get(index), getRandomObject().nextBoolean() ? SPAWN_VALUE_1 : SPAWN_VALUE_2);
        }
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
     * Add a block at the specified location with the specified value
     * @param col Column location
     * @param row Row location
     * @param value Desired value of the new block
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

        //if the game is not over keep track of duration
        if (!GAME_OVER) {

            //each update the time added will be for each frame duration
            setDuration(getDuration() + FRAME_DURATION);

            //flag render
            DIRTY_FLAG = true;
        }

        //are all of the blocks at their target
        final boolean hasTarget = hasTarget();

        //update all the blocks
        for (int i = 0; i < getBlocks().size(); i++) {
            getBlocks().get(i).update();
        }

        //if we weren't at the target but am now, we can update the merged blocks
        if (!hasTarget && hasTarget()) {
            BoardHelper.updateMerged(this);

            //flag render
            DIRTY_FLAG = true;
        }
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

    public boolean hasValue(final int value) {

        //check every block
        for (int i = 0; i < getBlocks().size(); i++) {

            //if the values are the same, we have a match
            if (getBlocks().get(i).getValue() == value)
                return true;
        }

        //we did not find a block with the specified value
        return false;
    }

    public boolean hasMove() {

        //check every available place on the board
        for (int col = 0; col < getCols(); col++) {
            for (int row = 0; row < getRows(); row++) {

                //get the current block
                Block block = getBlock(col, row);

                //if the block doesn't exist we can still move the blocks
                if (block == null)
                    return true;

                //check for blocks in all 4 directions
                Block east = getBlock(col + 1, row);
                Block west = getBlock(col - 1, row);
                Block north = getBlock(col, row - 1);
                Block south = getBlock(col, row + 1);

                //if the neighbor block exists and has the same value, we still have a move
                if (east != null && block.getValue() == east.getValue())
                    return true;
                if (west != null && block.getValue() == west.getValue())
                    return true;
                if (north != null && block.getValue() == north.getValue())
                    return true;
                if (south != null && block.getValue() == south.getValue())
                    return true;
            }
        }

        //we couldn't find any moves
        return false;
    }


    /**
     * Assign the appropriate texture for each block etc...
     * @param textures Our array of texture id's for each texture
     */
    public void assignTextures(int[] textures) {

        //assign to background
        this.background.setTextureId(textures[0]);

        //check every block
        for (int col = 0; col < getCols(); col++) {
            for (int row = 0; row < getRows(); row++) {

                //get the current block
                Block block = getBlock(col, row);

                //if the block doesn't exist here, skip to the next
                if (block == null)
                    continue;

                //assign the appropriate texture
                block.assignTextures(textures);
            }
        }
    }

    /**
     * Render the game objects
     * @param gl Surface used for rendering pixels
     */
    public void draw(GL10 gl) throws Exception {

        //render the border
        //?

        //render the background tiles
        for (int col = 0; col < getCols(); col++) {
            for (int row = 0; row < getRows(); row++) {

                //assign the current location
                this.background.setCol(col);
                this.background.setRow(row);

                //assign dimensions
                this.background.setWidth(BLOCK_DIMENSIONS);
                this.background.setHeight(BLOCK_DIMENSIONS);

                //calculate x,y render coordinates
                Block.updateCoordinates(this.background);

                //render the background
                this.background.render(gl);
            }
        }

        //render all the blocks that aren't expanding/collapsing
        for (int i = 0; i < getBlocks().size(); i++) {

            //get the current block
            final Block tmp = getBlocks().get(i);

            //skip blocks that are in transition
            if (tmp.hasExpand() || tmp.hasCollapse())
                continue;

            //update block attributes
            block.updateBlock(tmp);

            //render the block
            block.render(gl);
        }

        //now render all the blocks that are expanding/collapsing so they appear on the top
        for (int i = 0; i < getBlocks().size(); i++) {

            final Block tmp = getBlocks().get(i);

            //skip blocks that aren't in transition
            if (!tmp.hasExpand() && !tmp.hasCollapse())
                continue;

            //update block attributes
            block.updateBlock(tmp);

            //render the block
            block.render(gl);
        }
    }
}