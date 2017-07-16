package com.gamesbykevin.a2048.util;

import com.gamesbykevin.a2048.base.EntityItem;
import com.gamesbykevin.a2048.board.Block;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kevin on 7/15/2017.
 */
public class StatDescription extends EntityItem {

    //the dimensions of each number animation
    public static final int ANIMATION_WIDTH = 66;
    public static final int ANIMATION_HEIGHT = 83;

    private final float RATIO = 0.35F;

    private final int WIDTH = (int)(ANIMATION_WIDTH * RATIO);
    private final int HEIGHT = (int)(ANIMATION_HEIGHT * RATIO);

    //the starting coordinates where we render the number
    public static final int START_Y = 50;
    public static final int START_X = 50;

    //our array object for each digit in our score
    private ArrayList<Character> characters;

    //the text to display
    private String desc = "";

    //the total number of characters we need access to render
    public static final int TOTAL_CHARACTERS = 12;

    /**
     * Default constructor
     */
    public StatDescription()
    {
        //create a new numbers list
        this.characters = new ArrayList<>();

        //set the dimensions
        setDefaultDimensions();

        //set the start location
        super.setX(START_X);
        super.setY(START_Y);
    }

    public void setDefaultDimensions() {
        //set the dimensions
        super.setWidth(WIDTH);
        super.setHeight(HEIGHT);
    }

    public void setDescription(final int desc, final int[] textures)
    {
        setDescription(desc + "", textures);
    }

    public void setDescription(final String desc, final int[] textures)
    {
        //assign the value
        this.desc = desc;

        //disable any unnecessary digits
        for (int i = desc.length(); i < characters.size(); i++)
        {
            characters.get(i).enabled = false;
        }

        //convert string to single characters
        char[] text = desc.toCharArray();

        //get the x-coordinate for our starting point
        int x = (int)getX();

        //check each character so we can map the animations
        for (int i = 0; i < text.length; i++)
        {
            int textureId;

            //identify which animation
            switch (text[i])
            {
                case '0':
                    textureId = textures[Block.VALUES.length + 0];
                    break;

                case '1':
                    textureId = textures[Block.VALUES.length + 1];
                    break;

                case '2':
                    textureId = textures[Block.VALUES.length + 2];
                    break;

                case '3':
                    textureId = textures[Block.VALUES.length + 3];
                    break;

                case '4':
                    textureId = textures[Block.VALUES.length + 4];
                    break;

                case '5':
                    textureId = textures[Block.VALUES.length + 5];
                    break;

                case '6':
                    textureId = textures[Block.VALUES.length + 6];
                    break;

                case '7':
                    textureId = textures[Block.VALUES.length + 7];
                    break;

                case '8':
                    textureId = textures[Block.VALUES.length + 8];
                    break;

                case '9':
                    textureId = textures[Block.VALUES.length + 9];
                    break;

                case ':':
                    textureId = textures[Block.VALUES.length + 10];
                    break;

                case '.':
                    textureId = textures[Block.VALUES.length + 11];
                    break;

                default:
                    throw new RuntimeException("Character not found '" + text[i] + "'");
            }

            //if we are within the array we can reuse
            if (i < characters.size())
            {
                characters.get(i).x = x;
                characters.get(i).enabled = true;
                characters.get(i).textureId = textureId;
            }
            else
            {
                //else we add a new object to the array
                characters.add(new Character(x, textureId));
            }

            //adjust x-coordinate
            x += super.getWidth();
        }
    }

    @Override
    public void render(GL10 gl)
    {
        //store coordinates
        final double x = getX();
        final double y = getY();

        //check every digit in the list
        for (int i = 0; i < characters.size(); i++)
        {
            //get the current digit object
            final Character character = characters.get(i);

            //if this is not enabled no need to continue
            if (!character.enabled)
                return;

            //assign x-coordinate location
            setX(character.x);

            //assign texture
            super.setTextureId(character.textureId);

            //render animation
            super.render(gl);
        }

        //restore original coordinates
        setX(x);
        setY(y);
    }

    /**
     * This class will keep track of each character in our number
     */
    private class Character
    {
        //location
        protected int x;

        //texture to render
        protected int textureId;

        //are we rendering this digit?
        protected boolean enabled = true;

        private Character(final int x, final int textureId)
        {
            this.x = x;
            this.textureId = textureId;
            this.enabled = true;
        }
    }
}