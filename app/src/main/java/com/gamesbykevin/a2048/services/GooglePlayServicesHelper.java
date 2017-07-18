package com.gamesbykevin.a2048.services;

import com.gamesbykevin.a2048.activity.GameActivity;
import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.board.Board;
import com.gamesbykevin.a2048.util.UtilityHelper;

import static com.gamesbykevin.a2048.board.Block.VALUES;
import static com.gamesbykevin.a2048.board.BoardHelper.NEW_BLOCKS;
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

        //UtilityHelper.logEvent("checkAchievementsCompletedGame");

        //update event for games played
        activity.trackEvent(R.string.event_played_games);

        switch (MODE) {

            case Original:

                //update event for games played
                activity.trackEvent(R.string.event_original_games_count);

                //unlock achievements
                activity.unlockAchievement(R.string.achievement_complete_your_first_game_mode_original);
                activity.incrementAchievement(R.string.achievement_complete_100_games_mode_original, 1);

                //make sure the board was actually solved before updating the leaderboard and achievement
                if (board.hasValue(ORIGINAL_MODE_GOAL_VALUE)) {

                    //update the appropriate leaderboard
                    switch (DIFFICULTY) {

                        case Easy:
                            activity.unlockAchievement(R.string.achievement_win_original_easy);
                            activity.updateLeaderboard(R.string.leaderboard_original_easy, board.getDuration());
                            break;

                        case Medium:
                            activity.unlockAchievement(R.string.achievement_win_original_medium);
                            activity.updateLeaderboard(R.string.leaderboard_original_medium, board.getDuration());
                            break;

                        case Hard:
                            activity.unlockAchievement(R.string.achievement_win_original_hard);
                            activity.updateLeaderboard(R.string.leaderboard_original_hard, board.getDuration());
                            break;
                    }
                }
                break;

            case Challenge:

                //update event for games played
                activity.trackEvent(R.string.event_challenge_games_count);

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

                //update event for games played
                activity.trackEvent(R.string.event_puzzle_games_count);

                //unlock achievements
                activity.unlockAchievement(R.string.achievement_complete_your_first_game_mode_puzzle);
                activity.incrementAchievement(R.string.achievement_complete_100_games_mode_puzzle, 1);
                break;

            case Infinite:

                //update event for games played
                activity.trackEvent(R.string.event_infinite_games_count);

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

        //UtilityHelper.logEvent("checkAchievementsNewRecord");

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

        //UtilityHelper.logEvent("checkAchievementsPuzzleTime");

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
        boolean valid = false;

        for (int i = 8; i < VALUES.length; i++) {
            if (NEW_BLOCKS.get(VALUES[i]) > 0)
                valid = true;
        }

        //if no new blocks, don't continue
        if (!valid)
            return;

        //track events for total blocks created
        if (NEW_BLOCKS.get(VALUES[11]) > 0)
            activity.trackEvent(R.string.event_2048_block_count, NEW_BLOCKS.get(VALUES[11]));
        if (NEW_BLOCKS.get(VALUES[12]) > 0)
            activity.trackEvent(R.string.event_4096_block_count, NEW_BLOCKS.get(VALUES[12]));
        if (NEW_BLOCKS.get(VALUES[13]) > 0)
            activity.trackEvent(R.string.event_8192_block_count, NEW_BLOCKS.get(VALUES[13]));
        if (NEW_BLOCKS.get(VALUES[14]) > 0)
            activity.trackEvent(R.string.event_16384_block_count, NEW_BLOCKS.get(VALUES[14]));
        if (NEW_BLOCKS.get(VALUES[15]) > 0)
            activity.trackEvent(R.string.event_32768_block_count, NEW_BLOCKS.get(VALUES[15]));
        if (NEW_BLOCKS.get(VALUES[16]) > 0)
            activity.trackEvent(R.string.event_65536_block_count, NEW_BLOCKS.get(VALUES[16]));
        if (NEW_BLOCKS.get(VALUES[17]) > 0)
            activity.trackEvent(R.string.event_131072_block_count, NEW_BLOCKS.get(VALUES[17]));
        if (NEW_BLOCKS.get(VALUES[18]) > 0)
            activity.trackEvent(R.string.event_262144_block_count, NEW_BLOCKS.get(VALUES[18]));
        if (NEW_BLOCKS.get(VALUES[19]) > 0)
            activity.trackEvent(R.string.event_524288_block_count, NEW_BLOCKS.get(VALUES[19]));
        if (NEW_BLOCKS.get(VALUES[20]) > 0)
            activity.trackEvent(R.string.event_1048576_block_count, NEW_BLOCKS.get(VALUES[20]));

        switch (MODE) {

            //do nothing for puzzle or original mode
            case Puzzle:
            case Original:
                break;

            case Challenge:
                if (NEW_BLOCKS.get(VALUES[8]) > 0)
                    activity.unlockAchievement(R.string.achievement_256_block_mode_challenge);
                if (NEW_BLOCKS.get(VALUES[9]) > 0)
                    activity.unlockAchievement(R.string.achievement_512_block_mode_challenge);
                if (NEW_BLOCKS.get(VALUES[12]) > 0)
                    activity.incrementAchievement(R.string.achievement_create_a_4096_block_100_times_mode_challenge_infinite, NEW_BLOCKS.get(VALUES[12]));
                if (NEW_BLOCKS.get(VALUES[13]) > 0)
                    activity.incrementAchievement(R.string.achievement_create_a_8192_block_100_times_mode_challenge_infinite, NEW_BLOCKS.get(VALUES[13]));
                break;

            case Infinite:

                if (NEW_BLOCKS.get(VALUES[12]) > 0) {
                    activity.unlockAchievement(R.string.achievement_4096_block);
                    activity.incrementAchievement(R.string.achievement_create_a_4096_block_100_times_mode_challenge_infinite, NEW_BLOCKS.get(VALUES[12]));
                }

                if (NEW_BLOCKS.get(VALUES[13]) > 0) {
                    activity.unlockAchievement(R.string.achievement_8192_block);
                    activity.incrementAchievement(R.string.achievement_create_a_8192_block_100_times_mode_challenge_infinite, NEW_BLOCKS.get(VALUES[13]));
                }

                if (NEW_BLOCKS.get(VALUES[14]) > 0)
                    activity.unlockAchievement(R.string.achievement_16384_block);
                if (NEW_BLOCKS.get(VALUES[15]) > 0)
                    activity.unlockAchievement(R.string.achievement_32768_block);
                if (NEW_BLOCKS.get(VALUES[16]) > 0)
                    activity.unlockAchievement(R.string.achievement_65536_block);
                if (NEW_BLOCKS.get(VALUES[17]) > 0)
                    activity.unlockAchievement(R.string.achievement_131072_block);
                if (NEW_BLOCKS.get(VALUES[18]) > 0)
                    activity.unlockAchievement(R.string.achievement_262144_block);
                if (NEW_BLOCKS.get(VALUES[19]) > 0)
                    activity.unlockAchievement(R.string.achievement_524288_block);
                if (NEW_BLOCKS.get(VALUES[20]) > 0)
                    activity.unlockAchievement(R.string.achievement_1048576_block);
                break;

            default:
                throw new RuntimeException("Mode: " + MODE.toString() + " not handled here");
        }

        //reset count back to 0
        for (int i = 0; i < VALUES.length; i++) {
            NEW_BLOCKS.put(VALUES[i], 0);
        }
    }
}