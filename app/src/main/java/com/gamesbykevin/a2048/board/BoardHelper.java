package com.gamesbykevin.a2048.board;

import java.util.List;

/**
 * Created by Kevin on 5/31/2017.
 */

public class BoardHelper {

    /**
     * Merge the blocks towards the west
     * @param board Board containing our blocks to merge
     */
    public static void mergeWest(Board board) {

        //check every row
        for (int row = 0; row < Board.ROWS; row++) {

            //the previous value block
            int previousValue = -1;

            //the west column of the last block that we can merge with
            int columnMerge = 0;

            //if we can't merge what is the next block
            int columnNoMerge = 0;

            //check each column in the row one by one starting in the west
            for (int col = 0; col < Board.COLUMNS; col++) {

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
    public static void mergeEast(Board board) {

        //check every row
        for (int row = 0; row < Board.ROWS; row++) {

            //the previous value block
            int previousValue = -1;

            //the east column of the last block that we can merge with
            int columnMerge = Board.COLUMNS - 1;

            //if we can't merge what is the next block
            int columnNoMerge = Board.COLUMNS - 1;

            //check each column in the row one by one starting in the east
            for (int col = Board.COLUMNS - 1; col >= 0; col--) {

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
    public static void mergeNorth(Board board) {

        //check each column
        for (int col = 0; col < Board.COLUMNS; col++) {

            //the previous value block
            int previousValue = -1;

            //the north row of the last block that we can merge with
            int rowMerge = 0;

            //if we can't merge what is the next block
            int rowNoMerge = 0;

            //check each row one by one starting in the north
            for (int row = 0; row < Board.ROWS; row++) {

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
    public static void mergeSouth(Board board) {

        //check each column
        for (int col = 0; col < Board.COLUMNS; col++) {

            //the previous value block
            int previousValue = -1;

            //the south row of the last block that we can merge with
            int rowMerge = Board.ROWS - 1;

            //if we can't merge what is the next block
            int rowNoMerge = Board.ROWS - 1;

            //check each row one by one starting in the south
            for (int row = Board.ROWS - 1; row >= 0; row--) {

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

        List<Block> blocks = board.getBlocks();

        for (int i = 0; i < blocks.size(); i++) {

            //get the current block
            final Block block = blocks.get(i);

            //check if any other blocks are at the same location so we can merge and update the score
            for (int j = i + 1; j < blocks.size(); j++) {

                //check the potential match
                final Block tmp = blocks.get(j);

                //we can't check the same block
                if (block.getId().equals(tmp.getId()))
                    continue;

                //if both of these blocks are at the same location we can merge
                if (block.hasLocation(tmp)) {

                    //update block value
                    block.setValue(block.getValue() + tmp.getValue());

                    //add to our total score
                    board.setScore(board.getScore() + block.getValue());

                    //remove the extra block
                    blocks.remove(j);

                    //keep the index inbounds
                    j--;
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
        for (int col = 0; col < Board.COLUMNS; col++) {
            for (int row = 0; row < Board.ROWS; row++) {

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
}