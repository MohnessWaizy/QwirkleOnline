package de.upb.cs.swtpra_04.qwirkle.controller;

import java.util.HashMap;

import de.upb.swtpra1819interface.models.Client;

/**
 * controller for finished game
 */
public interface GameFinishedController extends Controller {

    /**
     * Sets the Winner information in the Activity
     * @param id Winner id
     * @param username Winner username
     * @param score Score
     */
    public void setWinner(int id, String username, int score);

    /**
     * Sets the leaderboard in the Activity
     * @param leaderboard
     */
    public void setLeaderboard(HashMap<Client, Integer> leaderboard);

    /**
     * Called to show the information
     */
    public void showScene();
}
