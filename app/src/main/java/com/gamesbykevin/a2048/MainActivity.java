package com.gamesbykevin.a2048;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends BaseActivity {

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
    protected void onDestroy() {
        super.onDestroy();
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

    public void onClickRate(View view) {

        //open the url
        openUrl(URL_RATE);
    }

    public void onClickMore(View view) {

        //open the url
        openUrl(URL_WEBSITE);
    }

    public void onClickExit(View view) {

        //close activity altogether
        super.finish();
    }

    public static void handleException(final Exception exception) {
        //if not debugging, don't continue
        if (!DEBUG)
            return;

        //handle process
        exception.printStackTrace();
    }

    public static void logEvent(final String message) {

        //if not debugging, don't continue
        if (!DEBUG)
            return;

        //handle process
        Log.i("2048", message);
    }
}