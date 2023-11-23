package org.GameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.Board.Map;
import org.GameLogic.Board.Tuple;
import org.junit.Test;

import de.upb.swtpra1819interface.models.Tile;

import junit.framework.TestCase;

public class MapTest extends TestCase {

	@Test
	public void testMap() {

		Tile trash = newTile(0, 0, 0);
		Tile t1 = newTile(1, 0, 0);
		Tile t2 = newTile(2, 0, 0);

		final Map map = new Map();

		assertEquals("size check 1", 0, map.getTilesPlayed());

		map.addTile(new Coordinate(0, 0), t1);
		map.addTile(new Tuple<Coordinate, Tile>(new Coordinate(1, 1), t2));

		assertTrue("Tiles wurden plaziert", map.isTileOn(Coordinate.of(0, 0)) && map.isTileOn(Coordinate.of(1, 1)));

		Coordinate c1 = new Coordinate(0, 0);
		Coordinate c2 = new Coordinate(1, 1);

		assertFalse("Coordinate besetzt", map.addTile(Coordinate.of(0, 0), trash));
		assertTrue("Coordinate besetzt", map.isTileOn(Coordinate.of(0, 0)));
		assertEquals("size check 2", 2, map.getTilesPlayed());

		HashMap<Coordinate, Tile> comparision = new HashMap<Coordinate, Tile>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -6891595623290966744L;

			{
				put(c1, t1);
				put(c2, t2);
			}
		};

		HashMap<Coordinate, Tile> getBoard = map.getBoard();
		assertEquals("Reverse check", comparision, getBoard);

		assertEquals("getTile", map.getTile(Coordinate.of(1, 1)), t2);

		/*
		 * Test AddTiles for already placed tiles
		 */

		Tile t3 = newTile(99, 0, 0);
		Tile t4 = newTile(999, 0, 0);

		int size = map.getTilesPlayed();

		// already in the DS
		Tuple<Coordinate, Tile> toBeAdded1 = new Tuple<Coordinate, Tile>(Coordinate.of(0, 0), t3);
		Tuple<Coordinate, Tile> toBeAdded2 = new Tuple<Coordinate, Tile>(Coordinate.of(-1, 0), t4);

		List<Tuple<Coordinate, Tile>> listToBeAdded = new ArrayList<Tuple<Coordinate, Tile>>();
		listToBeAdded.add(toBeAdded2);
		listToBeAdded.add(toBeAdded1);

		// assertFalse("Can not add tiles to the game", map.addTiles(listToBeAdded));
		assertEquals("addTiles resets state of the map if not succsessfull", map.getTilesPlayed(), size);
		// tiles should not be removed because the tiles are not added
		map.removeTiles(listToBeAdded);
		assertEquals("no tiles can be removed", map.getTilesPlayed(), size);

		/*
		 * Test AddTiles for only new tiles
		 */
		toBeAdded1 = new Tuple<Coordinate, Tile>(Coordinate.of(5, 0), t3);
		toBeAdded2 = new Tuple<Coordinate, Tile>(Coordinate.of(0, 5), t4);

		listToBeAdded = new ArrayList<Tuple<Coordinate, Tile>>();
		listToBeAdded.add(toBeAdded2);
		listToBeAdded.add(toBeAdded1);

		assertTrue("Tiles are placed succsessfully", map.addTiles(listToBeAdded));

		int beforeSize = map.getTilesPlayed();

		listToBeAdded = new ArrayList<Tuple<Coordinate, Tile>>();
		listToBeAdded.add(new Tuple<Coordinate, Tile>(new Coordinate(10, 10), new Tile(0, 0, 15)));
		listToBeAdded.add(toBeAdded1);

		assertFalse("Tiles are not placed succsessfully", map.addTiles(listToBeAdded));
		assertTrue("Tile count did not change", beforeSize == map.getTilesPlayed());
		assertFalse("Add null tile", map.addTile(new Coordinate(0, 10), null));
		assertFalse("Remove null tile", map.removeTile(new Coordinate(0, 10), null));
	}

	public Tile newTile(int id, int color, int shape) {
		return new Tile(color, shape, id);
	}

}
