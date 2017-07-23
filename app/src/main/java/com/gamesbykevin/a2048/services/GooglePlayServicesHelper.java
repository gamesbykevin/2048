package com.gamesbykevin.a2048.services;

import com.gamesbykevin.a2048.activity.GameActivity;
import com.gamesbykevin.a2048.R;
import com.gamesbykevin.a2048.board.Board;
import com.gamesbykevin.a2048.util.UtilityHelper;
import com.gamesbykevin.a2048.game.GameManagerHelper.Mode;

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

        //if empty, no new blocks
        if (NEW_BLOCKS.isEmpty())
            return;

        //if no blocks, we can't unlock any achievements
        boolean valid = false;

        for (int i = 8; i < VALUES.length; i++) {

            //get current value
            Integer value = NEW_BLOCKS.get(VALUES[i]);

            //we need at least one to continue
            if (value != null && value > 0) {
                valid = true;
                break;
            }
        }

        //if no new blocks, don't continue
        if (!valid)
            return;

        //get the number of blocks for each type
        Integer blocks_256 = NEW_BLOCKS.get(VALUES[8]);
        Integer blocks_512 = NEW_BLOCKS.get(VALUES[9]);
        Integer blocks_2048 = NEW_BLOCKS.get(VALUES[11]);
        Integer blocks_4096 = NEW_BLOCKS.get(VALUES[12]);
        Integer blocks_8192 = NEW_BLOCKS.get(VALUES[13]);
        Integer blocks_16384 = NEW_BLOCKS.get(VALUES[14]);
        Integer blocks_32768 = NEW_BLOCKS.get(VALUES[15]);
        Integer blocks_65536 = NEW_BLOCKS.get(VALUES[16]);
        Integer blocks_131072 = NEW_BLOCKS.get(VALUES[17]);
        Integer blocks_262144 = NEW_BLOCKS.get(VALUES[18]);
        Integer blocks_524288 = NEW_BLOCKS.get(VALUES[19]);
        Integer blocks_1048576 = NEW_BLOCKS.get(VALUES[20]);

        //if there was no blocks created, then we don't need to continue
        if ((blocks_256 == null || blocks_256 < 1) &&
            (blocks_512 == null || blocks_512 < 1) &&
            (blocks_2048 == null || blocks_2048 < 1) &&
            (blocks_4096 == null || blocks_4096 < 1) &&
            (blocks_8192 == null || blocks_8192 < 1) &&
            (blocks_16384 == null || blocks_16384 < 1) &&
            (blocks_32768 == null || blocks_32768 < 1) &&
            (blocks_65536 == null || blocks_65536 < 1) &&
            (blocks_131072 == null || blocks_131072 < 1) &&
            (blocks_262144 == null || blocks_262144 < 1) &&
            (blocks_524288 == null || blocks_524288 < 1) &&
            (blocks_1048576 == null || blocks_1048576 < 1))
            return;

        //block progress don't count for puzzle mode for these events
        if (MODE != Mode.Puzzle) {

            //track events for total blocks created
            if (blocks_2048 != null && blocks_2048 > 0)
                activity.trackEvent(R.string.event_2048_block_count, blocks_2048);
            if (blocks_4096 != null && blocks_4096 > 0)
                activity.trackEvent(R.string.event_4096_block_count, blocks_4096);
            if (blocks_8192 != null && blocks_8192 > 0)
                activity.trackEvent(R.string.event_8192_block_count, blocks_8192);
            if (blocks_16384 != null && blocks_16384 > 0)
                activity.trackEvent(R.string.event_16384_block_count, blocks_16384);
            if (blocks_32768 != null && blocks_32768 > 0)
                activity.trackEvent(R.string.event_32768_block_count, blocks_32768);
            if (blocks_65536 != null && blocks_65536 > 0)
                activity.trackEvent(R.string.event_65536_block_count, blocks_65536);
            if (blocks_131072 != null && blocks_131072 > 0)
                activity.trackEvent(R.string.event_131072_block_count, blocks_131072);
            if (blocks_262144 != null && blocks_262144 > 0)
                activity.trackEvent(R.string.event_262144_block_count, blocks_262144);
            if (blocks_524288 != null && blocks_524288 > 0)
                activity.trackEvent(R.string.event_524288_block_count, blocks_524288);
            if (blocks_1048576 != null && blocks_1048576 > 0)
                activity.trackEvent(R.string.event_1048576_block_count, blocks_1048576);
        }

        switch (MODE) {

            //do nothing for puzzle or original mode
            case Puzzle:
            case Original:
                break;

            case Challenge:
                if (blocks_256 != null && blocks_256 > 0)
                    activity.unlockAchievement(R.string.achievement_256_block_mode_challenge);
                if (blocks_512 != null && blocks_512 > 0)
                    activity.unlockAchievement(R.string.achievement_512_block_mode_challenge);
                if (blocks_4096 != null && blocks_4096 > 0)
                    activity.incrementAchievement(R.string.achievement_create_a_4096_block_100_times_mode_challenge_infinite, blocks_4096);
                if (blocks_8192 != null && blocks_8192 > 0)
                    activity.incrementAchievement(R.string.achievement_create_a_8192_block_100_times_mode_challenge_infinite, blocks_8192);
                break;

            case Infinite:

                if (blocks_4096 != null && blocks_4096 > 0) {
                    activity.unlockAchievement(R.string.achievement_4096_block);
                    activity.incrementAchievement(R.string.achievement_create_a_4096_block_100_times_mode_challenge_infinite, blocks_4096);
                }

                if (blocks_8192 != null && blocks_8192 > 0) {
                    activity.unlockAchievement(R.string.achievement_8192_block);
                    activity.incrementAchievement(R.string.achievement_create_a_8192_block_100_times_mode_challenge_infinite, blocks_8192);
                }

                if (blocks_16384 != null && blocks_16384 > 0)
                    activity.unlockAchievement(R.string.achievement_16384_block);
                if (blocks_32768 != null && blocks_32768 > 0)
                    activity.unlockAchievement(R.string.achievement_32768_block);
                if (blocks_65536 != null && blocks_65536 > 0)
                    activity.unlockAchievement(R.string.achievement_65536_block);
                if (blocks_131072 != null && blocks_131072 > 0)
                    activity.unlockAchievement(R.string.achievement_131072_block);
                if (blocks_262144 != null && blocks_262144 > 0)
                    activity.unlockAchievement(R.string.achievement_262144_block);
                if (blocks_524288 != null && blocks_524288 > 0)
                    activity.unlockAchievement(R.string.achievement_524288_block);
                if (blocks_1048576 != null && blocks_1048576 > 0)
                    activity.unlockAchievement(R.string.achievement_1048576_block);
                break;

            default:
                throw new RuntimeException("Mode: " + MODE.toString() + " not handled here");
        }

        //reset back to nothing
        NEW_BLOCKS.clear();
    }
}