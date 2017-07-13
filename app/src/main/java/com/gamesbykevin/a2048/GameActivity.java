package com.gamesbykevin.a2048;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.gamesbykevin.a2048.game.GameManager;
import com.gamesbykevin.a2048.game.GameManagerHelper;
import com.gamesbykevin.a2048.game.GameManagerHelper.Difficulty;
import com.gamesbykevin.a2048.game.GameManagerHelper.Mode;
import com.gamesbykevin.a2048.level.Stats;
import com.gamesbykevin.a2048.opengl.OpenGLSurfaceView;
import com.gamesbykevin.a2048.services.AchievementHelper;
import com.gamesbykevin.a2048.services.BaseGameActivity;
import com.gamesbykevin.a2048.ui.CustomAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.gamesbykevin.a2048.game.GameManagerHelper.updateDisplayStats;
import static com.gamesbykevin.a2048.level.Stats.DIFFICULTY;
import static com.gamesbykevin.a2048.level.Stats.MODE;
import static com.gamesbykevin.a2048.opengl.OpenGLSurfaceView.DIRTY_FLAG;

public class GameActivity extends BaseGameActivity implements AdapterView.OnItemClickListener {

    //our open GL surface view
    private GLSurfaceView glSurfaceView;

    /**
     * Create a random object which the seed as the current time stamp
     */
    private static Random RANDOM;

    /**
     * Our game manager class
     */
    public static GameManager MANAGER;

    /**
     * The class that tracks our level progress for each game mode/difficulty
     */
    public static Stats STATS;

    //has the activity been paused
    private boolean paused = false;

    //our layout parameters
    private LinearLayout.LayoutParams layoutParams;

    //a list of layouts on the game screen, separate from opengl layout
    private List<ViewGroup> layouts;

    /**
     * Different steps in the game
     */
    public enum Step {
        Loading,
        Ready,
        GameOver,
        LevelSelect
    }

    //current step we are on
    private Step step = Step.Loading;

    //our custom adapter that we bind to GridView
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //don't check achievements, yet
        AchievementHelper.CHECK_ACHIEVEMENTS = false;

        //call parent
        super.onCreate(savedInstanceState);

        //store the current game settings while we play
        MODE = (Mode)getObjectValue(R.string.mode_file_key, Mode.class);
        DIFFICULTY = (Difficulty)getObjectValue(R.string.difficulty_file_key, Difficulty.class);

        //create our game manager
        MANAGER = new GameManager(this);

        //first assign default level is 0
        STATS.setLevelIndex(0);

        //update display stats
        updateDisplayStats();

        //flag reset
        GameManagerHelper.RESET = true;

        //turn dirty flag off
        DIRTY_FLAG = false;

        //set the content view
        setContentView(R.layout.activity_game);

        //obtain our open gl surface view object for reference
        this.glSurfaceView = (OpenGLSurfaceView)findViewById(R.id.openglView);

        //add the layouts to our list
        this.layouts = new ArrayList<>();
        this.layouts.add((LinearLayout)findViewById(R.id.gameOverLayoutDefault));
        this.layouts.add((LinearLayout)findViewById(R.id.gameOverLayoutPuzzle));
        this.layouts.add((LinearLayout)findViewById(R.id.loadingScreenLayout));
        this.layouts.add((TableLayout)findViewById(R.id.levelSelectLayout));

        //update level select screen
        refreshLevelSelect();
    }

    /**
     * Update the level select screen with the current data
     */
    public void refreshLevelSelect() {

        //get the grid view reference and assign on click
        GridView levelSelectGrid = (GridView)findViewById(R.id.levelSelectGrid);
        levelSelectGrid.setOnItemClickListener(this);

        //create the custom adapter using the level selection layout and data to populate it
        this.customAdapter = new CustomAdapter(getApplicationContext(), R.layout.level_selection, STATS.getLevels());

        //set our adapter to the grid view
        levelSelectGrid.setAdapter(this.customAdapter);
    }

    @Override
    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id)
    {
        //if puzzle mode, generate board with the specified seed
        if (MODE == Mode.Puzzle) {

            //assign the level selected
            STATS.setLevelIndex(STATS.getLevels().get(position).getLevelIndex());

        } else {

            //assign the level selected (technically won't be called since puzzle mode only has level select)
            STATS.setLevelIndex(0);
        }

        //update display stats
        updateDisplayStats();

        //reset game
        GameManagerHelper.RESET = true;

        //flag false
        DIRTY_FLAG = false;

        //we are now ready
        setStep(Step.Ready);
    }

    /**
     * Get our random object.<br>
     * If object is null a new instance will be instantiated
     * @return Random object used to generate random events
     */
    public static Random getRandomObject() {

        //create the object if null
        if (RANDOM == null) {

            //get the current timestamp
            final long time = System.nanoTime();

            //create our Random object
            RANDOM = new Random(time);

            //print the random seed
            MainActivity.logEvent("Random seed: " + time);
        }

        return RANDOM;
    }

    @Override
    protected void onStart() {

        //call parent
        super.onStart();
    }

    @Override
    protected void onDestroy() {

        //call parent
        super.onDestroy();

        //cleanup resources
    }

    @Override
    protected void onPause() {

        //call parent
        super.onPause();

        //flag paused true
        this.paused = true;

        //pause the game view
        glSurfaceView.onPause();

        //flag for recycling
        glSurfaceView = null;
    }

    @Override
    protected void onResume() {

        //call parent
        super.onResume();

        //if the game was previously paused create a new view
        if (this.paused) {

            //flag paused false
            this.paused = false;

            //create a new OpenGL surface view
            glSurfaceView = new OpenGLSurfaceView(this);

            //resume the game view
            glSurfaceView.onResume();

            //remove layouts from the parent view
            for (int i = 0; i < layouts.size(); i++) {
                ((ViewGroup)layouts.get(i).getParent()).removeView(layouts.get(i));
            }

            //set the content view for our open gl surface view
            setContentView(glSurfaceView);

            //add the layouts to the current content view
            for (int i = 0; i < layouts.size(); i++) {
                super.addContentView(layouts.get(i), getLayoutParams());
            }

        } else {

            //resume the game view
            glSurfaceView.onResume();
        }

        //determine what screens are displayed
        setStep(step);
    }

    public Step getStep() {
        return this.step;
    }

    public void setStep(final Step step) {

        //assign step
        this.step = step;

        //default all layouts to hidden
        for (int i = 0; i < layouts.size(); i++) {
            setLayoutVisibility(layouts.get(i), false);
        }

        //only display the correct screens
        switch (getStep()) {

            //show loading screen
            case Loading:
                setLayoutVisibility((ViewGroup)findViewById(R.id.loadingScreenLayout), true);
                break;

            //decide which game over screen is displayed
            case GameOver:
                setLayoutVisibility((ViewGroup)findViewById((MODE == Mode.Puzzle) ? R.id.gameOverLayoutPuzzle : R.id.gameOverLayoutDefault), true);
                break;

            //show level select screen
            case LevelSelect:
                setLayoutVisibility((ViewGroup)findViewById(R.id.levelSelectLayout), true);
                break;

            //don't re-enable any
            case Ready:
                break;
        }
    }

    private LinearLayout.LayoutParams getLayoutParams() {

        if (this.layoutParams == null)
            this.layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.MATCH_PARENT);

        return this.layoutParams;
    }

    /**
     *
     * @param layoutView
     * @param visible
     */
    public void setLayoutVisibility(final ViewGroup layoutView, final boolean visible) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //assign visibility accordingly
                layoutView.setVisibility(visible ? VISIBLE : INVISIBLE);

                //if the layout is visible, make sure it is displayed
                if (visible) {
                    layoutView.invalidate();
                    layoutView.bringToFront();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        //if we are playing puzzle mode, go back to the level select screen
        if (MODE == Mode.Puzzle) {

            //if not on level select screen, go to it
            if (getStep() != Step.LevelSelect) {

                //update list so it displays correct information
                refreshLevelSelect();

                //go to level select screen
                setStep(Step.LevelSelect);

                //no need to continue here
                return;
            }
        }

        //call parent
        super.onBackPressed();
    }

    public void onClickNext(View view) {

        //move to the next index
        STATS.nextLevelIndex();

        //update display stats
        updateDisplayStats();

        //flag the game to reset
        GameManagerHelper.RESET = true;

        //go back to the ready step
        setStep(Step.Ready);

        //play sound effect
        super.playSoundEffect();
    }

    /**
     * Reset the board
     * @param view
     */
    public void onClickRestart(View view) {

        //always stay at level 0 since it isn't puzzle mode
        STATS.setLevelIndex(0);

        //update display stats
        updateDisplayStats();

        //flag the game to reset
        GameManagerHelper.RESET = true;

        //go back to the ready step
        setStep(Step.Ready);

        //play sound effect
        super.playSoundEffect();
    }

    public void onClickLevelSelect(View view) {

        //go back to level select screen
        setStep(Step.LevelSelect);

        //update level select
        refreshLevelSelect();

        //play sound effect
        super.playSoundEffect();
    }

    /**
     * Go to the main game menu
     * @param view
     */
    public void onClickMenu(View view) {

        //go back to the main game menu
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        //play sound effect
        super.playSoundEffect();
    }

    @Override
    public void onSignInSucceeded() {
        MainActivity.displayMessage(this, "Google Play login worked!");

        //flag that we can check achievements
        AchievementHelper.CHECK_ACHIEVEMENTS = true;
    }

    @Override
    public void onSignInFailed() {
        MainActivity.displayMessage(this, "Google play login failed!");

        //don't check achievements, yet
        AchievementHelper.CHECK_ACHIEVEMENTS = false;
    }
}