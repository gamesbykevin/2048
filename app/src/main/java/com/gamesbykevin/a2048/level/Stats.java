package com.gamesbykevin.a2048.level;

import android.content.SharedPreferences;

import com.gamesbykevin.a2048.BaseActivity;
import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.game.GameManagerHelper.Difficulty;
import com.gamesbykevin.a2048.game.GameManagerHelper.Mode;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kevin on 7/6/2017.
 */

public class Stats {

    /**
     * Total number of levels for puzzle mode
     */
    private static final int LEVELS = 100;

    //this will contain all the stats for our levels
    private HashMap<Entry, ArrayList<Level>> data = new HashMap<>();

    //activity reference to access shared preferences
    private final BaseActivity activity;

    //track the current level
    private int index = 0;

    //the mode and difficulty in play
    public static Mode MODE;
    public static Difficulty DIFFICULTY;

    public Stats(BaseActivity activity) {

        //we need to create this type in order to de-serialize the json back to hash map object
        Type type = new TypeToken<HashMap<Entry, ArrayList<Level>>>(){}.getType();

        //get our data from the shared preferences
        this.data = (HashMap<Entry, ArrayList<Level>>)activity.getObjectValue(R.string.game_stats_file_key, type);

        //if still null create hash map and default level items
        if (this.data == null)
            this.data = new HashMap<>();

        //make sure all entries exist for every game mode/difficulty/level
        for (Mode mode : Mode.values()) {
            for (Difficulty difficulty : Difficulty.values()) {

                Entry entry = new Entry(mode, difficulty);

                if (data.get(entry) == null) {

                    //if there is no entry add it
                    data.put(entry, new ArrayList<Level>());
                }

                //get our list of levels
                ArrayList<Level> tmp = data.get(entry);

                //for puzzle mode we have a number of puzzles to add
                if (mode == Mode.Puzzle) {

                    //add all the levels to the list, if they don't exist
                    for (index = 0; index < LEVELS; index++) {
                        addLevel(tmp, index);
                    }

                } else {

                    //this mode only has 1 level
                    addLevel(tmp, 0);
                }

                //update our hash map with the new list
                data.put(entry, tmp);
            }
        }

        //store activity reference
        this.activity = activity;
    }

    /**
     * Add level to the provided array list, if the level already exists it will not be added
     * @param list The list of levels to search
     * @param index Level index we are checking for
     */
    private void addLevel(ArrayList<Level> list, int index) {

        //check if the level exists first
        for (int i = 0; i < list.size(); i++) {

            //if level already exists, no need to continue
            if (list.get(i).getLevelIndex() == index)
                return;
        }

        //no level was found, so now we create one
        Level level = new Level(index, index);

        //set the display title
        level.setTitle((index + 1) + "");

        //add it to the list
        list.add(level);
    }

    public void nextLevelIndex() {
        setLevelIndex(getLevelIndex() + 1);
    }

    public void setLevelIndex(final int index) {
        this.index = index;
    }

    public int getLevelIndex() {
        return this.index;
    }

    public ArrayList<Level> getLevels() {
        return getLevels(MODE, DIFFICULTY);
    }

    private ArrayList<Level> getLevels(Mode mode, Difficulty difficulty) {

        //check every key in the hash map
        for (Entry entry : data.keySet()) {

            //if a match return the list
            if (entry.mode == mode && entry.difficulty == difficulty)
                return data.get(entry);
        }

        //nothing was found
        return null;
    }

    public Level getLevel() {

        //get our list of levels
        ArrayList<Level> tmp = getLevels(MODE, DIFFICULTY);

        //make sure the level index stays in bounds
        if (getLevelIndex() >= tmp.size())
            setLevelIndex(0);

        for (int i = 0; i < tmp.size(); i++) {

            //if match we found our level
            if (tmp.get(i).getLevelIndex() == getLevelIndex())
                return tmp.get(i);
        }

        //nothing was found
        return null;
    }

    public void markComplete(long duration, int score) {

        //get the current level
        Level level = getLevel();

        //set complete true
        level.setCompleted(true);

        MainActivity.logEvent("Time New: " + duration);
        MainActivity.logEvent("Time Old: " + level.getDuration());
        MainActivity.logEvent("Score New: " + score);
        MainActivity.logEvent("Score Old: " + level.getScore());

        //if the time is faster, assign the new record
        if (duration < level.getDuration() || level.getDuration() <= 0) {
            level.setDuration(duration);
            MainActivity.logEvent("New Record (time)");
        }

        //if we set a new high score, assign the new record
        if (score > level.getScore() || level.getScore() <= 0) {
            level.setScore(score);
            MainActivity.logEvent("New Record (score)");
        }

        //save changes
        save();
    }

    /**
     * Store the data in the shared preferences
     */
    public void save() {

        //get the editor so we can change the shared preferences
        SharedPreferences.Editor editor = activity.getSharedPreferences().edit();

        //convert object to json string
        final String json = activity.GSON.toJson(data);

        //print data
        MainActivity.logEvent(json);

        //convert to json string and store in preferences
        editor.putString(activity.getString(R.string.game_stats_file_key), json);

        //make it final by committing the change
        editor.commit();
    }

    /**
     * Each entry contains the game mode / difficulty
     */
    private class Entry {

        protected final Mode mode;
        protected final Difficulty difficulty;

        protected Entry(Mode mode, Difficulty difficulty) {
            this.mode = mode;
            this.difficulty = difficulty;
        }

        @Override
        public boolean equals(Object object){

            if(object == null)
                return false;

            //if not instance return false
            if (object.getClass() != Entry.class)
                return false;

            //get our object
            Entry entry = (Entry)object;

            if (entry.mode == null && mode != null)
                return false;
            if (entry.mode != null && mode == null)
                return false;

            if (entry.difficulty == null && difficulty != null)
                return false;
            if (entry.difficulty != null && difficulty == null)
                return false;

            //make sure the values match to be equal
            return (entry.mode.equals(mode) && entry.difficulty.equals(difficulty));
        }

        @Override
        public int hashCode(){
            int key1HashCode = mode.hashCode();
            int key2HashCode = difficulty.hashCode();
            return key1HashCode >> 3 + key2HashCode << 5;
        }
    }
}