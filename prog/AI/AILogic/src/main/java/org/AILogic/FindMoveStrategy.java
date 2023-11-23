package org.AILogic;

import java.util.ArrayList;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.Board.MapLogic;
import org.GameLogic.Board.Tuple;

import com.google.common.collect.HashBasedTable;

import de.upb.swtpra1819interface.models.Tile;

/**
 * Interface for a strategy to calculate a move
 */
public interface FindMoveStrategy {
	/**
	 * Find a valid move with maximum score. If no move was found it returns null.
	 * 
	 * @param map        local map of the current game
	 * @param neighbours HashBasedTable of Arrays with booleans if tile have
	 *                   neighbour
	 * @param colorHand  Hand sorted by color
	 * @param shapeHand  Hand sorted by shape
	 * @return A ArrayList with a valid move, or null if no valid move is possible
	 */
	public ArrayList<Tuple<Coordinate, Tile>> findMove(MapLogic map,
			HashBasedTable<Integer, Integer, Tile> horizontalHead, HashBasedTable<Integer, Integer, Tile> verticalHead,
			ArrayList<Tile>[] colorHand, ArrayList<Tile>[] shapeHand, ArrayList<Tile> hand, int maxColorShape);
}
