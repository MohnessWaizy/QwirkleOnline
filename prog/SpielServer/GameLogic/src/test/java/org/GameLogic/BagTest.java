package org.GameLogic;

import java.util.ArrayList;
import java.util.List;

import org.GameLogic.Board.Bag;
import org.junit.Test;

import de.upb.swtpra1819interface.models.Tile;
import junit.framework.TestCase;

public class BagTest extends TestCase {

	@Test
	public void testBagCreation() {

		Bag bag = new Bag(6, 6, 6);

		assertEquals(bag.getBagSize(), 6 * 6 * 6);
		assertTrue("Take tiles", bag.canSwapTileAmount(6 * 6 * 6));
		assertFalse("Take tiles", bag.canSwapTileAmount(6 * 6 * 6 + 1));

	}

	@Test
	public void testBagTilesTaken() {
		Bag bag = new Bag(6, 6, 6);

		List<Tile> takenTiles = new ArrayList<Tile>();

		for (int i = 0; i < 4; i++) {
			takenTiles.addAll(bag.takeStartTiles());
		}
		// assert that takenTiles and the bag have no intersection
		assertFalse("No Intersection", bag.getBag().removeAll(takenTiles));
		assertTrue("Right amount taken", bag.getBagSize() == (6 * 6 * 6) - (6 * 4));

		List<Tile> takeTurnTiles = bag.takeTiles(16);

		// Swap test
		List<Tile> swapTiles = bag.swapTiles(takeTurnTiles);
		assertEquals(takeTurnTiles.size(), swapTiles.size());
		
		// Take all tiles
		List<Tile> rest = bag.takeTiles(6 * 6 * 6);
		assertTrue("Rest tiles are less than inital tiles", rest.size() < 6 * 6 * 6);
		assertTrue("Bag is empty", bag.takeTiles(1).size() == 0 && bag.isEmpty());
		
	}
}
