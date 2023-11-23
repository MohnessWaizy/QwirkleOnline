package de.upb.cs.swtpra_04.qwirkle.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.TileOnPosition;

/**
 * This interface implements the methods for
 */
public interface GameController extends Controller {
    /**
     * displays given message from client in the chat
     *
     * @param client  the client that sends a message
     * @param message the given message
     */
    public void addChatMessage(Client client, String message);

    /**
     * updates board with the new tiles
     *
     * @param updates list of new tiles
     */
    public void updateBoard(List<TileOnPosition> updates);

    /**
     * starts game with given configuration and and client list
     *
     * @param configuration game configuration
     * @param clients       client in the game
     */
    public void startGame(Configuration configuration, ArrayList<Client> clients);

    /**
     * ends game
     */
    public void endGame();

    /**
     * aborts game
     */
    public void abortGame();

    /**
     * pauses game
     */
    public void pauseGame();

    /**
     * resumes game
     */
    public void resumeGame();

    /**
     * gets called when client leaves
     *
     * @param id         clientID
     * @param username   client name
     * @param clientType given client
     */
    public void notifyPlayerLeft(int id, String username, ClientType clientType);

    /**
     *
     *
     * @param time time for turn
     */
    public void turnTimeLeftResponse(long time);

    /**
     *
     * @param time time for response
     */
    public void totalTimeResponse(long time);

    /**
     * update amount of tiles in bag
     *
     * @param number number of tiles in bag
     */
    public void updateBagCount(int number);

    /**
     * sets current player
     *
     * @param client
     */
    public void setCurrentPlayer(Client client);

    /**
     * updates player score
     *
     * @param scores
     */
    public void updateScore(HashMap<Client, Integer> scores);

    /**
     * Puts the game status into the status view
     */
    public void updateStatusView();
}
