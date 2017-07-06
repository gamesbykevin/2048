package com.gamesbykevin.a2048.board;

import org.junit.Test;

import static com.gamesbykevin.a2048.opengl.OpenGLSurfaceView.FPS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Kevin on 7/4/2017.
 */

public class BoardTest extends Board {

    @Test
    public void BoardTest() {

        //testing the constructors
        Board board = new Board(DEFAULT_COLUMNS, DEFAULT_ROWS);
        board = new Board();
    }

    @Test
    public void setColsTest() {

        //create default size board
        Board board = new Board();

        //assume default value is set
        assertTrue(board.getCols() == DEFAULT_COLUMNS);

        //assign total columns
        int cols = 8;

        //create new instance with columns
        board = new Board(cols, 1);

        //assume values match
        assertTrue(board.getCols() == cols);

        //change the total columns
        cols = 21;

        //update the board size
        board.setCols(cols);

        //assume it matches
        assertTrue(board.getCols() == cols);
    }

    @Test
    public void setRowsTest() {

        //create default size board
        Board board = new Board();

        //assume default value is set
        assertTrue(board.getRows() == DEFAULT_ROWS);

        //assign total rows
        int rows = 8;

        //create new instance with rows
        board = new Board(1, rows);

        //assume values match
        assertTrue(board.getRows() == rows);

        //change the total rows
        rows = 21;

        //update the board size
        board.setRows(rows);

        //assume it matches
        assertTrue(board.getRows() == rows);
    }

    @Test
    public void resetTest() {

        //create new instance
        Board board = new Board();

        //assign the score
        board.setScore(10000000);

        //now reset the board
        board.reset();

        //assert 2 default spawning blocks
        assertTrue(board.getBlocks().size() == 2);

        //assume the score has been reset
        assertTrue(board.getScore() == 0);
    }

    @Test
    public void setScoreTest() {

        //create new instance
        Board board = new Board();

        //assume score is 0 to begin
        assertTrue(board.getScore() == 0);

        //assign many scores
        for (int score = 0; score < 100; score++) {

            //assign score
            board.setScore(score);

            //make sure the value matches
            assertTrue(board.getScore() == score);
        }
    }

    @Test
    public void getBlockTest() {

        //create new instance
        Board board = new Board();

        //clear all blocks
        board.getBlocks().clear();

        //get block that doesn't exist
        Block block = board.getBlock(0,0);

        //assume block is null
        assertNull(block);

        int col = 1;
        int row = 1;

        //add block at location
        board.addBlock(col, row, 1);

        //get block at that location
        block = board.getBlock(col, row);

        //assume not null
        assertNotNull(block);

        //get block at a different position
        block = board.getBlock(col + 1, row + 1);

        //assume block is not found
        assertNull(block);

        //assume there is a block everywhere
        for (col = 0; col < board.getCols(); col++) {
            for (row = 0; row < board.getRows(); row++) {

                //assume there is a block in every position
                assertNotNull(board.getBlock(col, row));
            }
        }

        //remove all blocks
        board.getBlocks().clear();

        //assume there isn't a block anywhere
        for (col = 0; col < board.getCols(); col++) {
            for (row = 0; row < board.getRows(); row++) {

                //assume there isn't a block in every position
                assertNull(board.getBlock(col, row));
            }
        }
    }

    @Test
    public void spawnTest() {
        //create new instance
        Board board = new Board();

        //clear all blocks
        board.getBlocks().clear();

        //assume there are no blocks
        assertTrue(board.getBlocks().isEmpty());

        //spawn new blocks
        board.spawn();

        //since board was previously empty, 2 blocks will be spawned
        assertTrue(board.getBlocks().size() == 2);

        //spawn a new block
        board.spawn();

        //now assume we have 3 blocks
        assertTrue(board.getBlocks().size() == 3);
    }

    @Test
    public void addBlockTest() {

        //create new instance
        Board board = new Board(DEFAULT_COLUMNS, DEFAULT_ROWS);

        //remove all blocks
        board.getBlocks().clear();

        //assume blocks are empty
        assertTrue(board.getBlocks().isEmpty());

        //add a block to every position
        for (int col = 0; col < board.getCols(); col++) {
            for (int row = 0; row < board.getRows(); row++) {
                board.addBlock(col, row, 2);
            }
        }

        //assume every block has been added
        assertTrue(board.getBlocks().size() == (board.getCols() * board.getRows()));
    }

    @Test
    public void hasBlockTest() {

        //create new instance
        Board board = new Board(DEFAULT_COLUMNS, DEFAULT_ROWS);

        //remove all blocks
        board.getBlocks().clear();

        //add a block to every position
        for (int col = 0; col < board.getCols(); col++) {
            for (int row = 0; row < board.getRows(); row++) {
                board.addBlock(col, row, 2);
            }
        }

        //assume we have a block everywhere
        for (int col = 0; col < board.getCols(); col++) {
            for (int row = 0; row < board.getRows(); row++) {

                //assume we have a block here
                assertTrue(board.hasBlock(col, row));
            }
        }

        //remove all blocks
        board.getBlocks().clear();

        //assume all blocks are gone
        for (int col = 0; col < board.getCols(); col++) {
            for (int row = 0; row < board.getRows(); row++) {

                //assume we don't have a block here
                assertFalse(board.hasBlock(col, row));
            }
        }
    }

    @Test
    public void updateTest() {

        //create new instance
        Board board = new Board();

        //call update many times to assume no error
        for (int i = 0; i < (FPS * 10); i++) {
            board.update();
        }
    }

    @Test
    public void isGameOverTest() {

        //create new instance
        Board board = new Board();

        //assume the game isn't over
        assertFalse(board.isGameover());

        //remove all blocks
        board.getBlocks().clear();

        //default start value
        int value = 100000;

        //add a block to every position with a different value
        for (int col = 0; col < board.getCols(); col++) {
            for (int row = 0; row < board.getRows(); row++) {

                //change the value so no blocks are the same
                value++;

                //add block
                board.addBlock(col, row, value);
            }
        }

        //remove 1 block
        board.getBlocks().remove(0);

        //spawn a new block
        board.spawn();

        //assume the game is now over
        assertTrue(board.isGameover());
    }

    @Test
    public void hasTargetTest() {

        //create new instance
        Board board = new Board();

        //assume all blocks are at their target
        assertTrue(board.hasTarget());

        for (int i = 0; i < board.getBlocks().size(); i++) {
            Block block = board.getBlocks().get(i);

            //assign a different target
            block.setColTarget(block.getCol() + 1);
            block.setRowTarget(block.getRow() + 1);

            //assume the block is not at its target
            assertFalse(board.hasTarget());
        }

        //keep updating until all blocks arrive at their target
        while (!board.hasTarget()) {
            board.update();
        }

        //assume all blocks are at their target
        assertTrue(board.hasTarget());

        for (int i = 0; i < board.getBlocks().size(); i++) {
            Block block = board.getBlocks().get(i);

            //assign the same target
            block.setColTarget(block.getCol());
            block.setRowTarget(block.getRow());
        }

        //assume all blocks are at their target
        assertTrue(board.hasTarget());
    }

    @Test
    public void assignTexturesTest() {

    }

    @Test
    public void drawTest() {

    }
}