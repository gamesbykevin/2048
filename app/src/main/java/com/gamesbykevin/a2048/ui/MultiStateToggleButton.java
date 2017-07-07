package com.gamesbykevin.a2048.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by Kevin on 7/2/2017.
 */

public class MultiStateToggleButton extends AppCompatButton {

    //array of options
    private Object[] options;

    //current selection
    private int index = 0;

    //application context
    private final Context context;

    //header text to be displayed in front of our button text
    private String header = "";

    /**
     *
     * @param context
     * @param attrs
     */
    public MultiStateToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     *
     * @param context
     */
    public MultiStateToggleButton(Context context) {
        this(context, null, 0);
    }

    /**
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MultiStateToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        //store the context
        this.context = context;
    }

    /**
     *
     * @param header
     */
    public void setHeader(final String header) {

        //we don't want to store a null value
        if (header == null)
            return;

        //assign value
        this.header = header;
    }

    /**
     *
     * @return
     */
    public String getHeader() {
        return this.header;
    }

    /**
     *
     * @param options
     */
    public void setOptions(Object[] options) {

        //store our array reference
        this.options = options;

        //start at beginning
        setIndex(0);

        //make sure the correct text is displayed
        assignText();
    }

    /**
     * Set the current index
     * @param object Object we want as the current index
     */
    public void setIndex(final Object object) {

        //check each option until we find a match
        for (int i = 0; i < this.options.length; i++) {

            //if the object matches
            if (this.options[i] == object) {

                //assign the index
                setIndex(i);

                //we are done
                break;
            }
        }
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
    private int getIndex() {
        return this.index;
    }

    /**
     * Assign the text to be rendered based on the current index position
     */
    public void assignText() {

        //assign the text to be displayed
        super.setText(getHeader() + getText(getIndex()));

        //force re-draw
        super.invalidate();
    }

    /**
     * Get the text
     * @param index The position of the text
     * @return The text at the specified position
     */
    public String getText(final int index) {
        return getValue(index).toString();
    }

    /**
     * Get the text
     * @return The text at the current index position
     */
    public String getText() {
        return getText(getIndex());
    }

    /**
     * Get the value
     * @return The object of the current selection
     */
    public Object getValue() {
        return getValue(getIndex());
    }

    /**
     * Get the value
     * @param index The desired index
     * @return The object at the current selection
     */
    public Object getValue(final int index) {
        return this.options[index];
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