package com.gamesbykevin.a2048.board;

import com.gamesbykevin.a2048.base.EntityItem;

import org.junit.Test;

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
    }
}
