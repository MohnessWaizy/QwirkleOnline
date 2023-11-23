package org.GameLogic.Util;

import java.util.ArrayList;
import java.util.List;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.Board.Tuple;

import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;


public final class Converter {

	/**
	 * Converts Tuple with Coordinate and Tile to TileOnPosition
	 * 
	 * @param coordTiles ArrayList with Tuple with Coordinate and Tile
	 * @return List with TileOnPosition
	 */
	public List<TileOnPosition> toNetworkTileOnPosition(ArrayList<Tuple<Coordinate, Tile>> coordTiles) {
		List<TileOnPosition> tilesOnPosition = new ArrayList<TileOnPosition>();
		for (Tuple<Coordinate, Tile> coordTile : coordTiles) {
			Coordinate coord = coordTile.getFirst();
			tilesOnPosition.add(new TileOnPosition(coord.getX(), coord.getY(), coordTile.getSecond()));
		}
		return tilesOnPosition;
	}

	/**
	 * Converts TileOnPosition to Tuple with Coordinate and Tile
	 * 
	 * @param tilesOnPosition List with TileOnPosition
	 * @return ArrayList with Tuple with Coordinate and Tile
	 */
	public ArrayList<Tuple<Coordinate, Tile>> toGameCoordTile(List<TileOnPosition> tilesOnPosition) {
		ArrayList<Tuple<Coordinate, Tile>> coordTiles = new ArrayList<Tuple<Coordinate, Tile>>();
		for (TileOnPosition top : tilesOnPosition) {
			coordTiles.add(new Tuple<Coordinate, Tile>(new Coordinate(top.getCoordX(), top.getCoordY()), top.getTile()));
		}
		return coordTiles;
	}
}
