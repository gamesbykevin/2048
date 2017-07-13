package com.gamesbykevin.a2048.board;

import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.game.GameManager;
import com.gamesbykevin.a2048.game.GameManagerHelper.Mode;
import com.gamesbykevin.a2048.opengl.OpenGLSurfaceView;
import com.gamesbykevin.androidframework.base.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.gamesbykevin.a2048.board.Block.BLOCK_DIMENSIONS;
import static com.gamesbykevin.a2048.board.Block.DIMENSIONS_MAX;
import static com.gamesbykevin.a2048.board.Block.DIMENSION_CHANGE_VELOCITY;
import static com.gamesbykevin.a2048.board.Block.START_X;
import static com.gamesbykevin.a2048.board.Block.START_Y;
import static com.gamesbykevin.a2048.board.Block.VALUES;
import static com.gamesbykevin.a2048.board.Board.BORDER_THICKNESS;
import static com.gamesbykevin.a2048.board.Board.PADDING;
import static com.gamesbykevin.a2048.opengl.OpenGLSurfaceView.HEIGHT;
import static com.gamesbykevin.a2048.opengl.OpenGLSurfaceView.WIDTH;

/**
 * Created by Kevin on 5/31/2017.
 */

public class BoardHelper {

    /**
     * Our random object used to generate a random board
     */
    private static Random RANDOM_OBJECT;

    public static final int SPAWN_VALUE_1 = VALUES[1];
    public static final int SPAWN_VALUE_2 = VALUES[2];

    //keep track of how many new blocks are created
    public static int BLOCK_256 = 0;
    public static int BLOCK_512 = 0;
    public static int BLOCK_1024 = 0;
    public static int BLOCK_4096 = 0;
    public static int BLOCK_8192 = 0;

    /**
     * Merge the blocks on the board
     * @param board The board we are merging
     * @param merge The direction of the merge
     * @param combine Do we combine similar blocks?
     */
    public static void merge(Board board, GameManager.Merge merge, boolean combine) {

        //merge the board accordingly
        switch (merge) {

            case North:
                mergeVertical(board, combine, false);
                break;

            case South:
                mergeVertical(board, combine, true);
                break;

            case West:
                mergeHorizontal(board, combine, false);
                break;

            case East:
                mergeHorizontal(board, combine, true);
                break;

            default:
                throw new RuntimeException("Merge not handled here: " + merge.toString());
        }
    }

    /**
     * Merge the blocks horizontally
     * @param board The board containing the blocks we want to merge
     * @param combine If the blocks match do we combine them together?
     * @param east true if you want to merge east, false we will merge west
     */
    private static void mergeHorizontal(Board board, boolean combine, boolean east) {

        //check every row
        for (int row = 0; row < board.getRows(); row++) {

            //the previous value block
            int previousValue = -1;

            //the east column of the last block that we can merge with
            int columnMerge = (east) ? board.getCols() - 1 : 0;

            //if we can't merge what is the next block
            int columnNoMerge = columnMerge;

            int col = columnMerge;
            int end = (east) ? -1 : board.getCols();
            int increment = (east) ? -1 : 1;

            while (col != end) {

                //get the block at the current position
                Block block = board.getBlock(col, row);

                //change the column
                col += increment;

                //if the block doesn't exist there is no need to continue
                if (block == null)
                    continue;

                //if the block value matches the previous we can merge
                if (block.getValue() == previousValue && combine) {

                    //update the target of the current block
                    block.setColTarget(columnMerge);

                    //make sure previous value can't match since we just merged
                    previousValue = -1;

                    //since we can't merge this block, the next column where we can
                    columnMerge = columnMerge + increment;

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
                    columnNoMerge = columnMerge + increment;
                }
            }
        }
    }

    /**
     * Merge the blocks vertically
     * @param board The board containing the blocks we want to merge
     * @param combine If the blocks match do we combine them together?
     * @param south true if you want to merge south, false we will merge north
     */
    private static void mergeVertical(Board board, boolean combine, boolean south) {

        //check each column
        for (int col = 0; col < board.getCols(); col++) {

            //the previous value block
            int previousValue = -1;

            //the south row of the last block that we can merge with
            int rowMerge = (south) ? board.getRows() - 1 : 0;

            //if we can't merge what is the next block
            int rowNoMerge = rowMerge;

            int row = rowMerge;
            int end = (south) ? -1 : board.getRows();
            int increment = (south) ? -1 : 1;

            while (row != end) {

                //get the block at the current position
                Block block = board.getBlock(col, row);

                //change the row
                row += increment;

                //if the block doesn't exist there is no need to continue
                if (block == null)
                    continue;

                //if the block value matches the previous we can merge
                if (block.getValue() == previousValue && combine) {

                    //update the target of the current block
                    block.setRowTarget(rowMerge);

                    //make sure previous value can't match since we just merged
                    previousValue = -1;

                    //since we can't merge this block, the next row where we can
                    rowMerge = rowMerge + increment;

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
                    rowNoMerge = rowMerge + increment;
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

        //reset our block counts to 0
        BLOCK_256 = 0;
        BLOCK_512 = 0;
        BLOCK_1024 = 0;
        BLOCK_4096 = 0;
        BLOCK_8192 = 0;

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

                    //keep track of blocks created for achievements
                    if (block.getValue() == VALUES[8])
                        BLOCK_256++;
                    if (block.getValue() == VALUES[9])
                        BLOCK_512++;
                    if (block.getValue() == VALUES[10])
                        BLOCK_1024++;
                    if (block.getValue() == VALUES[12])
                        BLOCK_4096++;
                    if (block.getValue() == VALUES[13])
                        BLOCK_8192++;

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

    /**
     * Generate a puzzle board
     * @param board Our board reference
     */
    public static void generatePuzzle(Board board, final int seed) {

        //update the seed so the same random board is generated
        getRandomObject().setSeed(seed);

        //remove any existing blocks
        board.getBlocks().clear();

        final int cols = board.getCols();
        final int rows = board.getRows();

        //start at the max value
        int value = VALUES[VALUES.length - 1];

        //where do we start
        int startCol, startRow;

        //pick end of board to start
        if (getRandomObject().nextBoolean()) {

            //either first or last row
            startRow = (getRandomObject().nextBoolean()) ? 0 : rows - 1;
            startCol = (getRandomObject().nextInt(cols - 1)) + 1;

        } else {

            //either first or last column
            startCol = (getRandomObject().nextBoolean()) ? 0 : cols - 1;
            startRow = (getRandomObject().nextInt(rows - 1)) + 1;
        }

        //add the block to start
        board.addBlock(startCol, startRow, value);

        //full board size so we know when to stop
        final int max = cols * rows;

        //continue until there are no more open block spaces available
        while (board.getBlocks().size() < max && value > VALUES[1]) {

            //did we split anything
            boolean split = false;

            //get the block for our current value
            for (int i = 0; i < board.getBlocks().size(); i++) {

                //get the current block
                Block block = board.getBlocks().get(i);

                //if the block equals the current value and is > 2 we need to split it into 2 smaller value blocks
                if (block.getValue() == value && block.getValue() > VALUES[1]) {

                    //list of available places
                    List<Cell> available = new ArrayList<>();

                    //current location
                    int currentCol = (int)block.getCol();
                    int currentRow = (int)block.getRow();

                    for (int col = -1; col <= 1; col++) {
                        for (int row = -1; row <= 1; row++) {

                            if (col != 0 && row != 0)
                                continue;

                            //skip if the location is out of bounds
                            if (currentCol + col < 0 || currentCol + col >= cols)
                                continue;
                            if (currentRow + row < 0 || currentRow + row >= rows)
                                continue;

                            //make sure the block space is empty
                            if (board.getBlock(currentCol + col, currentRow + row) == null)
                                available.add(new Cell(currentCol + col, currentRow + row));
                        }
                    }

                    //if there is no way we can split continue to check the next block
                    if (available.isEmpty())
                        continue;

                    //new value
                    int newValue = (block.getValue() / 2);

                    //remove existing from list
                    board.getBlocks().remove(i);

                    //adjust index so we don't miss checking any blocks
                    i--;

                    //add new block in the same place
                    board.addBlock((int)block.getCol(), (int)block.getRow(), newValue);

                    //where to put the next block
                    Cell cell = available.get(getRandomObject().nextInt(available.size()));

                    //add the other block
                    board.addBlock((int)cell.getCol(), (int)cell.getRow(), newValue);

                    //flag that we split the block
                    split = true;

                    //move the blocks in random direction
                    moveBoard(board);

                } else {

                    //if we can't split up the values anymore, exit loop
                    if (block.getValue() <= VALUES[1] && value <= VALUES[1])
                        break;
                }
            }

            //if we weren't able to split any blocks, lower the split value
            if (!split)
                value = (value / 2);
        }
    }

    private static void moveBoard(Board board) {

        //since we split move the blocks in a random direction
        GameManager.Merge move = GameManager.Merge.values()[getRandomObject().nextInt(GameManager.Merge.values().length)];

        //move the pieces without merging
        merge(board, move, false);

        //move until we are at the target
        while (!board.hasTarget()) {
            for (Block tmp : board.getBlocks()) {
                if (!tmp.hasTarget())
                    tmp.update();
            }
        }
    }

    private static Random getRandomObject() {

        //create new instance if null
        if (RANDOM_OBJECT == null)
            RANDOM_OBJECT = new Random();

        return RANDOM_OBJECT;
    }
}