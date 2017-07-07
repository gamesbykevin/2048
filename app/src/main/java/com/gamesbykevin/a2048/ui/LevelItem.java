package com.gamesbykevin.a2048.ui;

import android.graphics.drawable.Drawable;

import com.gamesbykevin.a2048.game.GameManager;

/**
 * Created by Kevin on 7/2/2017.
 */

/**
 * Each level selection will have a text description and is it completed
 */
public class LevelItem
{
    //default value for our description
    private String title = "0";

    //is the level completed (for puzzle mode)
    private boolean completed = false;

    //seed used for random generation
    private final int seed;

    //level index
    private final int index;

    //the length it took to complete the item
    private long duration = 0;

    /**
     * Default constructor
     */
    public LevelItem(int seed, int index)
    {
        super();

        //assign the seed
        this.seed = seed;

        //store the level index
        this.index = index;
    }

    public int getLevelIndex() {
        return this.index;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(final long duration) {
        this.duration = duration;
    }

    /**
     *
     * @return
     */
    public int getSeed() {
        return this.seed;
    }

    /**
     *
     * @param title
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return this.title;
    }

    /**
     *
     * @param completed
     */
    public void setCompleted(final boolean completed) {
        this.completed = completed;
    }

    /**
     *
     * @return
     */
    public boolean isCompleted() {
        return this.completed;
    }
}