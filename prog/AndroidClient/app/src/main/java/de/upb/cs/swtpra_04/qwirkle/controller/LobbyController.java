package de.upb.cs.swtpra_04.qwirkle.controller;

import java.util.ArrayList;

import de.upb.swtpra1819interface.models.Game;

/**
 * Interface for LobbyActivity
 */
public interface LobbyController extends Controller {
    /**
     * Updates game list with active games
     * Called when server message with newest updates arrives
     *
     * @param games List of all games active at the moment
     */
    void updateGameList(ArrayList<Game> games);

    /**
     * Starts game activity
     * Called when server message arrives
     *
     * @param game the game that will be spectated
     */
    void acceptSpectatorJoinRequest(Game game);

    /**
     * Starts game activity
     * Called when server message arrives
     *
     * @param game the game that will be joined
     */
    void acceptPlayerJoinRequest(Game game);
}
