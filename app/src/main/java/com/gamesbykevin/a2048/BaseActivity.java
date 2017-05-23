package com.gamesbykevin.a2048;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Kevin on 5/22/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public static final String URL_YOUTUBE = "https://youtube.com/gamesbykevin";
    public static final String URL_FACEBOOK = "https://facebook.com/gamesbykevin";
    public static final String URL_TWITTER = "https://twitter.com/gamesbykevin";
    public static final String URL_HELP = "http://gamesbykevin.com";
    public static final String URL_WEBSITE = "http://gamesbykevin.com";
    public static final String URL_RATE = "https://play.google.com/store/apps/details?id=com.gamesbykevin.sokoban";

    public void onClickYoutube(View view) {
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
    protected void openUrl(final String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}