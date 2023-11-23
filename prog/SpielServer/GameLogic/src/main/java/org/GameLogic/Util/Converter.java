package org.GameLogic.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.DataStructures.Participant;
import org.GameLogic.DataStructures.Tuple;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;

/**
 * Used for converting instances of tileOnPosition to our internal structures
 */
public final class Converter {
	/**
	 * Converts a list of {@link de.upb.swtpra1819interface.models.TileOnPosition
	 * TilesOnPosition instances} to a list of tuples of {@link Coordinate} and
	 * {@link Tile}
	 * 
	 * @param tileOnPosition TileOnPositoion
	 * @return Tuple of type coordinate and tile
	 */
	public static List<Tuple<Coordinate, Tile>> toGameTileCoord(List<TileOnPosition> tileOnPosition) {

		/*
		 * Safety due to possibility of invalid incoming objects
		 */

		if (tileOnPosition == null) {
			return null;
		}

		List<Tuple<Coordinate, Tile>> converted = new ArrayList<Tuple<Coordinate, Tile>>();

		for (TileOnPosition top : tileOnPosition) {
			if (top == null) {
				converted.add(null);
			} else {
				converted.add(
						new Tuple<Coordinate, Tile>(new Coordinate(top.getCoordX(), top.getCoordY()), top.getTile()));
			}
		}
		return converted;

	}

	/**
	 * Converts a list of tuples of {@link Coordinate} and {@link Tile} to a list of
	 * {@link de.upb.swtpra1819interface.models.TileOnPosition TilesOnPosition
	 * instances}
	 * 
	 * @param tupleWithCoordinateAndTile Tuple of type coordinate and tile
	 * @return TileOnPositon instance
	 */
	public static List<TileOnPosition> toNetworkTileCoord(List<Tuple<Coordinate, Tile>> tupleWithCoordinateAndTile) {

		return tupleWithCoordinateAndTile.stream()
				.map(tuple -> new TileOnPosition(tuple.getFirst().getX(), tuple.getFirst().getY(), tuple.getSecond()))
				.collect(Collectors.toList());

	}

	/**
	 * Converts a {@link Participant} to a
	 * {@link de.upb.swtpra1819interface.models.Client Client}.
	 * 
	 * @param participants List of participants
	 * @return List of clients
	 */

	public static List<Client> toNetworkClient(List<? extends Participant> participants) {
		return participants.stream().map(participant -> participant.getClient()).collect(Collectors.toList());
	}

}
