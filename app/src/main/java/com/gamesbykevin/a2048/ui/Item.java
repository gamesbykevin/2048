package com.gamesbykevin.a2048.ui;

import android.graphics.drawable.Drawable;

/**
 * Created by Kevin on 7/2/2017.
 */

/**
 * Each level selection will have a text description and is it completed
 */
public class Item
{
    //default value for our description
    private String title = "0";

    //is the level completed
    private boolean completed = false;

    /**
     * Default constructor
     */
    public Item()
    {
        super();
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setCompleted(final boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return this.completed;
    }
}