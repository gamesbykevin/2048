package com.gamesbykevin.a2048;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static com.gamesbykevin.a2048.MainActivity.DEBUG;

/**
 * Created by Kevin on 5/22/2017.
 */
public abstract class BaseActivity extends AppCompatActivity {

    //our social media urls etc....
    public static final String URL_YOUTUBE = "https://youtube.com/gamesbykevin";
    public static final String URL_FACEBOOK = "https://facebook.com/gamesbykevin";
    public static final String URL_TWITTER = "https://twitter.com/gamesbykevin";
    public static final String URL_HELP = "http://gamesbykevin.com";
    public static final String URL_WEBSITE = "http://gamesbykevin.com";
    public static final String URL_RATE = "https://play.google.com/store/apps/details?id=com.gamesbykevin.sokoban";

    //where we store our game option selections
    private static SharedPreferences preferences;

    //our vibrate object
    private static Vibrator vibrator;

    //the object to play the sound selection
    private static MediaPlayer soundSelection;

    //the intent used to open web urls
    private Intent intent;

    /**
     * The default duration of the vibration
     */
    public static final long VIBRATE_DURATION = 600L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //call parent
        super.onCreate(savedInstanceState);

        //get our shared preferences object
        if (this.preferences == null)
            this.preferences = super.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        //get our vibrator object
        if (this.vibrator == null)
            this.vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        //get the object to play the sound effect
        if (this.soundSelection == null)
            this.soundSelection = MediaPlayer.create(this, R.raw.selection);
    }

    /**
     * Get the sound selection object
     * @return The media player object to play the sound
     */
    protected MediaPlayer getSoundSelection() {
        return this.soundSelection;
    }

    /**
     * Get the shared preferences
     * @return The object where we store our game options
     */
    public static SharedPreferences getSharedPreferences() {
        return preferences;
    }

    /**
     *
     * @return
     */
    public boolean isDifficultyEasy() {
        return hasDifficulty(0);
    }

    /**
     *
     * @return
     */
    public boolean isDifficultyMedium() {
        return hasDifficulty(1);
    }

    /**
     *
     * @return
     */
    public boolean isDifficultyHard() {
        return hasDifficulty(2);
    }

    /**
     *
     * @param index
     * @return
     */
    private boolean hasDifficulty(final int index) {
        return (getIntegerValue(getString(R.string.difficulty_file_key)) == index);
    }

    /**
     * Get the boolean value of the shared preferences setting
     * @param key The unique key of the setting we want to retrieve
     * @return The value of the setting for the corresponding key, if not set true will be returned by default
     */
    public boolean getBooleanValue(final String key) {
        return getSharedPreferences().getBoolean(key, true);
    }

    /**
     *
     * @param key
     * @return
     */
    public int getIntegerValue(final String key) {
        return getSharedPreferences().getInt(key, -1);
    }

    /**
     * Vibrate the phone for the default duration, if vibrate is enabled in shared preferences
     */
    public void vibrate() {
        vibrate(VIBRATE_DURATION, false);
    }

    /**
     * Vibrate the phone for the default duration
     * @param ignoreSetting Do we ignore the shared preferences vibrate setting?
     */
    public void vibrate(boolean ignoreSetting) {
        vibrate(VIBRATE_DURATION, ignoreSetting);
    }

    /**
     * Vibrate the phone for the specified duration
     * @param milliseconds The duration of the vibrate
     * @param ignoreSetting Do we ignore the shared preferences vibrate setting?
     */
    public void vibrate(long milliseconds, boolean ignoreSetting) {

        if (ignoreSetting) {
            //if we ignore the shared preferences setting vibrate anyways
            vibrator.vibrate(milliseconds);
        } else if (!ignoreSetting && getBooleanValue(getString(R.string.vibrate_file_key))) {
            //if we want to honor the shared preferences setting and it is enabled
            vibrator.vibrate(milliseconds);
        }

        //only vibrate if the setting is turned on
        if (ignoreSetting || getBooleanValue(getString(R.string.vibrate_file_key))) {
            //vibrate the phone for the specified duration
            vibrator.vibrate(milliseconds);
        }
    }

    /**
     * Play the sound effect based on the shared preferences setting
     */
    public void playSoundEffect() {

        try {
            //make sure the sound is enabled via shared preferences
            if (getBooleanValue(getString(R.string.sound_file_key))) {

                //start playing sound
                getSoundSelection().start();
            }
        } catch (Exception e) {
            MainActivity.handleException(e);
        }
    }

    /**
     * Open the youtube web page
     * @param view Current view
     */
    public void onClickYoutube(View view) {
        openUrl(URL_YOUTUBE);
    }

    /**
     * Open the facebook web page
     * @param view Current view
     */
    public void onClickFacebook(View view) {
        openUrl(URL_FACEBOOK);
    }

    /**
     * Open the twitter web page
     * @param view Current view
     */
    public void onClickTwitter(View view) {
        openUrl(URL_TWITTER);
    }

    /**
     * Open the help page
     * @param view Current view
     */
    public void onClickHelp(View view) {
        openUrl(URL_HELP);
    }

    public void onClickRate(View view) {

        //open the url
        openUrl(URL_RATE);
    }

    public void onClickMore(View view) {

        //open the url
        openUrl(URL_WEBSITE);
    }

    /**
     * Open the specified url in the mobile web browser
     * @param url Desired website
     */
    protected void openUrl(final String url) {

        //play sound effect
        playSoundEffect();

        //if not established create the intent
        if (this.intent == null) {
            this.intent = new Intent(Intent.ACTION_VIEW);
        }

        //set the url
        this.intent.setData(Uri.parse(url));

        //start the activity opening the app / web browser
        startActivity(this.intent);
    }
}