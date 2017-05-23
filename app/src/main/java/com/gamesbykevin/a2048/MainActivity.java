package com.gamesbykevin.a2048;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("2048","onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i("2048","onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("2048","onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("2048","onResume");
    }

    @Override
    public void onBackPressed() {
        //don't do anything, force user to hit android home button or exit button
        return;
    }

    public void onClickStart(View view) {
        //start game
    }

    public void onClickOptions(View view) {
        //start options activity
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public void onClickRate(View view) {
        openUrl(URL_RATE);
    }

    public void onClickMore(View view) {
        openUrl(URL_WEBSITE);
    }

    public void onClickExit(View view) {
        super.finish();
    }
}