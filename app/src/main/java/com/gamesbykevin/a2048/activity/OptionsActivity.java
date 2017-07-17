package com.gamesbykevin.a2048.activity;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.game.GameManagerHelper.Difficulty;
import com.gamesbykevin.a2048.game.GameManagerHelper.Mode;
import com.gamesbykevin.a2048.ui.MultiStateToggleButton;
import com.gamesbykevin.a2048.util.UtilityHelper;

public class OptionsActivity extends BaseActivity {

    //our multi state toggle buttons
    private MultiStateToggleButton buttonDifficulty, buttonMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //retrieve our buttons so we can update based on current setting (shared preferences)
        ToggleButton buttonSound = (ToggleButton)findViewById(R.id.ToggleButtonSound);
        ToggleButton buttonVibrate = (ToggleButton)findViewById(R.id.ToggleButtonVibrate);
        this.buttonMode = (MultiStateToggleButton)findViewById(R.id.ToggleButtonMode);
        this.buttonMode.setOptions(Mode.values());
        this.buttonMode.setHeader(getString(R.string.header_option_mode));

        this.buttonDifficulty = (MultiStateToggleButton)findViewById(R.id.ToggleButtonDifficulty);
        this.buttonDifficulty.setOptions(Difficulty.values());
        this.buttonDifficulty.setHeader(getString(R.string.header_option_difficulty));

        //update our buttons accordingly
        buttonSound.setChecked(getBooleanValue(R.string.sound_file_key));
        buttonVibrate.setChecked(getBooleanValue(R.string.vibrate_file_key));
        this.buttonMode.setIndex(getObjectValue(R.string.mode_file_key, Mode.class));
        this.buttonDifficulty.setIndex(getObjectValue(R.string.difficulty_file_key, Difficulty.class));
    }

    /**
     * Override the back pressed so we save the shared preferences
     */
    @Override
    public void onBackPressed() {

        try {

            //get the editor so we can change the shared preferences
            Editor editor = getSharedPreferences().edit();

            //store the sound setting based on the toggle button
            editor.putBoolean(getString(R.string.sound_file_key), ((ToggleButton)findViewById(R.id.ToggleButtonSound)).isChecked());

            //store the vibrate setting based on the toggle button
            editor.putBoolean(getString(R.string.vibrate_file_key), ((ToggleButton)findViewById(R.id.ToggleButtonVibrate)).isChecked());

            //store the mode setting based on the toggle button
            editor.putString(getString(R.string.mode_file_key), GSON.toJson(buttonMode.getValue()));

            //store the difficulty setting
            editor.putString(getString(R.string.difficulty_file_key), GSON.toJson(buttonDifficulty.getValue()));

            //make it final by committing the change
            editor.commit();

        } catch (Exception ex) {

            //handle exception
            UtilityHelper.handleException(ex);
        }

        //call parent function
        super.onBackPressed();
    }

    public void onClickVibrate(View view) {

        //get the button
        ToggleButton button = (ToggleButton)view.findViewById(R.id.ToggleButtonVibrate);

        //if the button is checked we will vibrate the phone
        if (button.isChecked()) {
            super.vibrate(true);
        }

        //play sound effect
        playSoundEffect();
    }

    public void onClickSound(View view) {

        //get the button
        //ToggleButton button = (ToggleButton)view.findViewById(R.id.ToggleButtonSound);

        //play sound effect
        playSoundEffect();
    }

    public void onClickMode(View view) {

        //move to the next option
        this.buttonMode.select();

        //play sound effect
        playSoundEffect();
    }

    public void onClickDifficulty(View view) {

        //move to the next option
        this.buttonDifficulty.select();

        //play sound effect
        playSoundEffect();
    }

    /**
     * Play the sound effect based on the button setting
     */
    @Override
    public void playSoundEffect() {

        //obtain the sound button reference
        ToggleButton button = (ToggleButton)findViewById(R.id.ToggleButtonSound);

        //make sure the sound is enabled before we play
        if (button.isChecked()) {
            getSoundSelection().start();
        }
    }
}