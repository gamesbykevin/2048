package com.gamesbykevin.a2048.board;

/**
 * Created by Kevin on 5/26/2017.
 */

import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.base.EntityItem;
import com.gamesbykevin.a2048.opengl.OpenGLSurfaceView;
import com.gamesbykevin.androidframework.base.Cell;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.a2048.GameActivity.getRandomObject;
import static com.gamesbykevin.a2048.board.Block.BLOCK_DIMENSIONS;

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
    protected static final int BORDER_THICKNESS = 5;

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

    //the size of the board
    private int cols, rows;

    //is the game over?
    private boolean gameover = false;

    /**
     * Default constructor
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

        //create some default blocks
        spawn();
    }

    /**
     * Assign the columns
     * @param cols The total number of columns in the puzzle
     */
    public void setCols(int cols) {
        this.cols = cols;
    }

    /**
     *
     * @param rows
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     *
     * @return
     */
    public int getCols() {
        return this.cols;
    }

    /**
     *
     * @return
     */
    public int getRows() {
        return this.rows;
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

        //notify if game over
        if (isGameover())
            MainActivity.logEvent("GAME OVER!!!!!!!!!!!");
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