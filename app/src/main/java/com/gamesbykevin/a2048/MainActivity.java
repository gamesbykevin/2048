package com.gamesbykevin.a2048;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final String URL_YOUTUBE = "https://youtube.com/gamesbykevin";
    public static final String URL_FACEBOOK = "https://facebook.com/gamesbykevin";
    public static final String URL_TWITTER = "https://twitter.com/gamesbykevin";
    public static final String URL_HELP = "http://gamesbykevin.com";
    public static final String URL_WEBSITE = "http://gamesbykevin.com";
    public static final String URL_RATE = "https://play.google.com/store/apps/details?id=com.gamesbykevin.sokoban";

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
        super.onBackPressed();

        Log.i("2048","onBackPressed");
    }

    public void onClickStart(View view) {

    }

    public void onClickOptions(View view) {

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

    public void buttonYoutube(View view) {
        openUrl(URL_YOUTUBE);
    }

    public void onClickFacebook(View view) {
        openUrl(URL_FACEBOOK);
    }

    public void onClickTwitter(View view) {
        openUrl(URL_TWITTER);
    }

    public void onClickHelp(View view) {
        openUrl(URL_HELP);
    }

    /**
     * Open the specified url in the mobile web browser
     * @param url Desired website
     */
    private void openUrl(final String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}