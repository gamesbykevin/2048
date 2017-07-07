package com.gamesbykevin.a2048.ui;

import android.app.LauncherActivity;
import android.content.SharedPreferences;

import com.gamesbykevin.a2048.BaseActivity;
import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.game.GameManager.Difficulty;
import com.gamesbykevin.a2048.game.GameManager.Mode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Created by Kevin on 7/6/2017.
 */

public class LevelItems {

    /**
     * Total number of levels for puzzle mode
     */
    private static final int LEVELS = 100;

    //this will contain all the stats for our levels
    private HashMap<Mode, HashMap<Difficulty, ArrayList<LevelItem>>> data = new HashMap<>();

    //activity reference to access shared preferences
    private final BaseActivity activity;

    //track the current level
    private int index = 0;

    public LevelItems(BaseActivity activity) {

        //get our data from the shared preferences
        this.data = (HashMap<Mode, HashMap<Difficulty, ArrayList<LevelItem>>>)activity.getObjectValue(R.string.game_stats_file_key, data.getClass());

        //if still null create hash map and default level items
        if (this.data == null)
            this.data = new HashMap<>();

        //make sure all entries exist for every game mode/difficulty/level
        for (Mode mode : Mode.values()) {
            for (Difficulty difficulty : Difficulty.values()) {

                int index = 0;

                //the current level item
                LevelItem item = getLevelItem(mode, difficulty, index);

                if (mode == Mode.Puzzle) {

                    for (index = 0; index < LEVELS; index++) {

                        //get the current level item
                        item = getLevelItem(mode, difficulty, index);

                        //if the item does not exist we will create it
                        if (item == null) {

                            //create new level item
                            item = new LevelItem(index, index);

                            //set the display title
                            item.setTitle((index + 1) + "");

                            //add it to the list
                            getLevels(mode, difficulty).add(item);
                        }
                    }

                } else {

                    if (item == null) {

                        //create new level item
                        item = new LevelItem(index, index);

                        //set the display title
                        item.setTitle((index + 1) + "");

                        //add it to the list
                        getLevels(mode, difficulty).add(item);
                    }
                }
            }
        }

        //store activity reference
        this.activity = activity;
    }

    public void setLevelIndex(final int index) {
        this.index = index;
    }

    public int getLevelIndex() {
        return this.index;
    }

    /**
     * Get the hash map list for each difficulty
     * @param mode
     * @return
     */
    private HashMap<Difficulty, ArrayList<LevelItem>> getDifficulties(Mode mode) {

        //if no entry add default
        if (this.data.get(mode) == null) {

            for (Difficulty difficulty : Difficulty.values()) {

                //create new tmp list
                HashMap<Difficulty, ArrayList<LevelItem>> tmp1 = new HashMap<>();

                ArrayList<LevelItem> tmp2 = new ArrayList<>();
            }

            this.data.put(mode, new HashMap<Difficulty, ArrayList<LevelItem>>());
        }

        return this.data.get(mode);
    }

    /**
     * Get the list of levels for the specified mode/difficulty
     * @param mode
     * @param difficulty
     * @return
     */
    public ArrayList<LevelItem> getLevels(Mode mode, Difficulty difficulty) {

        //if no entry add default
        if (getDifficulties(mode).get(difficulty) == null) {
            getDifficulties(mode).put(difficulty, new ArrayList<LevelItem>());
        }

        return getDifficulties(mode).get(difficulty);
    }

    public LevelItem getLevelItem(Mode mode, Difficulty difficulty) {
        return getLevelItem(mode, difficulty, getLevelIndex());
    }

    public LevelItem getLevelItem(Mode mode, Difficulty difficulty, int levelIndex) {

        //get our list of levels
        ArrayList<LevelItem> levels = getLevels(mode, difficulty);

        //check each levels for the desired level index
        for (int i = 0; i < levels.size(); i++) {

            if (levels.get(i).getLevelIndex() == levelIndex)
                return levels.get(i);
        }

        //if not found, return null
        return null;
    }

    /**
     * Mark the current level completed
     * @param mode The desired mode
     * @param difficulty The desired difficulty
     * @return true if level item was updated or added, false otherwise
     */
    public boolean update(Mode mode, Difficulty difficulty) {

        //get the current list of levels
        ArrayList<LevelItem> levels = getLevels(mode, difficulty);

        //check the list to find our item
        for (int i = 0; i < levels.size(); i++) {

            //get the level item
            LevelItem tmp = levels.get(i);

            //skip if no match
            if (tmp.getLevelIndex() != getLevelIndex())
                continue;

            //update true
            tmp.setCompleted(true);

            //exit loop
            break;
        }

        //save changes
        save();

        return true;
    }

    /**
     * Store the data in the shared preferences
     */
    public void save() {

        //get the editor so we can change the shared preferences
        SharedPreferences.Editor editor = activity.getSharedPreferences().edit();

        //convert to json string and store in preferences
        editor.putString(activity.getString(R.string.game_stats_file_key), activity.GSON.toJson(data));

        //make it final by committing the change
        editor.commit();
    }
}