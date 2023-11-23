package org.DesktopLogic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import de.upb.swtpra1819interface.messages.GameDataResponse;
import de.upb.swtpra1819interface.messages.Winner;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Game;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.SlowMove;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;
import de.upb.swtpra1819interface.models.WrongMove;
import model.Bag;
import model.GameData;

public class GameDataTest {
	private GameData gameData;
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
	private final static Configuration STANDARDCONFIGURATION = new Configuration(6, 1, 6, 30, 10, WrongMove.NOTHING, 4,
			SlowMove.POINT_LOSS, 0, 2);

	/*@Test
	public void testsetHandsPaused() {
		GameState state = GameState.NOT_STARTED;
		ArrayList<Client> clients = new ArrayList<Client>();
		clients.add(new Client(0, "player1", ClientType.PLAYER));
		clients.add(new Client(1, "player2", ClientType.PLAYER));

		gameData = new GameData(100, clients, state, currentPlayer, bag, winner, hands, board, STANDARDCONFIGURATION,
				previousPlayer);
		gameData.setCurrentPlayer(null);

		HashMap<Client, ArrayList<Tile>> handsToSet = new HashMap<Client, ArrayList<Tile>>();
		ArrayList<Tile> handPlayer1 = new ArrayList<Tile>();
		handPlayer1.add(new Tile(1, 0, 1));

		ArrayList<Tile> handPlayer2 = new ArrayList<Tile>();
		handPlayer2.add(new Tile(0, 0, 2));
		handPlayer2.add(new Tile(1, 1, 3));

		handsToSet.put(gameData.getClients().get(0), handPlayer1);
		handsToSet.put(gameData.getClients().get(1), handPlayer2);

		gameData.setHands(handsToSet);

		TileOnPosition tileOnPosition = new TileOnPosition(0, 0, new Tile(0, 0, 0));
		ArrayList<TileOnPosition> boardGame = new ArrayList<TileOnPosition>();
		boardGame.add(tileOnPosition);
		ArrayList<Tile> ownTiles = gameData.getHands().get(0);
		GameDataResponse gameDataResponse = new GameDataResponse(boardGame, gameData.getClients().get(0), ownTiles,
				GameState.PAUSED);

		assertEquals(gameData.getCurrentPlayer(), null);
		assertNull(gameData.getBoard());
		gameData.importGameData(gameDataResponse);
		assertTrue(gameData.getBoard().size() == 1);
		assertEquals(gameData.getGameState(), GameState.PAUSED);
		assertEquals(gameData.getCurrentPlayer(), gameData.getClients().get(0));

	}*/

	@Test
	public void testsetHandNotPaused() {
		ArrayList<Client> clients = new ArrayList<Client>();
		clients.add(new Client(0, "player1", ClientType.PLAYER));
		clients.add(new Client(1, "player2", ClientType.PLAYER));
		
		Game game = new  Game(100, "Game1", GameState.NOT_STARTED, false, clients, STANDARDCONFIGURATION);

		gameData = new GameData();
		gameData.setGame(game);
		
		gameData.setCurrentPlayer(null);

		HashMap<Client, ArrayList<Tile>> handsToSet = new HashMap<Client, ArrayList<Tile>>();
		ArrayList<Tile> handPlayer1 = new ArrayList<Tile>();
		handPlayer1.add(new Tile(1, 0, 1));

		ArrayList<Tile> handPlayer2 = new ArrayList<Tile>();
		handPlayer2.add(new Tile(0, 0, 2));
		handPlayer2.add(new Tile(1, 1, 3));

		handsToSet.put(gameData.getClients().get(0), handPlayer1);
		handsToSet.put(gameData.getClients().get(1), handPlayer2);

		gameData.setHands(handsToSet);

		TileOnPosition tileOnPosition = new TileOnPosition(0, 0, new Tile(0, 0, 0));
		ArrayList<TileOnPosition> boardGame = new ArrayList<TileOnPosition>();
		boardGame.add(tileOnPosition);
		ArrayList<Tile> ownTiles = gameData.getHands().get(0);
		GameDataResponse gameDataResponse = new GameDataResponse(boardGame, gameData.getClients().get(0), ownTiles,
				GameState.NOT_STARTED);

		assertEquals(gameData.getGameState(), GameState.NOT_STARTED);
		gameData.importGameData(gameDataResponse);
		assertEquals(gameData.getGameState(), GameState.NOT_STARTED);

	}

}
