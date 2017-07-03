package com.gamesbykevin.a2048;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

public class OptionsActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //retrieve our buttons so we can update based on current setting (shared preferences)
        ToggleButton buttonSound = (ToggleButton)findViewById(R.id.ToggleButtonSound);
        ToggleButton buttonVibrate = (ToggleButton)findViewById(R.id.ToggleButtonVibrate);
        ToggleButton buttonMode = (ToggleButton)findViewById(R.id.ToggleButtonMode);

        //update our buttons accordingly
        buttonSound.setChecked(getBooleanValue(getString(R.string.sound_file_key)));
        buttonVibrate.setChecked(getBooleanValue(getString(R.string.vibrate_file_key)));
        buttonMode.setChecked(getBooleanValue(getString(R.string.mode_file_key)));
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
            editor.putBoolean(getString(R.string.mode_file_key), ((ToggleButton)findViewById(R.id.ToggleButtonMode)).isChecked());

            //make it final by committing the change
            editor.commit();

        } catch (Exception ex) {
            //print stack trace
            ex.printStackTrace();
        }

        //call parent function
        super.onBackPressed();
    }

    /**
     * Handle the button click
     * @param view Current view
     */
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

    /**
     * Handle the button click
     * @param view Current view
     */
    public void onClickSound(View view) {

        //get the button
        //ToggleButton button = (ToggleButton)view.findViewById(R.id.ToggleButtonSound);

        //play sound effect
        playSoundEffect();
    }

    /**
     * Handle the button click
     * @param view Current view
     */
    public void onClickMode(View view) {

        //play sound effect
        playSoundEffect();
    }

    /**
     * Play the sound effect based on the button setting
     */
    @Override
    public void playSoundEffect() {
        ToggleButton button = (ToggleButton)findViewById(R.id.ToggleButtonSound);

        //make sure the sound is enabled before we play
        if (button.isChecked()) {
            getSoundSelection().start();
        }
    }
}