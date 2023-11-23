package org.GameLogic.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.DataStructures.CycleList;
import org.GameLogic.DataStructures.Player;

import de.upb.swtpra1819interface.models.*;
import de.upb.swtpra1819interface.messages.GameDataResponse;
import de.upb.swtpra1819interface.messages.Winner;

/**
 * Class to structure GameHandler and remove overhead
 */
public final class GenerateResponse {
	/**
	 * Constructs a {@link GameDataResponse} by given parameters of the interface
	 * 
	 * @param board
	 * @param current
	 * @param ownTiles
	 * @param gameState
	 * @return GameDataResponse instance
	 */
	public final static GameDataResponse constructGameDataResponse(Map<Coordinate, Tile> board, Player current,
			List<Tile> ownTiles, GameState gameState) {

		List<TileOnPosition> convertedBoard = new ArrayList<TileOnPosition>();

		for (Entry<Coordinate, Tile> entry : board.entrySet()) {
			Coordinate coord = entry.getKey();
			convertedBoard.add(new TileOnPosition(coord.getX(), coord.getY(), entry.getValue()));
		}

		return new GameDataResponse(convertedBoard, current == null ? null : current.getClient(), ownTiles, gameState);

	}

	/**
	 * Constructs a {@link Winner} by given parameters of the interface
	 * 
	 * @param winner
	 * @param players
	 * @return Winner instance
	 */
	public final static Winner constructWinner(Player winner, CycleList<Player> players) {

		if (winner == null) {
			return new Winner(null, 0, Collections.emptyMap());
		}

		Map<Client, Integer> score = new HashMap<Client, Integer>();

		for (Player player : players) {
			score.put(player.getClient(), player.getScore());
		}

		return new Winner(winner.getClient(), winner.getScore(), score);
	}
}