package com.gamesbykevin.a2048.board;

import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.game.GameManager;
import com.gamesbykevin.a2048.opengl.OpenGLSurfaceView;

import java.util.List;

import static com.gamesbykevin.a2048.board.Block.BLOCK_DIMENSIONS;
import static com.gamesbykevin.a2048.board.Block.DIMENSIONS_MAX;
import static com.gamesbykevin.a2048.board.Block.DIMENSION_CHANGE_VELOCITY;
import static com.gamesbykevin.a2048.board.Block.START_X;
import static com.gamesbykevin.a2048.board.Block.START_Y;
import static com.gamesbykevin.a2048.board.Board.BORDER_THICKNESS;
import static com.gamesbykevin.a2048.board.Board.PADDING;
import static com.gamesbykevin.a2048.opengl.OpenGLSurfaceView.HEIGHT;
import static com.gamesbykevin.a2048.opengl.OpenGLSurfaceView.WIDTH;

/**
 * Created by Kevin on 5/31/2017.
 */

public class BoardHelper {

    /**
     * Merge the blocks on the board
     * @param board The board we are merging
     * @param merge The direction of the merge
     */
    public static void merge(Board board, GameManager.Merge merge) throws Exception {

        //merge the board accordingly
        switch (merge) {

            case North:
                mergeNorth(board);
                break;

            case South:
                mergeSouth(board);
                break;

            case West:
                mergeWest(board);
                break;

            case East:
                mergeEast(board);
                break;

            default:
                throw new Exception("Merge not handled here: " + merge.toString());
        }
    }

    /**
     * Merge the blocks towards the west
     * @param board Board containing our blocks to merge
     */
    private static void mergeWest(Board board) {

        //check every row
        for (int row = 0; row < board.getRows(); row++) {

            //the previous value block
            int previousValue = -1;

            //the west column of the last block that we can merge with
            int columnMerge = 0;

            //if we can't merge what is the next block
            int columnNoMerge = 0;

            //check each column in the row one by one starting in the west
            for (int col = 0; col < board.getCols(); col++) {

                //get the block at the current position
                Block block = board.getBlock(col, row);

                //if the block doesn't exist there is no need to continue
                if (block == null)
                    continue;

                //if the block value matches the previous we can merge
                if (block.getValue() == previousValue) {

                    //update the target of the current block
                    block.setColTarget(columnMerge);

                    //make sure previous value can't match since we just merged
                    previousValue = -1;

                    //since we can't merge this block, the next column to place is to the right
                    columnMerge = columnMerge + 1;

                    //also mark the non-merge column as the same
                    columnNoMerge = columnMerge;

                } else {

                    //update the target of the current block
                    block.setColTarget(columnNoMerge);

                    //store the previous value in case of match
                    previousValue = block.getValue();

                    //track this column in case we can merge the next block
                    columnMerge = columnNoMerge;

                    //in case we can't merge the next block track the non-merge column
                    columnNoMerge = columnMerge + 1;
                }
            }
        }
    }

    /**
     * Merge the blocks towards the east
     * @param board Board containing our blocks to merge
     */
    private static void mergeEast(Board board) {

        //check every row
        for (int row = 0; row < board.getRows(); row++) {

            //the previous value block
            int previousValue = -1;

            //the east column of the last block that we can merge with
            int columnMerge = board.getCols() - 1;

            //if we can't merge what is the next block
            int columnNoMerge = board.getCols() - 1;

            //check each column in the row one by one starting in the east
            for (int col = board.getCols() - 1; col >= 0; col--) {

                //get the block at the current position
                Block block = board.getBlock(col, row);

                //if the block doesn't exist there is no need to continue
                if (block == null)
                    continue;

                //if the block value matches the previous we can merge
                if (block.getValue() == previousValue) {

                    //update the target of the current block
                    block.setColTarget(columnMerge);

                    //make sure previous value can't match since we just merged
                    previousValue = -1;

                    //since we can't merge this block, the next column where we can
                    columnMerge = columnMerge - 1;

                    //also mark the non-merge column as the same
                    columnNoMerge = columnMerge;

                } else {

                    //update the target of the current block
                    block.setColTarget(columnNoMerge);

                    //store the previous value in case of match
                    previousValue = block.getValue();

                    //track this column in case we can merge the next block
                    columnMerge = columnNoMerge;

                    //in case we can't merge the next block track the non-merge column
                    columnNoMerge = columnMerge - 1;
                }
            }
        }
    }

    /**
     * Merge the blocks towards the north
     * @param board Board containing our blocks to merge
     */
    private static void mergeNorth(Board board) {

        //check each column
        for (int col = 0; col < board.getCols(); col++) {

            //the previous value block
            int previousValue = -1;

            //the north row of the last block that we can merge with
            int rowMerge = 0;

            //if we can't merge what is the next block
            int rowNoMerge = 0;

            //check each row one by one starting in the north
            for (int row = 0; row < board.getRows(); row++) {

                //get the block at the current position
                Block block = board.getBlock(col, row);

                //if the block doesn't exist there is no need to continue
                if (block == null)
                    continue;

                //if the block value matches the previous we can merge
                if (block.getValue() == previousValue) {

                    //update the target of the current block
                    block.setRowTarget(rowMerge);

                    //make sure previous value can't match since we just merged
                    previousValue = -1;

                    //since we can't merge this block, the next row where we can
                    rowMerge = rowMerge + 1;

                    //also mark the non-merge row as the same
                    rowNoMerge = rowMerge;

                } else {

                    //update the target of the current block
                    block.setRowTarget(rowNoMerge);

                    //store the previous value in case of match
                    previousValue = block.getValue();

                    //track this row in case we can merge the next block
                    rowMerge = rowNoMerge;

                    //in case we can't merge the next block track the non-merge row
                    rowNoMerge = rowMerge + 1;
                }
            }
        }
    }

    /**
     * Merge the blocks towards the south
     * @param board Board containing our blocks to merge
     */
    private static void mergeSouth(Board board) {

        //check each column
        for (int col = 0; col < board.getCols(); col++) {

            //the previous value block
            int previousValue = -1;

            //the south row of the last block that we can merge with
            int rowMerge = board.getRows() - 1;

            //if we can't merge what is the next block
            int rowNoMerge = board.getRows() - 1;

            //check each row one by one starting in the south
            for (int row = board.getRows() - 1; row >= 0; row--) {

                //get the block at the current position
                Block block = board.getBlock(col, row);

                //if the block doesn't exist there is no need to continue
                if (block == null)
                    continue;

                //if the block value matches the previous we can merge
                if (block.getValue() == previousValue) {

                    //update the target of the current block
                    block.setRowTarget(rowMerge);

                    //make sure previous value can't match since we just merged
                    previousValue = -1;

                    //since we can't merge this block, the next row where we can
                    rowMerge = rowMerge - 1;

                    //also mark the non-merge row as the same
                    rowNoMerge = rowMerge;

                } else {

                    //update the target of the current block
                    block.setRowTarget(rowNoMerge);

                    //store the previous value in case of match
                    previousValue = block.getValue();

                    //track this row in case we can merge the next block
                    rowMerge = rowNoMerge;

                    //in case we can't merge the next block track the non-merge row
                    rowNoMerge = rowMerge - 1;
                }
            }
        }
    }

    /**
     * Clean up the merged blocks on the board.<br>
     * If more than 1 block is at the same location/value it will be merged into 1 block and double the value
     * @param board The board containing the blocks we want to check
     */
    protected static void updateMerged(Board board) {

        //get the list of blocks from our board
        List<Block> blocks = board.getBlocks();

        for (int i = 0; i < blocks.size(); i++) {

            //get the current block
            final Block block = blocks.get(i);

            //check if any other blocks are at the same location so we can merge and update the score
            for (int j = 0; j < blocks.size(); j++) {

                //check the potential match
                final Block tmp = blocks.get(j);

                //we can't check the same block
                if (block.getId().equals(tmp.getId()))
                    continue;

                //if both of these blocks are at the same location we can merge
                if (block.hasLocation(tmp)) {

                    //both blocks have to have the same value or else they can't merge together
                    if (block.getValue() != tmp.getValue()) {

                        //for some reason we tried to merge these blocks
                        MainActivity.logEvent("Block 1: (" + block.getCol() + ", " + block.getRow() + ") " + block.getValue());
                        MainActivity.logEvent("Block 2: (" + tmp.getCol() + ", " + tmp.getRow() + ") " + tmp.getValue());

                        //skip to the next block
                        continue;
                    }

                    MainActivity.logEvent("Merged 1: (" + block.getCol() + ", " + block.getRow() + ") " + block.getValue());
                    MainActivity.logEvent("Merged 2: (" + tmp.getCol() + ", " + tmp.getRow() + ") " + tmp.getValue());

                    //update block value
                    block.setValue(block.getValue() + tmp.getValue());

                    //flag the block to expand
                    block.setExpand(true);

                    //add to our total score
                    board.setScore(board.getScore() + block.getValue());

                    //remove the extra block
                    blocks.remove(j);

                    //move back index
                    i--;

                    //since we can only merge two blocks at a time, exit the loop
                    break;
                }
            }
        }
    }

    /**
     *
     * @param board
     * @return
     */
    protected static boolean isGameOver(Board board) {

        //check every available place on the board
        for (int col = 0; col < board.getCols(); col++) {
            for (int row = 0; row < board.getRows(); row++) {

                //get the current block
                Block block = board.getBlock(col, row);

                //if the block doesn't exist we can still move the blocks and the game is not over
                if (block == null)
                    return false;

                //check for blocks in all 4 directions
                Block east = board.getBlock(col + 1, row);
                Block west = board.getBlock(col - 1, row);
                Block north = board.getBlock(col, row - 1);
                Block south = board.getBlock(col, row + 1);

                //if the neighbor block exists and has the same value, the game is not yet over
                if (east != null && block.getValue() == east.getValue())
                    return false;
                if (west != null && block.getValue() == west.getValue())
                    return false;
                if (north != null && block.getValue() == north.getValue())
                    return false;
                if (south != null && block.getValue() == south.getValue())
                    return false;
            }
        }

        //we couldn't find any moves, so the game is over
        return true;
    }

    /**
     * Assign the size, xy, etc... coordinates for the board
     */
    protected static void assignLogistics(Board board) {

        //amount of space we can render the board and elements
        int dimension = WIDTH - (PADDING * 2);

        //calculate the amount of space available after subtracting the borders
        dimension = dimension - ((board.getCols() + 1) * BORDER_THICKNESS);

        //amount of pixel space per block
        int remaining = (int)(dimension / board.getCols());

        //make sure the block size is an even number
        if (remaining % 2 != 0) {
            remaining--;
        }

        //lets make the dimensions a nice even number
        int goodSize = 1024;

        //continue to check until we found the ideal dimension
        while (goodSize > remaining) {
            goodSize -= 8;
        }

        //assign the dimensions of a single block
        BLOCK_DIMENSIONS = goodSize;

        int middleX = (int)(WIDTH  * .5);
        START_Y     = (int)(HEIGHT * .15);

        //the total width of the blocks
        int blockWidth = (board.getCols() * BLOCK_DIMENSIONS);

        //the total width of the borders
        int borderWidth = (board.getCols() + 1) * BORDER_THICKNESS;

        //now that we know the middle and block dimensions we can find the start x,y
        START_X = middleX - ((blockWidth + borderWidth) / 2);

        //determine the maximum dimensions before we collapse
        DIMENSIONS_MAX = (int)(BLOCK_DIMENSIONS * 1.5);

        //determine how fast we expand/collapse
        DIMENSION_CHANGE_VELOCITY = (BLOCK_DIMENSIONS / (OpenGLSurfaceView.FPS / 4));
    }
}