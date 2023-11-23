package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


import de.upb.swtpra1819interface.messages.GameDataResponse;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.messages.Winner;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Game;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.TileOnPosition;


/**
 * holds data for a game
 *
 */
public class GameData {

	private int gameID;
	private ArrayList<Client> clients;
	private GameState gameState;
	private List<TileOnPosition> board;
	private HashMap<Integer, ArrayList<Tile>> hands;
	private HashMap<Client, Integer> scores;
	private Client currentPlayer;
	private Client previousPlayer;
	private Bag bag;
	private Winner winner;
	private Configuration config;

	/**
	 * constructor
	 */
	public GameData() {
		this.clients = new ArrayList<Client>();
		this.board = new ArrayList<TileOnPosition>();
		this.hands = new HashMap<Integer, ArrayList<Tile>>();
		this.scores = new HashMap<Client, Integer>();
		this.bag = new Bag();
		this.gameID = -1;
		this.previousPlayer = new Client(-1, null, null);
	}

	/**
	 * constructor
	 * 
	 * @param gameID
	 * @param clients
	 * @param state
	 * @param currentPlayer
	 * @param bag
	 * @param winner
	 * @param hands
	 * @param board
	 * @param config
	 * @param previousPlayer
	 */
	public GameData(int gameID, ArrayList<Client> clients, GameState state, Client currentPlayer, Bag bag,
			Winner winner, HashMap<Integer, ArrayList<Tile>> hands, List<TileOnPosition> board, Configuration config, Client previousPlayer) {
		this.gameID = gameID;
		this.clients = clients;
		this.gameState = state;
		this.board = board;
		this.currentPlayer = currentPlayer;
		this.bag = bag;
		this.winner = winner;
		this.hands = hands;
		this.config = config;
		this.previousPlayer = previousPlayer;
	}
	
	
	/**
	 * @param game
	 */
	public void setGame(Game game) {
		this.gameID = game.getGameId();
		this.clients = (ArrayList<Client>) game.getPlayers();
		this.gameState = game.getGameState();
		this.config = game.getConfig();
	}

	/**
	 * @return id of the game
	 */
	public int getGameID() {
		return gameID;
	}

	/**
	 * @param gameID
	 */
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	/**
	 * @return clients in game
	 */
	public ArrayList<Client> getClients() {
		return clients;
	}

	/**
	 * @param clients
	 */
	public void setClients(ArrayList<Client> clients) {
		this.clients = clients;
	}

	/**
	 * @return state of game
	 */
	public GameState getGameState() {
		return gameState;
	}

	/**
	 * @param gameState
	 */
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	/**
	 * @return tiles on the board
	 */
	public List<TileOnPosition> getBoard() {
		return board;
	}

	/**
	 * @param board
	 */
	public void setBoard(ArrayList<TileOnPosition> board) {
		this.board = board;
	}
	
	/**
	 * @param tiles
	 */
	public void addBoardTiles(Collection<TileOnPosition> tiles) {
		this.board.addAll(tiles);
	}

	/**
	 * @return hands of a player
	 */
	public HashMap<Integer, ArrayList<Tile>> getHands() {
		return hands;
	}

	/**
	 * @param hands
	 */
	public void setHands(HashMap<Client, ArrayList<Tile>> hands) {
		this.hands = new HashMap<Integer, ArrayList<Tile>>();
		for (Entry<Client, ArrayList<Tile>> entry : hands.entrySet()) {
			this.hands.put(entry.getKey().getClientId(), entry.getValue());
		}
	}

	/**
	 * @return player score
	 */
	public HashMap<Client, Integer> getScores() {
		return scores;
	}

	/**
	 * @param scores
	 */
	public void setScores(HashMap<Client, Integer> scores) {
		this.scores = scores;
	}

	/**
	 * @return current player on turn
	 */
	public Client getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * @param currentPlayer
	 */
	public void setCurrentPlayer(Client currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	/**
	 * @return bag
	 */
	public Bag getBag() {
		return bag;
	}

	/**
	 * @param bag
	 */
	public void setBag(Bag bag) {
		this.bag = bag;
	}
	
	/**
	 * Puts the data in gameDataResponse into the attributes of this class
	 * 
	 * @param gameDataResponse
	 */

	public void importGameData(GameDataResponse gameDataResponse) {
		this.board = (List<TileOnPosition>) gameDataResponse.getBoard();
		this.currentPlayer = gameDataResponse.getCurrentClient();
		this.gameState = gameDataResponse.getGameState();
	}

	/**
	 * @return winner of the game 
	 */
	public Winner getWinner() {
		return winner;
	}

	/**
	 * @param winner
	 */
	public void setWinner(Winner winner) {
		this.winner = winner;
		this.setScores(new HashMap<Client, Integer>(winner.getLeaderboard()));
	}

	/**
	 * @return game configuration
	 */
	public Configuration getConfig() {
		return config;
	}

	/**
	 * @param config
	 */
	public void setConfig(Configuration config) {
		this.config = config;
	}

	/**
	 * @return previous player
	 */
	public Client getPreviousPlayer() {
		return previousPlayer;
	}

	/**
	 * @param previousPlayer
	 */
	public void setPreviousPlayer(Client previousPlayer) {
		this.previousPlayer = previousPlayer;
	}
}
