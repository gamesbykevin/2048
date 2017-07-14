package com.gamesbykevin.a2048.services;

import com.gamesbykevin.a2048.GameActivity;
import com.gamesbykevin.a2048.MainActivity;
import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.board.Board;
import com.gamesbykevin.a2048.util.UtilityHelper;

import static com.gamesbykevin.a2048.board.BoardHelper.BLOCK_1024;
import static com.gamesbykevin.a2048.board.BoardHelper.BLOCK_256;
import static com.gamesbykevin.a2048.board.BoardHelper.BLOCK_4096;
import static com.gamesbykevin.a2048.board.BoardHelper.BLOCK_512;
import static com.gamesbykevin.a2048.board.BoardHelper.BLOCK_8192;
import static com.gamesbykevin.a2048.game.GameManagerHelper.ORIGINAL_MODE_GOAL_VALUE;
import static com.gamesbykevin.a2048.level.Stats.DIFFICULTY;
import static com.gamesbykevin.a2048.level.Stats.MODE;

/**
 * Created by Kevin on 7/12/2017.
 */

public class GooglePlayServicesHelper {

    //duration of each difficulty indicating a fast time to resolve
    private static final long PUZZLE_FAST_DURATION_EASY = 5000;
    private static final long PUZZLE_FAST_DURATION_MEDIUM = 15000;
    private static final long PUZZLE_FAST_DURATION_HARD = 30000;

    public static void checkCompletedGame(GameActivity activity, Board board) {

        //if we aren't connected to google play services, exit method
        if (!activity.getApiClient().isConnected())
            return;

        UtilityHelper.logEvent("checkAchievementsCompletedGame");

        switch (MODE) {

            case Original:

                //make sure the board was actually solved before updating the leaderboard and achievements
                if (board.hasValue(ORIGINAL_MODE_GOAL_VALUE)) {

                    //unlock achievements
                    activity.unlockAchievement(R.string.achievement_complete_your_first_game_mode_original);
                    activity.incrementAchievement(R.string.achievement_complete_100_games_mode_original, 1);

                    //update the appropriate leaderboard
                    switch (DIFFICULTY) {

                        case Easy:
                            activity.updateLeaderboard(R.string.leaderboard_original_easy, board.getDuration());
                            break;

                        case Medium:
                            activity.updateLeaderboard(R.string.leaderboard_original_medium, board.getDuration());
                            break;

                        case Hard:
                            activity.updateLeaderboard(R.string.leaderboard_original_hard, board.getDuration());
                            break;
                    }
                }
                break;

            case Challenge:

                //unlock achievements
                activity.unlockAchievement(R.string.achievement_complete_your_first_game_mode_challenge);
                activity.incrementAchievement(R.string.achievement_complete_100_games_mode_challenge, 1);

                //update the appropriate leaderboard
                switch (DIFFICULTY) {

                    case Easy:
                        activity.updateLeaderboard(R.string.leaderboard_challenge_easy, board.getScore());
                        break;

                    case Medium:
                        activity.updateLeaderboard(R.string.leaderboard_challenge_medium, board.getScore());
                        break;

                    case Hard:
                        activity.updateLeaderboard(R.string.leaderboard_challenge_hard, board.getScore());
                        break;
                }
                break;

            case Puzzle:

                //unlock achievements
                activity.unlockAchievement(R.string.achievement_complete_your_first_game_mode_puzzle);
                activity.incrementAchievement(R.string.achievement_complete_100_games_mode_puzzle, 1);
                break;

            case Infinite:

                //unlock achievements
                activity.unlockAchievement(R.string.achievement_complete_your_first_game_mode_infinite);
                activity.incrementAchievement(R.string.achievement_complete_100_games_mode_infinite, 1);

                //update the appropriate leaderboard
                switch (DIFFICULTY) {

                    case Easy:
                        activity.updateLeaderboard(R.string.leaderboard_infinite_easy, board.getScore());
                        break;

                    case Medium:
                        activity.updateLeaderboard(R.string.leaderboard_infinite_medium, board.getScore());
                        break;

                    case Hard:
                        activity.updateLeaderboard(R.string.leaderboard_infinite_hard, board.getScore());
                        break;
                }
                break;
        }
    }

    public static void checkAchievementsNewRecord(GameActivity activity) {

        //if we aren't connected to google play services, exit method
        if (!activity.getApiClient().isConnected())
            return;

        UtilityHelper.logEvent("checkAchievementsNewRecord");

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

        //if we aren't connected to google play services, exit method
        if (!activity.getApiClient().isConnected())
            return;

        UtilityHelper.logEvent("checkAchievementsPuzzleTime");

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

        //if we aren't connected to google play services, exit method
        if (!activity.getApiClient().isConnected())
            return;

        //if no blocks, we can't unlock any achievements
        if (BLOCK_256 <= 0 && BLOCK_512 <= 0 && BLOCK_1024 <= 0 && BLOCK_4096 <= 0 && BLOCK_8192 <= 0)
            return;

        //UtilityHelper.logEvent("checkAchievementsNewBlocks");

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

        BLOCK_256 = 0;
        BLOCK_512 = 0;
        BLOCK_1024 = 0;
        BLOCK_4096 = 0;
        BLOCK_8192 = 0;
    }
}