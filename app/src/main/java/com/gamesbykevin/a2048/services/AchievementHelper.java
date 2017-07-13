package com.gamesbykevin.a2048.services;

import com.gamesbykevin.a2048.GameActivity;
import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.board.Board;

import static com.gamesbykevin.a2048.board.BoardHelper.BLOCK_1024;
import static com.gamesbykevin.a2048.board.BoardHelper.BLOCK_256;
import static com.gamesbykevin.a2048.board.BoardHelper.BLOCK_4096;
import static com.gamesbykevin.a2048.board.BoardHelper.BLOCK_512;
import static com.gamesbykevin.a2048.board.BoardHelper.BLOCK_8192;
import static com.gamesbykevin.a2048.level.Stats.DIFFICULTY;
import static com.gamesbykevin.a2048.level.Stats.MODE;

/**
 * Created by Kevin on 7/12/2017.
 */

public class AchievementHelper {

    //duration of each difficulty indicating a fast time to resolve
    private static final long PUZZLE_FAST_DURATION_EASY = 5000;
    private static final long PUZZLE_FAST_DURATION_MEDIUM = 15000;
    private static final long PUZZLE_FAST_DURATION_HARD = 30000;

    //can we check achievements
    public static boolean CHECK_ACHIEVEMENTS = false;

    public static void checkAchievementsCompletedGame(GameActivity activity) {

        //wait until we can check achievements
        if (!CHECK_ACHIEVEMENTS)
            return;

        //if we aren't connected to google play services, exit method
        if (!activity.getApiClient().isConnected())
            return;

        MainActivity.logEvent("checkAchievementsCompletedGame");

        switch (MODE) {
            case Original:
                activity.unlockAchievement(R.string.achievement_complete_your_first_game_mode_original);
                activity.incrementAchievement(R.string.achievement_complete_100_games_mode_original, 1);
                break;

            case Challenge:
                activity.unlockAchievement(R.string.achievement_complete_your_first_game_mode_challenge);
                activity.incrementAchievement(R.string.achievement_complete_100_games_mode_challenge, 1);
                break;

            case Puzzle:
                activity.unlockAchievement(R.string.achievement_complete_your_first_game_mode_puzzle);
                activity.incrementAchievement(R.string.achievement_complete_100_games_mode_puzzle, 1);
                break;

            case Infinite:
                activity.unlockAchievement(R.string.achievement_complete_your_first_game_mode_infinite);
                activity.incrementAchievement(R.string.achievement_complete_100_games_mode_infinite, 1);
                break;
        }
    }

    public static void checkAchievementsNewRecord(GameActivity activity) {

        //wait until we can check achievements
        if (!CHECK_ACHIEVEMENTS)
            return;

        //if we aren't connected to google play services, exit method
        if (!activity.getApiClient().isConnected())
            return;

        MainActivity.logEvent("checkAchievementsNewRecord");

        switch (MODE) {
            case Original:
                activity.unlockAchievement(R.string.achievement_beat_your_personal_best_mode_original);
                break;

            case Challenge:
                activity.unlockAchievement(R.string.achievement_beat_your_personal_best_mode_challenge);
                break;

            case Puzzle:
                activity.unlockAchievement(R.string.achievement_beat_your_personal_best_mode_puzzle);
                break;

            case Infinite:
                activity.unlockAchievement(R.string.achievement_beat_your_personal_best_mode_infinite);
                break;
        }
    }

    public static void checkAchievementsPuzzleTime(GameActivity activity, Board board) {

        //wait until we can check achievements
        if (!CHECK_ACHIEVEMENTS)
            return;

        //if we aren't connected to google play services, exit method
        if (!activity.getApiClient().isConnected())
            return;

        MainActivity.logEvent("checkAchievementsPuzzleTime");

        switch (DIFFICULTY) {
            case Easy:
                if (board.getDuration() <= PUZZLE_FAST_DURATION_EASY)
                    activity.unlockAchievement(R.string.achievement_complete_a_game_in_less_than_5_seconds_mode_puzzle);
                break;

            case Medium:
                if (board.getDuration() <= PUZZLE_FAST_DURATION_MEDIUM)
                    activity.unlockAchievement(R.string.achievement_complete_a_game_in_less_than_15_seconds_mode_puzzle);
                break;

            case Hard:
                if (board.getDuration() <= PUZZLE_FAST_DURATION_HARD)
                    activity.unlockAchievement(R.string.achievement_complete_a_game_in_less_than_30_seconds_mode_puzzle);
                break;
        }
    }

    public static void checkAchievementsNewBlocks(GameActivity activity) {

        //wait until we can check achievements
        if (!CHECK_ACHIEVEMENTS)
            return;

        //if we aren't connected to google play services, exit method
        if (!activity.getApiClient().isConnected())
            return;

        //if no blocks, we can't unlock any achievements
        if (BLOCK_256 <= 0 && BLOCK_512 <= 0 && BLOCK_1024 <= 0 && BLOCK_4096 <= 0 && BLOCK_8192 <= 0)
            return;

        MainActivity.logEvent("checkAchievementsNewBlocks");

        switch (MODE) {

            //do nothing for puzzle or original mode
            case Puzzle:
            case Original:
                break;

            case Challenge:
                if (BLOCK_256 > 0)
                    activity.unlockAchievement(R.string.achievement_get_a_256_block_mode_challenge);
                if (BLOCK_512 > 0)
                    activity.unlockAchievement(R.string.achievement_get_a_512_block_mode_challenge);
                if (BLOCK_1024 > 0)
                    activity.unlockAchievement(R.string.achievement_get_a_1024_block_mode_challenge);
                if (BLOCK_4096 > 0)
                    activity.incrementAchievement(R.string.achievement_create_a_4096_block_100_times_mode_challenge_infinite, BLOCK_4096);
                if (BLOCK_8192 > 0)
                    activity.incrementAchievement(R.string.achievement_create_a_8192_block_100_times_mode_challenge_infinite, BLOCK_8192);
                break;

            case Infinite:

                if (BLOCK_4096 > 0) {
                    activity.unlockAchievement(R.string.achievement_get_a_4096_block_mode_infinite);
                    activity.incrementAchievement(R.string.achievement_create_a_4096_block_100_times_mode_challenge_infinite, BLOCK_4096);
                }
                if (BLOCK_8192 > 0) {
                    activity.unlockAchievement(R.string.achievement_get_a_8192_block_mode_infinite);
                    activity.incrementAchievement(R.string.achievement_create_a_8192_block_100_times_mode_challenge_infinite, BLOCK_8192);
                }
                break;

            default:
                throw new RuntimeException("Mode: " + MODE.toString() + " not handled here");
        }
    }
}