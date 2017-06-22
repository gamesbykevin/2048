package com.gamesbykevin.a2048.board;

import com.gamesbykevin.a2048.base.EntityItem;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Kevin on 6/20/2017.
 */

public class BlockTest extends Block {

    /**
     * Unit test for our constant values
     */
    @Test
    public void testConstants() {

        //always needs to be an even number when moving blocks
        assertTrue(1 % Block.VELOCITY == 0);

        //no velocity should always be 0
        assertTrue(Block.VELOCITY_NONE == 1);

        //our png image height will be the same, as well as width
        assertTrue(ANIMATION_DIMENSIONS == 90);
    }

    @Test
    public void testBlock() {

        //create a new block
        Block block = new Block();

        //default size of the blocks
        assertTrue(block.getWidth() == ANIMATION_DIMENSIONS);
        assertTrue(block.getHeight() == ANIMATION_DIMENSIONS);

        //by default  the location is 0,0
        assertTrue(block.getCol() == 0);
        assertTrue(block.getRow() == 0);

        //assert our block is not null
        assertNotNull(block);
    }
}