package com.gamesbykevin.a2048.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.services.BaseGameActivity;
import com.gamesbykevin.a2048.util.UtilityHelper;

public class MainActivity extends BaseGameActivity {

    /**
     * Do we debug the application?
     */
    public static final boolean DEBUG = true;

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

    public void onClickExit(View view) {

        //no need to bypass login in the future
        BYPASS_LOGIN = false;

        //finish activity
        super.finish();

        //close all activities
        ActivityCompat.finishAffinity(this);

        //log out of google play services
        super.signOut();
    }

    @Override
    public void onSignInSucceeded() {
        //UtilityHelper.displayMessage(this, "Google Play login worked!");

        if (ACCESS_ACHIEVEMENT) {

            //if we just came from achievements button and are now signed in, display ui
            displayAchievementUI();

            //flag back false
            ACCESS_ACHIEVEMENT = false;
        }

        //don't bypass auto login
        BYPASS_LOGIN = false;
    }

    @Override
    public void onSignInFailed() {
        //UtilityHelper.displayMessage(this, "Google play login failed!");

        //bypass auto login
        BYPASS_LOGIN = true;
    }
}