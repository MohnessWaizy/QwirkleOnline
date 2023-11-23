package org.GameLogic.Communication;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.DataStructures.Player;
import org.GameLogic.DataStructures.Tuple;
import org.GameLogic.Exceptions.NotAllowedException;
import org.GameLogic.Exceptions.ParserException;

import de.upb.swtpra1819interface.messages.GameDataResponse;
import de.upb.swtpra1819interface.messages.Winner;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.parser.ParsingException;
import de.upb.swtpra1819interface.models.Client;

/**
 * Interface to structure the requests for the <i>GameHandler</i>. Every
 * function is at least suitable for one request message code.
 */
public interface Communication {

	/*
	 * Join a game
	 */
	/**
	 * Request for a client that wants to join a game
	 * 
	 * @param client
	 *            Client
	 * @throws NotAllowedException
	 */
	public void requestJoin(Client client) throws NotAllowedException;

	/**
	 * Request for a client that wants to be removed from a game
	 * 
	 * @param client
	 *            Client
	 * @throws NotAllowedException
	 */
	public void requestRemove(Client client) throws NotAllowedException;

	/*
	 * For game information only
	 */

	/**
	 * Request to generate a Winner
	 * 
	 * @return Winner object
	 * @throws NotAllowedException
	 */
	public Winner requestWinner() throws NotAllowedException;

	/**
	 * Request the current player
	 * 
	 * @return Current player
	 */
	public Player requestCurrentPayer();

	/**
	 * Request score of all players
	 * 
	 * @return Mapping from player to score
	 */
	public Map<Client, Integer> requestScore();

	/**
	 * Request for turnTimeLeft
	 * 
	 * @return turnTimeLeft
	 */
	public long requestTurnTimeLeft();

	/**
	 * Request for totalTime
	 * 
	 * @return totalTime
	 */
	public long requestTotalTime();

	/**
	 * Request Bag
	 * 
	 * @return Bag
	 */
	public List<Tile> requestBag();

	/**
	 * Request every players hand
	 * 
	 * @return Map assigning every player their tiles
	 * 
	 */
	public Map<Client, Collection<Tile>> requestPlayerHands();

	/**
	 * Request for a tile swap
	 * 
	 * @param client
	 *            Player that wants to swap
	 * @param toBeSwappedTiles
	 *            Tiles
	 * @return New tiles from bag
	 * @throws NotAllowedException
	 * @throws ParsingException
	 */
	public List<Tile> requestTileSwap(Client client, List<Tile> toBeSwappedTiles)
			throws NotAllowedException, ParserException;

	/**
	 * Request for placing tiles on the board
	 * 
	 * @param client
	 *            Player that wants to place tiles
	 * @param tilesWithCoordinate
	 *            Tiles on certain coordinate
	 * @throws NotAllowedException
	 * @throws ParsingException
	 * @throws ParserException
	 */
	public void requestPlayTiles(Client client, List<Tuple<Coordinate, Tile>> tilesWithCoordinate)
			throws NotAllowedException, ParserException;

	/**
	 * Request for GameDataResponse
	 * 
	 * @param client
	 *            Client that requests
	 * @return GameDataResponse object
	 */
	public GameDataResponse requestGameData(Client client);

	/**
	 * Request an update of the board
	 * 
	 * @return Last done move a player did
	 */
	public List<Tuple<Coordinate, Tile>> requestUpdate();

	/**
	 * Request tiles a player has
	 * 
	 * @param client
	 *            Player
	 * @return Tiles of player
	 * @throws NotAllowedException
	 */
	public List<Tile> requestPlayerTiles(Client client) throws NotAllowedException;

}
