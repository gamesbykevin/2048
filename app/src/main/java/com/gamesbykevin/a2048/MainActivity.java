package com.gamesbykevin.a2048;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gamesbykevin.a2048.game.GameManagerHelper;
import com.gamesbykevin.a2048.services.BaseGameActivity;
import com.gamesbykevin.a2048.services.GameHelper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

public class MainActivity extends BaseGameActivity {

    /**
     * Do we debug the application?
     */
    public static final boolean DEBUG = true;

    //did we access the achievements
    private boolean ACCESS_ACHIEVEMENT = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //log out of google play services
        super.signOut();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        //don't do anything, force user to hit android home button or exit button
        return;
    }

    public void onClickStart(View view) {

        //start game
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void onClickOptions(View view) {

        //start options activity
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);

        //play sound effect
        super.playSoundEffect();
    }

    public void onClickAchievements(View view) {

        //if we are connected, display default achievements ui
        if (getApiClient().isConnected()) {
            displayAchievementUI();
        } else {
            //if not connected, re-attempt google play login
            beginUserInitiatedSignIn();

            //flag that we want to open the achievements
            ACCESS_ACHIEVEMENT = true;
        }

        //play sound effect
        super.playSoundEffect();
    }

    private void displayAchievementUI() {
        startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), 1);
    }

    public void onClickExit(View view) {

        //close all activities
        ActivityCompat.finishAffinity(this);

        //log out of google play services
        super.signOut();
    }

    public static void handleException(final Exception exception) {

        //if not debugging, don't continue
        if (!DEBUG)
            return;

        //log as error
        Log.e("2048", exception.getMessage());

        //handle process
        exception.printStackTrace();
    }

    public static void logEvent(final String message) {

        //if not debugging, don't continue
        if (!DEBUG)
            return;

        //don't do anything if null
        if (message == null)
            return;

        //length limit of each line we print
        int maxLogSize = 4000;

        //if the string is too long
        if (message.length() > maxLogSize) {

            //we will display a portion at a time
            for(int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i+1) * maxLogSize;
                end = end > message.length() ? message.length() : end;
                Log.i("2048", message.substring(start, end));
            }

        } else {
            //log string as information
            Log.i("2048", message);
        }
    }

    public static void displayMessage(final Context context, final String message) {

        //if not debugging, don't continue
        if (!DEBUG)
            return;

        //show text
        Toast.makeText(context, message , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignInSucceeded() {
        MainActivity.displayMessage(this, "Google Play login worked!");

        if (ACCESS_ACHIEVEMENT) {

            //if we just came from achievements button and are now signed in, show achievements ui
            displayAchievementUI();

            //flag back false
            ACCESS_ACHIEVEMENT = false;
        }
    }

    @Override
    public void onSignInFailed() {
        MainActivity.displayMessage(this, "Google play login failed!");
    }
}