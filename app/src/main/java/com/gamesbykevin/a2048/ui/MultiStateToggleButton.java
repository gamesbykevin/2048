package com.gamesbykevin.a2048.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by Kevin on 7/2/2017.
 */

public class MultiStateToggleButton extends AppCompatButton {

    //array of options
    private String[] options;

    //current selection
    private int index = 0;

    private final Context context;

    public MultiStateToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStateToggleButton(Context context) {
        this(context, null, 0);
    }

    public MultiStateToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        //store the context
        this.context = context;
    }

    public void setOptions(String[] options) {

        //store our array reference
        this.options = options;

        //start at beginning
        setIndex(0);

        //make sure the correct text is displayed
        assignText();
    }

    /**
     * Assign the index
     * @param index The desired position to be current
     */
    public void setIndex(final int index) {

        //assign index value
        this.index = index;

        //keep index in bounds
        if (getIndex() < 0 || getIndex() >= this.options.length)
            setIndex(0);

        //assign the text based on the current index
        assignText();
    }

    /**
     * Get the index
     * @return The current index position
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Assign the text to be rendered based on the current index position
     */
    public void assignText() {

        //assign the text to be displayed
        super.setText(getText(getIndex()));

        //force re-draw
        super.invalidate();
    }

    /**
     * Get the text
     * @param index The position of the text
     * @return The text at the specified position
     */
    public String getText(final int index) {
        return this.options[index];
    }

    /**
     * Get the text
     * @return The text at the current index position
     */
    public String getText() {
        return getText(getIndex());
    }

    /**
     * The user pressed the button, switch states and update the text
     */
    public void select() {

        //increase index
        setIndex(getIndex() + 1);

        //update the new text
        assignText();
    }
}