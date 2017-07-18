package com.gamesbykevin.a2048.services;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gamesbykevin.a2048.activity.BaseActivity;
import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.util.UtilityHelper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import static com.gamesbykevin.a2048.activity.MainActivity.DEBUG;
import static com.gamesbykevin.a2048.level.Stats.DIFFICULTY;
import static com.gamesbykevin.a2048.level.Stats.MODE;

/**
 * Created by Kevin on 7/10/2017.
 */

public abstract class BaseGameActivity extends BaseActivity implements GameHelper.GameHelperListener {

    // The game helper object. This class is mainly a wrapper around this object.
    protected GameHelper mHelper;

    // We expose these constants here because we don't want users of this class
    // to have to know about GameHelper at all.
    public static final int CLIENT_GAMES = GameHelper.CLIENT_GAMES;
    public static final int CLIENT_PLUS = GameHelper.CLIENT_PLUS;
    public static final int CLIENT_ALL = GameHelper.CLIENT_ALL;

    // Requested clients. By default, that's just the games client.
    protected int mRequestedClients = CLIENT_GAMES;

    private final static String TAG = "BaseGameActivity";

    //did we want to access the achievements
    protected boolean ACCESS_ACHIEVEMENT = false;

    //did we want to access the leaderboards
    protected boolean ACCESS_LEADERBOARD = false;

    //do we pass future login? (this is in case the player does not want to sign in)
    public static boolean BYPASS_LOGIN = false;

    /** Constructs a BaseGameActivity with default client (GamesClient). */
    protected BaseGameActivity() {
        super();
    }

    /**
     * Constructs a BaseGameActivity with the requested clients.
     * @param requestedClients The requested clients (a combination of CLIENT_GAMES,
     *         CLIENT_PLUS).
     */
    protected BaseGameActivity(int requestedClients) {
        super();
        setRequestedClients(requestedClients);
    }

    /**
     * Sets the requested clients. The preferred way to set the requested clients is
     * via the constructor, but this method is available if for some reason your code
     * cannot do this in the constructor. This must be called before onCreate or getGameHelper()
     * in order to have any effect. If called after onCreate()/getGameHelper(), this method
     * is a no-op.
     *
     * @param requestedClients A combination of the flags CLIENT_GAMES, CLIENT_PLUS
     *         or CLIENT_ALL to request all available clients.
     */
    protected void setRequestedClients(int requestedClients) {
        mRequestedClients = requestedClients;
    }

    public GameHelper getGameHelper() {
        if (mHelper == null) {
            mHelper = new GameHelper(this, mRequestedClients);
            mHelper.enableDebugLog(DEBUG);
        }
        return mHelper;
    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        if (mHelper == null) {
            getGameHelper();
        }
        mHelper.setup(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //only call game helper once
        mHelper.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHelper.onStop();
    }

    @Override
    protected void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        mHelper.onActivityResult(request, response, data);
    }

    public GoogleApiClient getApiClient() {
        return getGameHelper().getApiClient();
    }

    public void unlockAchievement(final int resId) {
        try {
            String achievementId = getString(resId);
            //UtilityHelper.logEvent("Unlocking achievement " + achievementId);
            Games.Achievements.unlock(getApiClient(), achievementId);
            //UtilityHelper.logEvent("Achievement unlocked " + achievementId);
        } catch (Exception e) {
            //UtilityHelper.handleException(e);
        }
    }

    public void incrementAchievement(final int resId, final int incrementValue) {
        try {
            String achievementId = getString(resId);
            //UtilityHelper.logEvent("Incrementing achievement " + achievementId);
            Games.Achievements.increment(getApiClient(), achievementId, incrementValue);
            //UtilityHelper.logEvent("Achievement incremented " + achievementId);
        } catch (Exception e) {
            //UtilityHelper.handleException(e);
        }
    }

    public void trackEvent(final int resId) {
        trackEvent(resId, 1);
    }

    public void trackEvent(final int resId, final int incrementValue) {
        try {
            String eventId = getString(resId);
            //UtilityHelper.logEvent("Tracking event " + eventId);
            Games.Events.increment(getApiClient(), eventId, incrementValue);
            //UtilityHelper.logEvent("Event tracked " + eventId);
        } catch (Exception e) {
            //UtilityHelper.handleException(e);
        }
    }

    public void updateLeaderboard(final int resId, final long score) {
        try {
            String leaderboardId = getString(resId);
            //UtilityHelper.logEvent("Submitting score:  " + leaderboardId);
            Games.Leaderboards.submitScore(getApiClient(), leaderboardId, score);
            //UtilityHelper.logEvent("Score submitted:  " + leaderboardId);
        } catch (Exception e) {
            //UtilityHelper.handleException(e);
        }
    }

    protected boolean isSignedIn() {
        return mHelper.isSignedIn();
    }

    protected void beginUserInitiatedSignIn() {
        mHelper.beginUserInitiatedSignIn();
    }

    protected void signOut() {
        mHelper.signOut();
    }

    protected void showAlert(String message) {
        mHelper.makeSimpleDialog(message).show();
    }

    protected void showAlert(String title, String message) {
        mHelper.makeSimpleDialog(title, message).show();
    }

    protected void enableDebugLog(boolean enabled) {
        if (mHelper != null) {
            mHelper.enableDebugLog(enabled);
        }
    }

    @Deprecated
    protected void enableDebugLog(boolean enabled, String tag) {
        Log.w(TAG, "BaseGameActivity.enabledDebugLog(bool,String) is deprecated. Use enableDebugLog(boolean)");
        enableDebugLog(enabled);
    }

    protected String getInvitationId() {
        return mHelper.getInvitationId();
    }

    protected void reconnectClient() {
        mHelper.reconnectClient();
    }

    protected boolean hasSignInError() {
        return mHelper.hasSignInError();
    }

    protected GameHelper.SignInFailureReason getSignInError() {
        return mHelper.getSignInError();
    }

    public void onClickAchievements(View view) {

        //if we are connected, display default achievements ui
        if (getApiClient().isConnected()) {
            displayAchievementUI();
        } else {
            //UtilityHelper.logEvent("beginUserInitiatedSignIn() before");
            //if not connected, re-attempt google play login
            beginUserInitiatedSignIn();
            //UtilityHelper.logEvent("beginUserInitiatedSignIn() after");

            //flag that we want to open the achievements
            ACCESS_ACHIEVEMENT = true;
        }

        //play sound effect
        super.playSoundEffect();
    }

    public void onClickLeaderboard(View view) {

        //if we are connected, display default achievements ui
        if (getApiClient().isConnected()) {
            displayLeaderboardUI();
        } else {
            //UtilityHelper.logEvent("beginUserInitiatedSignIn() before");
            //if not connected, re-attempt google play login
            beginUserInitiatedSignIn();
            //UtilityHelper.logEvent("beginUserInitiatedSignIn() after");

            //flag that we want to open the achievements
            ACCESS_LEADERBOARD = true;
        }

        //play sound effect
        super.playSoundEffect();
    }

    protected void displayAchievementUI() {
        //UtilityHelper.logEvent("Displaying achievement ui");
        startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), 1);
    }

    protected void displayLeaderboardUI() {

        String leaderboardId = "";

        switch (MODE) {
            case Challenge:

                //display the appropriate leaderboard
                switch (DIFFICULTY) {
                    case Easy:
                        leaderboardId = getString(R.string.leaderboard_challenge_easy);
                        break;

                    case Medium:
                        leaderboardId = getString(R.string.leaderboard_challenge_medium);
                        break;

                    case Hard:
                        leaderboardId = getString(R.string.leaderboard_challenge_hard);
                        break;

                    default:
                        throw new RuntimeException("Difficulty not handled here: " + DIFFICULTY.toString());
                }
                break;

            case Original:

                //display the appropriate leaderboard
                switch (DIFFICULTY) {
                    case Easy:
                        leaderboardId = getString(R.string.leaderboard_original_easy);
                        break;

                    case Medium:
                        leaderboardId = getString(R.string.leaderboard_original_medium);
                        break;

                    case Hard:
                        leaderboardId = getString(R.string.leaderboard_original_hard);
                        break;

                    default:
                        throw new RuntimeException("Difficulty not handled here: " + DIFFICULTY.toString());
                }
                break;

            case Infinite:

                //display the appropriate leaderboard
                switch (DIFFICULTY) {
                    case Easy:
                        leaderboardId = getString(R.string.leaderboard_infinite_easy);
                        break;

                    case Medium:
                        leaderboardId = getString(R.string.leaderboard_infinite_medium);
                        break;

                    case Hard:
                        leaderboardId = getString(R.string.leaderboard_infinite_hard);
                        break;

                    default:
                        throw new RuntimeException("Difficulty not handled here: " + DIFFICULTY.toString());
                }
                break;

            default:
                throw new RuntimeException("Mode not handled here: " + MODE.toString());
        }

        //UtilityHelper.logEvent("Displaying leaderboard ui " + leaderboardId);
        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), leaderboardId), 1);
    }
}