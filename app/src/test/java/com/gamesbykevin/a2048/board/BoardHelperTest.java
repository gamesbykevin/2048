package com.gamesbykevin.a2048.board;

import com.gamesbykevin.a2048.game.GameManager;
import com.gamesbykevin.a2048.game.GameManagerHelper;

import org.junit.Test;

import static com.gamesbykevin.a2048.game.GameManagerHelper.DIMENSIONS_EASY;
import static com.gamesbykevin.a2048.game.GameManagerHelper.DIMENSIONS_HARD;
import static com.gamesbykevin.a2048.game.GameManagerHelper.DIMENSIONS_MEDIUM;
import static com.gamesbykevin.a2048.game.GameManagerHelper.PUZZLE_DIMENSIONS_EASY;
import static com.gamesbykevin.a2048.game.GameManagerHelper.PUZZLE_DIMENSIONS_HARD;
import static com.gamesbykevin.a2048.game.GameManagerHelper.PUZZLE_DIMENSIONS_MEDIUM;
import static com.gamesbykevin.a2048.level.Stats.LEVELS;
import static com.gamesbykevin.a2048.level.Stats.MODE;
import static com.gamesbykevin.a2048.level.Stats.DIFFICULTY;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import com.gamesbykevin.a2048.game.GameManagerHelper.Mode;
import com.gamesbykevin.a2048.game.GameManagerHelper.Difficulty;

/**
 * Created by Kevin on 7/15/2017.
 */

public class BoardHelperTest extends BoardHelper {

    @Test
    public void generatePuzzleTest() {

        //generate a board for each mode / difficulty
        for (Mode mode : Mode.values())
        {
            //set the current mode
            MODE = mode;

            for (Difficulty difficulty : Difficulty.values()) {

                //set the current difficulty
                DIFFICULTY = difficulty;

                switch (MODE) {
                    case Puzzle:

                        for (int i = 0; i < LEVELS; i++) {

                            Board board = new Board();
                            BoardHelper.generatePuzzle(board, i);

                            for (int x = i + 1; x < LEVELS; x++) {

                                //shouldn't happen, but make sure
                                if (x == i)
                                    continue;

                                System.out.println("Mode: " + MODE.toString());
                                System.out.println("Difficulty: " + DIFFICULTY.toString());
                                System.out.println("Seeds: " + i + " & " + x);

                                Board tmp = new Board();
                                BoardHelper.generatePuzzle(tmp, x);

                                //now check to make sure the levels aren't the same
                                assertFalse(hasMatch(board, tmp));
                                System.out.println("Passed");
                            }
                        }
                        break;

                    //don't need to worry about these
                    case Original:
                    case Challenge:
                    case Infinite:
                        break;

                    default:
                        throw new RuntimeException("Mode not handled here " + MODE.toString());
                }

            }
        }
    }

    private static boolean hasMatch(Board board1, Board board2) {

        //there can't be a match if the dimensions aren't the same
        if (board1.getCols() != board2.getCols())
            return false;
        if (board1.getRows() != board2.getRows())
            return false;

        //count how many blocks are matching
        int matches = 0;

        //check every block
        for (int row = 0; row < board1.getRows(); row++) {
            for (int col = 0; col < board1.getCols(); col++) {

                //get the current block for each board
                Block block1 = board1.getBlock(col, row);
                Block block2 = board2.getBlock(col, row);

                if (block1 != null && block2 == null)
                    continue;
                if (block1 == null && block2 != null)
                    continue;
                if (block1 == null && block2 == null) {
                    matches++;
                } else if (block1.getValue() == block2.getValue()) {
                    matches++;
                }
            }
        }

        //if the number of matches equals the board size we have a match
        return (matches == (board1.getCols() * board1.getRows()));
    }

    @Test
    public void getBoardDimensionsTest() {

        for (Mode mode : Mode.values()) {

            MODE = mode;

            for (Difficulty difficulty : Difficulty.values()) {

                DIFFICULTY = difficulty;

                //puzzle mode will have a different size board
                boolean puzzle = (MODE == GameManagerHelper.Mode.Puzzle);

                int test = 0;

                //figure out the size of the board based on difficulty and game mode
                switch (DIFFICULTY) {

                    case Easy:
                        test = (puzzle) ? PUZZLE_DIMENSIONS_EASY : DIMENSIONS_EASY;
                        break;

                    case Medium:
                        test = (puzzle) ? PUZZLE_DIMENSIONS_MEDIUM : DIMENSIONS_MEDIUM;
                        break;

                    case Hard:
                        test = (puzzle) ? PUZZLE_DIMENSIONS_HARD : DIMENSIONS_HARD;
                        break;
                }

                assertEquals(test, getBoardDimensions());
            }
        }
    }

    @Test
    public void mergeTest() {

        for (GameManager.Merge merge : GameManager.Merge.values()) {

            for (Mode mode : Mode.values()) {
                for (Difficulty difficulty : Difficulty.values()) {
                    MODE = mode;
                    DIFFICULTY = difficulty;

                    Board board;

                    board = new Board();

                    //fill up the whole board spawning a block everywhere
                    for (int i = 0; i < board.getCols() * board.getRows(); i++) {
                        board.spawn();
                    }

                    //merge in any direction
                    super.merge(board, merge, false);

                    //assume all blocks are already at their target
                    assertTrue(board.hasTarget());

                    super.merge(board, merge, true);
                }
            }
        }
    }
}