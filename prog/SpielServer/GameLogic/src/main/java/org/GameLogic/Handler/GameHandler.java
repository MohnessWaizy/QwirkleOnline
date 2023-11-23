package org.GameLogic.Handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.GameLogic.Board.Bag;
import org.GameLogic.Board.Coordinate;
import org.GameLogic.Board.MapLogic;
import org.GameLogic.Clock.Clockable;
import org.GameLogic.Clock.GameClock;
import org.GameLogic.Communication.Communication;
import org.GameLogic.Communication.CommunicationHandlerCallback;
import org.GameLogic.DataStructures.CycleList;
import org.GameLogic.DataStructures.Player;
import org.GameLogic.DataStructures.Spectator;
import org.GameLogic.DataStructures.Tuple;
import org.GameLogic.Exceptions.ErrorType;
import org.GameLogic.Exceptions.NotAllowedException;
import org.GameLogic.Exceptions.ParserException;
import org.GameLogic.Util.GenerateResponse;

import de.upb.swtpra1819interface.models.*;
import de.upb.swtpra1819interface.messages.*;

public class GameHandler implements Clockable, Communication {

	/**
	 * Configuration for this game
	 */
	private Configuration config;
	/**
	 * Bag instance to hold tiles
	 */
	private Bag bag;
	/**
	 * MapLogic instance to play tiles
	 */
	private MapLogic mapLogic;
	/**
	 * Gamestate
	 */
	private GameState gameState;
	/**
	 * Spectators of this game
	 */

	private List<Spectator> spectators;
	/**
	 * Players of this game
	 */
	private CycleList<Player> players;
	/**
	 * Stopwatch
	 */
	private GameClock gameClock;
	/**
	 * Callback to CommunicationHandler
	 */
	private CommunicationHandlerCallback chc;

	/**
	 * Constructs a game. The starting gameState will be <i>NOT_STARTED</i>.
	 * 
	 * @param callback
	 *            Callback to CommunicationHandler
	 * @param config
	 *            Configuration for this game
	 */
	public GameHandler(CommunicationHandlerCallback callback, Configuration config) {
		// callback
		this.chc = callback;

		// set configuration
		this.config = config;

		// set GameState to not_started
		this.gameState = GameState.NOT_STARTED;

		// initialize player list
		this.players = new CycleList<Player>(config.getMaxPlayerNumber());

		// initialize Stack
		this.bag = new Bag(config.getColorShapeCount(), config.getTileCount(), config.getMaxHandTiles());

		// build gameClockhandler
		this.gameClock = new GameClock(this, config.getTurnTime(), config.getTimeVisualization());

		// create MapLogic
		this.mapLogic = new MapLogic(config.getColorShapeCount());

		// create Spectator list
		this.spectators = new ArrayList<Spectator>();
	}

	/*
	 * STRUCTURE : ADD/REMOVE CLIENTS
	 */

	/**
	 * Search for a Player
	 * 
	 * @param client
	 *            Client
	 * @return Player assigned to the client or null if not existent
	 */
	private Player getPlayerByClient(Client client) {

		// most of the times, it is the current or next player
		if (players.current() != null && players.current().getClient().equals(client)) {
			return players.current();
		} else if (players.peekNext() != null && players.peekNext().getClient().equals(client)) {
			return players.peekNext();
		}

		// it is another player
		for (Player player : players) {
			if (player.getClient().equals(client)) {
				return player;
			}
		}
		return null;
	}

	/**
	 * Search for a Spectator
	 * 
	 * @param client
	 *            Client
	 * @return Spectator assigned to the client or null if not existent
	 */
	private Spectator getSpectatorByClient(Client client) {
		for (Spectator spectator : spectators) {
			if (spectator.getClient().equals(client)) {
				return spectator;
			}
		}
		return null;
	}

	@Override
	public void requestJoin(Client client) throws NotAllowedException {

		if (client == null) {
			throw new NotAllowedException("Leerer Client");
		}

		if (client.getClientType() == ClientType.PLAYER) {
			if (gameState == GameState.NOT_STARTED && getPlayerByClient(client) == null) {
				if (config.getMaxPlayerNumber() == 0 || config.getMaxPlayerNumber() > players.size()) {
					players.add(new Player(client));
				} else {
					throw new NotAllowedException("Zu viele Spieler");
				}
			} else {
				throw new NotAllowedException("Spieler schon im Spiel");
			}
		} else {
			if (getSpectatorByClient(client) == null) {
				spectators.add(new Spectator(client));
			} else {
				throw new NotAllowedException("Beobachter schon im Spiel");
			}
		}
	}

	@Override
	public void requestRemove(Client client) {

		if (client.getClientType() == ClientType.PLAYER) {
			Player player = getPlayerByClient(client);
			if (player != null) {
				// put players tiles back into the bag
				bag.refill(new ArrayList<Tile>(player.getTiles()));
				// remove player from cycle list
				players.remove(player);
				if (gameState == GameState.IN_PROGRESS && players.size() <= 1) {
					// End game
					gameState = GameState.ENDED;
					// Destroy Clock
					gameClock.destroy();
					// Notify
					chc.callbackGameFinish(true);
				}
			}
		} else {
			Spectator spec = getSpectatorByClient(client);
			if (spec != null) {
				spectators.remove(spec);
			}
		}

	}

	/*
	 * START / STOP / ABORT
	 */

	/**
	 * Starts a game
	 * 
	 * @throws NotAllowedException
	 *             If the player count is 0 or 1
	 */
	public void startGame() throws NotAllowedException {
		if (players.size() <= 1) {
			throw new NotAllowedException("Mehrere Spieler nötig ");
		} else if (gameState != GameState.NOT_STARTED) {
			throw new NotAllowedException("Spiel ist schon zuende ");
		}

		this.gameState = GameState.IN_PROGRESS;

		/*
		 * Give starting tiles to each player
		 */
		for (Player player : players) {
			List<Tile> startTiles = bag.takeStartTiles();
			player.addTiles(startTiles);
		}

	}

	/**
	 * Starts the clock
	 */
	public void startGameClock() {
		gameClock.startTicking();
	}

	/**
	 * End the game
	 * 
	 * @throws NotAllowedException
	 *             If the game is already ended
	 */
	public void endGame() throws NotAllowedException {

		if (gameState == GameState.ENDED) {
			throw new NotAllowedException("Spiel ist schon zuende");
		}

		gameState = GameState.ENDED;

		gameClock.destroy();

	}

	/**
	 * Paused a game
	 * 
	 * @throws NotAllowedException
	 *             If a gameState is not in <i>IN_PROGRESS</I>.
	 */
	public void pauseGame() throws NotAllowedException {
		if (gameState != GameState.IN_PROGRESS) {
			throw new NotAllowedException("Spiel ist momentan nicht am laufen");
		}
		gameState = GameState.PAUSED;
		gameClock.pauseTicking();
	}

	/**
	 * Resume a game
	 * 
	 * @throws NotAllowedException
	 *             If a gameState is not in <i>PAUSED</I>.
	 */
	public void resumeGame() throws NotAllowedException {
		if (gameState != GameState.PAUSED) {
			throw new NotAllowedException("Spiel ist momentan nicht pausiert");
		}
		gameState = GameState.IN_PROGRESS;
		gameClock.resumeTicking();
	}

	/*
	 * PENALTIES
	 */

	/**
	 * Handels wrong moves accordingly to the configuration
	 */
	private void checkWrongMove() {
		if (config.getWrongMove() == WrongMove.KICK) {

			chc.callbackPlayerKicked(players.current());

			requestRemove(players.current().getClient());

			gameClock.turnTimeOver();

		} else if (config.getWrongMove() == WrongMove.POINT_LOSS) {

			players.current().modifyScore(config.getWrongMovePenalty() * -1);
			gameClock.turnTimeOver();

		}
	}

	/*
	 * Interface Methods
	 */

	@Override
	public Winner requestWinner() throws NotAllowedException {
		if (gameState == GameState.ENDED) {

			Player bestPlayer = players.stream().max(new Comparator<Player>() {
				public int compare(Player p1, Player p2) {
					return Integer.compare(p1.getScore(), p2.getScore());
				}
			}).get();

			return GenerateResponse.constructWinner(bestPlayer, players);

		} else {
			throw new NotAllowedException("Spiel noch nicht zuende");
		}

	}

	@Override
	public Player requestCurrentPayer() {
		return players.current();
	}

	@Override
	public void requestPlayTiles(Client client, List<Tuple<Coordinate, Tile>> tilesWithCoordinate)
			throws NotAllowedException, ParserException {

		if (!validateTilesOnPosition(tilesWithCoordinate)) {
			if (players.current().getClient().equals(client)) {
				// player on turn will get move valid false
				throw new ParserException(ErrorType.ValidationError, "Einer der Steine nicht bekannt");
			} else {
				throw new ParserException(ErrorType.BadRequestError, "Einer der Steine nicht bekannt");
			}
		}

		List<Tile> tiles = new ArrayList<Tile>();
		for (Tuple<Coordinate, Tile> tct : tilesWithCoordinate) {
			tiles.add(tct.getSecond());
		}
		if (gameState == GameState.NOT_STARTED) {
			throw new NotAllowedException(ErrorType.BadRequestError, "Spiel hat noch nicht begonnen");
		} else if (!players.current().getClient().equals(client)) {
			throw new NotAllowedException(ErrorType.BadRequestError, "Spieler ist nicht dran");
		} else if (tiles.isEmpty()) {
			checkWrongMove();
			throw new NotAllowedException("Spieler muss mehr als 0 Seine spielen");
		} else if (!players.current().hasTiles(tiles)) {
			checkWrongMove();
			throw new NotAllowedException("Spieler hat nicht alle Steine in seiner Hand");
		} else if (tiles.size() > config.getMaxHandTiles()) {
			checkWrongMove();
			throw new NotAllowedException(
					"Spieler kann nicht mehr Steine spielen, als in der Konfiguration vorgesehen");
		} else if (!mapLogic.validateMove(tilesWithCoordinate)) {
			checkWrongMove();
			throw new NotAllowedException("Spielzug nicht valide");
		}
		// modify score
		players.current().modifyScore(mapLogic.doMove(tilesWithCoordinate));

		// remove tiles of a player
		players.current().removeTiles(tiles);

		// player did move, spectators need visualization time
		gameClock.turnTimeOver();
	}

	@Override
	public Map<Client, Integer> requestScore() {
		/*
		 * Does this need the ability to send a NotAllowed ? If a game is not started,
		 * all scores are 0, should be fine
		 */

		return new HashMap<Client, Integer>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				for (Player player : players) {
					put(player.getClient(), player.getScore());
				}
			}
		};
	}

	@Override
	public long requestTurnTimeLeft() {
		return gameClock.getTimeLeft();
	}

	@Override
	public long requestTotalTime() {
		return gameClock.getTotalTime();
	}

	@Override
	public List<Tile> requestBag() {
		return bag.getBag();
	}

	@Override
	public Map<Client, Collection<Tile>> requestPlayerHands() {
		return new HashMap<Client, Collection<Tile>>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				for (Player player : players) {
					put(player.getClient(), new ArrayList<Tile>(player.getTiles()));
				}
			}
		};
	}

	@Override
	public GameDataResponse requestGameData(Client client) {
		if (client.getClientType() == ClientType.PLAYER) {
			return GenerateResponse.constructGameDataResponse(mapLogic.getBoard(), players.current(),
					new ArrayList<Tile>(players.current().getTiles()), gameState);
		} else {
			return GenerateResponse.constructGameDataResponse(mapLogic.getBoard(), players.current(),
					Collections.emptyList(), gameState);
		}

	}

	@Override
	public List<Tile> requestTileSwap(Client client, List<Tile> toBeSwappedTiles)
			throws NotAllowedException, ParserException {

		if (!validateTiles(toBeSwappedTiles)) {

			if (players.current().getClient().equals(client)) {
				// player on turn will get swap valid false
				throw new ParserException(ErrorType.ValidationError, "Einer der Steine nicht bekannt");
			} else {
				throw new ParserException(ErrorType.BadRequestError, "Einer der Steine nicht bekannt");
			}

		} else if (gameState == GameState.NOT_STARTED) {

			throw new NotAllowedException(ErrorType.BadRequestError, "Spiel hat noch nicht begonnen");

		} else if (!players.current().getClient().equals(client)) {

			throw new NotAllowedException(ErrorType.BadRequestError, "Spieler nicht am Zug");

		} else if (toBeSwappedTiles.isEmpty()) {

			checkWrongMove();
			throw new NotAllowedException("Mehr als ein Stein muss getauscht werden");

		} else if (!players.current().hasTiles(toBeSwappedTiles)) {

			checkWrongMove();
			throw new NotAllowedException("Ein Spieler kann nur Steine tauschen, die er hat");

		} else if (toBeSwappedTiles.size() > config.getMaxHandTiles()) {

			checkWrongMove();
			throw new NotAllowedException("Spieler kann nicht mehr Steine tauschen als möglich");
		} else if (bag.getBagSize() < toBeSwappedTiles.size()) {

			checkWrongMove();
			throw new NotAllowedException("Spieler will mehr Steine tauschen, als im Stapel sind");
		}

		// Remove the tiles, place them into the stack and add the new tiles to the
		// player

		players.current().removeTiles(toBeSwappedTiles);
		ArrayList<Tile> newHandTiles = bag.swapTiles(toBeSwappedTiles);
		players.current().addTiles(newHandTiles);

		gameClock.turnTimeOver();

		return newHandTiles;
	}

	@Override
	public List<Tile> requestPlayerTiles(Client client) throws NotAllowedException {
		Player player = getPlayerByClient(client);
		if (player != null) {
			return new ArrayList<Tile>(player.getTiles());
		} else {
			throw new NotAllowedException("Spieler nicht im Spiel");
		}
	}

	@Override
	public List<Tuple<Coordinate, Tile>> requestUpdate() {
		return mapLogic.getLastMove();
	}

	/*
	 * CLOCK
	 */

	@Override
	public void visualisationTimeElapsed() {
		players.next();

		int tilesDifference = config.getMaxHandTiles() - players.current().getTilesOnHandCount();
		if (tilesDifference > 0) {

			List<Tile> newTiles = bag.takeTiles(tilesDifference);
			players.current().addTiles(newTiles);
			chc.callbackPlayerTiles(players.current(), newTiles);

		}

		if (bag.isEmpty()) {
			for (Player player : players) {
				if (player.hasNoTiles()) {
					// winning player gets 6 points
					player.modifyScore(6);
					// Game will be finished
					gameState = GameState.ENDED;
					// Stop Clock
					gameClock.destroy();
					// Send Winner to clients
					chc.callbackGameFinish(true);
					return;
				}
			}
		}
		chc.callbackNextPlayer(players.current());
	}

	@Override
	public void turnTimeElapsed() {
		if (config.getSlowMove() == SlowMove.KICK) {

			// Kick player and tell clients
			chc.callbackPlayerKicked(players.current());
			requestRemove(players.current().getClient());

		} else {
			players.current().modifyScore(config.getSlowMovePenalty() * -1);
		}

	}

	/**
	 * Check if tiles are valid tiles. That means, that the uniqueId, the color and
	 * the shape must lie in between its intervals
	 * 
	 * @param tiles
	 *            List of tiles
	 * @return true if all tiles are valid
	 */
	private boolean validateTiles(List<Tile> tiles) {
		int maxTileId = config.getColorShapeCount() * config.getColorShapeCount() * config.getTileCount();

		if (tiles == null) {
			return false;
		}

		for (Tile tile : tiles) {
			if (tile == null || tile.getUniqueId() < 0 || tile.getUniqueId() > maxTileId || tile.getColor() < 0
					|| tile.getColor() > config.getColorShapeCount() || tile.getShape() < 0
					|| tile.getShape() > config.getColorShapeCount()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks if tiles with their associated coordinate are valid. That means, that
	 * the uniqueId, the color and the shape of a tile must lie in between its
	 * intervals. Also the coordinate and tile can not be null.
	 * 
	 * 
	 * @param tiles
	 *            List of Tuples with Tile and Coordinate
	 * @return true if all tiles with coordinates are valid
	 */
	private boolean validateTilesOnPosition(List<Tuple<Coordinate, Tile>> tiles) {
		int maxTileId = config.getColorShapeCount() * config.getColorShapeCount() * config.getTileCount();

		if (tiles == null) {
			return false;
		}

		for (Tuple<Coordinate, Tile> tileWithCoord : tiles) {

			if (tileWithCoord == null) {
				return false;
			}

			Tile tile = tileWithCoord.getSecond();
			Coordinate coord = tileWithCoord.getFirst();

			if (coord == null || tile == null || tile.getUniqueId() < 0 || tile.getUniqueId() > maxTileId
					|| tile.getColor() < 0 || tile.getColor() > config.getColorShapeCount() || tile.getShape() < 0
					|| tile.getShape() > config.getColorShapeCount()) {
				return false;
			}

		}

		return true;
	}

	/*
	 * GETTER SETTER
	 */

	public Configuration getConfig() {
		return config;
	}

	public GameState getGameState() {
		return gameState;
	}

	public List<Player> getPlayers() {
		return players.getData();
	}

	public List<Spectator> getSpectators() {
		return spectators;
	}

	public boolean inTurn() {
		return gameClock.isInTurnTime();
	}

	public GameClock getGameClock() {
		return gameClock;
	}

}
