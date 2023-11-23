package AiController;

import java.util.ArrayList;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.Board.Tuple;

import de.upb.swtpra1819interface.messages.Winner;
import de.upb.swtpra1819interface.models.*;

/**
 * The controller manages the AI. From here, messages are sent to the server
 * using the NetClient and calculations are coordinated.
 */
public interface AiControllerInterface {

	/**
	 * Send a connect request to the server with the client name and client type
	 * (ENGINE_PLAYER)
	 * 
	 * @param clientName is the name of the AI, which is entered at the start of the
	 *                   program in the console
	 */
	public void sendConnectRequest(String clientName);

	/**
	 * This method is called when the client successfully connects to the server.
	 * The client id is saved
	 * 
	 * @param clientId is the id assigned by the server
	 */
	public void loginRequestIsValid(int clientId);

	/**
	 * This method is called if the client was unable to connect to the server.
	 */
	public void loginRequestNotValid();

	/**
	 * Game host assigned the AI ​​to a game
	 */
	public void gameJoinAccepted(Game game);

	/**
	 * Is called when game start, choose an algorithm and create the logic for the
	 * game
	 */
	public void startGame(Configuration config);

	/**
	 * Is called when game stop
	 */
	public void stopGame();

	/**
	 * Is called when game resumes
	 */
	public void resumeGame();

	/**
	 * Is called when game is paused
	 */
	public void pauseGame();

	/**
	 * Is called every time the next player is on Turn
	 * 
	 * @param clientId is the id of the current player
	 */
	public void currentPlayer(int clientId);

	/**
	 * Is called every time the AI get new tiles
	 * 
	 * @param tiles An ArrayList of all tiles added to the AI's hand
	 */
	public void setTilesOnHand(ArrayList<Tile> tiles);

	/**
	 * Is called every time the server set tiles on the map
	 * 
	 * @param tiles A List of all tiles added to the map
	 */
	public void setTilesOnMap(ArrayList<Tuple<Coordinate, Tile>> coordTiles);

	/**
	 * Notify the AI ​​if the swap is valid
	 * 
	 * @param isValid true, if the swap is valid
	 */
	public void tileSwapIsValid();

	/**
	 * Notify the AI ​​if the move is valid
	 * 
	 * @param isValid true, if the move is valid
	 */
	public void moveIsValid(boolean isValid);

	/**
	 * Print the winner of the current game
	 * 
	 * @param winner message from Server with the winner and score
	 */
	public void printWinner(Winner winner);

}
