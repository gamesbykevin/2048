package com.gamesbykevin.a2048;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.gamesbykevin.a2048.board.BoardHelper;
import com.gamesbykevin.a2048.game.GameManager;
import com.gamesbykevin.a2048.game.GameManagerHelper;
import com.gamesbykevin.a2048.opengl.OpenGLSurfaceView;
import com.gamesbykevin.a2048.ui.CustomAdapter;
import com.gamesbykevin.a2048.ui.LevelItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class GameActivity extends BaseActivity implements AdapterView.OnItemClickListener {

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

    //has the activity been paused
    private boolean paused = false;

    //our layout parameters
    private LinearLayout.LayoutParams layoutParams;

    //our layout for the game over screen
    private LinearLayout gameOverLayout;

    //access our loading screen
    private ConstraintLayout loadingScreenLayout;

    //the container for our level select
    private TableLayout levelSelectLayout;

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

    //our array list of levels
    private ArrayList<LevelItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //call parent
        super.onCreate(savedInstanceState);

        //create our game manager
        MANAGER = new GameManager(this);

        //set the content view
        setContentView(R.layout.activity_game);

        //obtain our open gl surface view object for reference
        this.glSurfaceView = (OpenGLSurfaceView)findViewById(R.id.openglView);

        //obtain the game over layout so we can choose when to display it
        this.gameOverLayout = (LinearLayout)findViewById(R.id.gameOverLayout);

        //store our object reference to toggle visibility
        this.loadingScreenLayout = (ConstraintLayout)findViewById(R.id.loadingScreenLayout);

        //store the level select layout reference
        this.levelSelectLayout = (TableLayout)findViewById(R.id.levelSelectLayout);

        //get the grid view reference and assign on click
        GridView levelSelectGrid = (GridView)findViewById(R.id.levelSelectGrid);
        levelSelectGrid.setOnItemClickListener(this);

        //get the list of levels
        this.data = MANAGER.getLevelItems().getLevels(
            (GameManager.Mode)getObjectValue(R.string.mode_file_key, GameManager.Mode.class),
            (GameManager.Difficulty)getObjectValue(R.string.difficulty_file_key, GameManager.Difficulty.class)
        );

        //create the custom adapter using the level selection layout and data to populate it
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), R.layout.level_selection, data);

        //set our adapter to the grid view
        levelSelectGrid.setAdapter(customAdapter);

        //default to loading screen
        setStep(Step.Loading);
    }

    @Override
    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id)
    {
        MainActivity.displayMessage(getApplicationContext(), "Clicked : " + data.get(position).getTitle());

        //if puzzle mode, generate board with the specified seed
        if (hasSetting(R.string.mode_file_key, GameManager.Mode.class, GameManager.Mode.Puzzle)) {

            //assign the level selected
            MANAGER.getLevelItems().setLevelIndex(data.get(position).getLevelIndex());

            //reset game
            GameManagerHelper.RESET = true;
        }

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
            ((ViewGroup)gameOverLayout.getParent()).removeView(gameOverLayout);
            ((ViewGroup)loadingScreenLayout.getParent()).removeView(loadingScreenLayout);
            ((ViewGroup)levelSelectLayout.getParent()).removeView(levelSelectLayout);

            //set the content view for our open gl surface view
            setContentView(glSurfaceView);

            //add the layouts to the current content view
            super.addContentView(gameOverLayout, getLayoutParams());
            super.addContentView(loadingScreenLayout, getLayoutParams());
            super.addContentView(levelSelectLayout, getLayoutParams());

        } else {

            //resume the game view
            glSurfaceView.onResume();
        }

        //determine what screens are displayed
        setStep(step);
    }

    /**
     *
     * @return
     */
    public Step getStep() {
        return this.step;
    }

    /**
     *
     * @param step
     */
    public void setStep(final Step step) {

        //assign step
        this.step = step;

        //display the correct screens
        switch (this.step) {

            case Loading:
                setLayoutVisibility(loadingScreenLayout, true);
                setLayoutVisibility(gameOverLayout, false);
                setLayoutVisibility(levelSelectLayout, false);
                break;

            case GameOver:
                setLayoutVisibility(loadingScreenLayout, false);
                setLayoutVisibility(gameOverLayout, true);
                setLayoutVisibility(levelSelectLayout, false);
                break;

            case LevelSelect:
                setLayoutVisibility(loadingScreenLayout, false);
                setLayoutVisibility(gameOverLayout, false);
                setLayoutVisibility(levelSelectLayout, true);
                break;

            case Ready:
                setLayoutVisibility(loadingScreenLayout, false);
                setLayoutVisibility(gameOverLayout, false);
                setLayoutVisibility(levelSelectLayout, false);
                break;
        }
    }

    /**
     *
     * @return
     */
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

        //call parent
        super.onBackPressed();
    }

    /**
     * Reset the board
     * @param view
     */
    public void onClickRestart(View view) {

        //flag the game to reset
        GameManagerHelper.RESET = true;

        //go back to the ready step
        setStep(Step.Ready);

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
}