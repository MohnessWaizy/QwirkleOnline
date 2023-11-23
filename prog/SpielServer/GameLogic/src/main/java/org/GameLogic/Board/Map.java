package org.GameLogic.Board;

import java.util.HashMap;
import java.util.List;

import org.GameLogic.DataStructures.Tuple;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

import de.upb.swtpra1819interface.models.Tile;

/**
 * Data structure storing all tiles on given coordinates. Uses a HashBasedTable
 * provided by Guava.
 */
public class Map {

	/**
	 * To minimize the effect of the Hashtable updating its inner data structure, we
	 * just expect the tiles to stay in the range between -128 to 127 in x and y
	 * coordinates. This means that the Hashtable will update itself(resize) when
	 * one side(left,right,down,up) surpasses 128 tiles. We can save 2^16 tiles
	 * without resizing the map in the best case.
	 */
	public static final int EXPECTED = 256;
	/**
	 * Data structure for saving the tile with their coordinates
	 */
	private Table<Integer, Integer, Tile> board;

	/**
	 * Creates an empty Map
	 */
	public Map() {
		board = HashBasedTable.create(EXPECTED, EXPECTED);
	}

	/**
	 * Adds a tile to the board
	 * 
	 * @param coordTile
	 *            Tuple containing coordinate and tile
	 * @return True if successful
	 */
	public boolean addTile(Tuple<Coordinate, Tile> coordTile) {
		return addTile(coordTile.getFirst(), coordTile.getSecond());
	}

	/**
	 * Adds a tile to the board
	 * 
	 * @param coord
	 *            Coordinate
	 * @param tile
	 *            Tile
	 * @return True if successful
	 */
	public boolean addTile(Coordinate coord, Tile tile) {
		if (tile != null && !isTileOn(coord)) {
			board.put(coord.getX(), coord.getY(), tile);
			return true;
		}
		return false;
	}

	/**
	 * Adds a list of tiles. If one coordinate is blocked, then the process of
	 * adding these tiles will be revoked.
	 * 
	 * @param coordTile
	 *            Tuple containing coordinate and tile
	 * @return True if successful
	 */
	public boolean addTiles(List<Tuple<Coordinate, Tile>> coordTile) {

		boolean succsessfull = true;
		int length = coordTile.size();
		int i = 0;

		for (; i < length && succsessfull; i++) {
			succsessfull = addTile(coordTile.get(i));
		}

		if (!succsessfull) {
			/*
			 * Only remove the tiles prior to the tile that is already on the board. Without
			 * this, the method will also remove a tile that was placed before succsessfully
			 */
			removeTiles(coordTile.subList(0, i - 1));
			return false;
		}

		return true;
	}

	/**
	 * Remove a tile
	 * 
	 * @param coord
	 *            coordinate
	 * @param tile
	 *            Tile
	 * @return True if successfully removed
	 */
	public boolean removeTile(Coordinate coord, Tile tile) {
		Tile tileFromBoard = getTile(coord);
		if (tileFromBoard != null && tileFromBoard.equals(tile)) {
			board.remove(coord.getX(), coord.getY());
			return true;
		}
		return false;
	}

	/**
	 * Remove a tile
	 * 
	 * @param coordTile
	 *            Tuple containing coordinate and tile
	 * @return True if successfully removed
	 */
	public boolean removeTile(Tuple<Coordinate, Tile> coordTile) {
		return removeTile(coordTile.getFirst(), coordTile.getSecond());
	}

	/**
	 * 
	 * @param coordTile
	 *            Tuple containing coordinate and tile
	 * @return True if successfully removed
	 */
	public boolean removeTiles(List<Tuple<Coordinate, Tile>> coordTile) {
		boolean succsessfull = true;
		for (Tuple<Coordinate, Tile> remove : coordTile) {
			succsessfull = succsessfull && removeTile(remove);
		}
		return succsessfull;
	}

	/**
	 * Gets a tile at a desired coordinate.
	 * 
	 * @param coord
	 *            Coordinate
	 * @return Tile on coordinate and null if coordinate is not present in the map
	 */
	public Tile getTile(Coordinate coord) {
		return board.get(coord.getX(), coord.getY());
	}

	/**
	 * Determines if a tile is on a given coordinate
	 * 
	 * @param coord
	 *            Coordinate
	 * @return True if a tile is on a coordinate
	 */
	public boolean isTileOn(Coordinate coord) {
		return board.contains(coord.getX(), coord.getY());
	}

	/**
	 * 
	 * @return Amount of tiles played
	 */
	public int getTilesPlayed() {
		return board.size();
	}

	// TODO : HashMap ändern, sodass diese nicht immer neu erstellt werden muss,
	// sonder nur immer gelöscht/hinzugefügt werden muss

	/**
	 * This method does not return a reference to the actual data structure used
	 * here.
	 * 
	 * @return A hash map based board
	 */
	public HashMap<Coordinate, Tile> getBoard() {
		HashMap<Coordinate, Tile> map = new HashMap<Coordinate, Tile>();

		for (Cell<Integer, Integer, Tile> cell : board.cellSet()) {
			map.put(Coordinate.of(cell.getRowKey(), cell.getColumnKey()), cell.getValue());
		}

		return map;

	}

}
