package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.upb.swtpra1819interface.messages.GameDataResponse;
import de.upb.swtpra1819interface.messages.Winner;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;

/**
 * interface for FieldSceneController
 *
 */
public interface FieldSceneControllerInterface {

	/**
	 * Shows the winner
	 * 
	 * @param winner
	 */
	public void showWinner(Winner winner);

	/**
	 * Shows the current player and if no other player is pinned, also his hand
	 * 
	 * @param client
	 */
	public void updateCurrentPlayer(Client client);

	/**
	 * Update tiles on the field
	 * 
	 * @param tileOnPositionList
	 */
	public void updateTilesOnField(List<TileOnPosition> tileOnPositionList);

	/**
	 * Update scores of all players
	 * 
	 * @param scores
	 */
	public void updateScores(Map<Client, Integer> scores);

	/**
	 * updates given gameData
	 * 
	 * @param gameData
	 */
	public void updateGameData(GameDataResponse gameData);

	/**
	 * Deletes a player from the panels and the clients list
	 * 
	 * @param client
	 */
	public void playerLeft(Client client);

	/**
	 * Updates the turn time
	 * 
	 * @param time
	 */
	public void updateTurnTime(long time);

	/**
	 * Updates the total time
	 * 
	 * @param time
	 */
	public void updateTotalTime(long time);

	/**
	 * Updates the tiles in the bag's HBox
	 * 
	 * @param tiles
	 */
	public void updateBag(Collection<Tile> tiles);

	/**
	 * Updates the tiles in the player's HBox
	 * 
	 * @param hands
	 */
	public void updatePlayerHand(Map<Client, ArrayList<Tile>> hands);

	/**
	 * pauses game
	 */
	public void pauseGame();

	/**
	 * resumes game
	 */
	public void resumeGame();

	/**
	 * starts game
	 * 
	 * @param collection
	 * @param configuration
	 */
	public void startGame(Collection<Client> collection, Configuration configuration);

	/**
	 * update chat
	 * 
	 * @param client
	 * @param message
	 */
	public void updateChat(Client client, String message);

	/**
	 * aborts game
	 */
	public void abortGame();
}
