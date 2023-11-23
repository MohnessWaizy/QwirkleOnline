package de.upb.cs.swtpra_04.qwirkle.model.lobby;

import de.upb.cs.swtpra_04.qwirkle.R;

/**
 * List of all possible states a game can be in
 */
public enum CurrentGameState {
    RUNNING,
    PAUSED,
    NOT_STARTED,
    FINISHED;

    /**
     * Translate a GameState to the corresponding String resource for the UI
     *
     * @param gameState state to translate
     * @return String resource ID
     */
    public static int getStringRes(CurrentGameState gameState) {
        switch (gameState) {
            case NOT_STARTED:
                return R.string.lobby_game_waitingForPlayers;
            case PAUSED:
                return R.string.lobby_game_paused;
            case RUNNING:
                return R.string.lobby_game_running;
            case FINISHED:
                return R.string.lobby_game_finished;
        }
        return 0;
    }
}

