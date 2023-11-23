package org.DesktopLogic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.upb.swtpra1819interface.models.Tile;
import model.Bag;

public class BagTest {
	private List<Tile> tiles;
	private int numberOfTiles;
	private Bag bag;

	@Test
	public void testBagLeerKonst() {
		ArrayList<Tile> tile = new ArrayList<Tile>();
		tile.add(new Tile(1, 3, 20));
		tile.add(new Tile(2, 3, 21));
		tile.add(new Tile(3, 3, 22));
		bag = new Bag();
		bag.setTiles(tile);
		assertTrue(bag.getNumberOfTiles() == tile.size());
		assertTrue(bag.getTiles().size() == tile.size());
		
	}
	
	@Test
	public void testBagKonst() {
		ArrayList<Tile> tile = new ArrayList<Tile>();
		tile.add(new Tile(1, 3, 20));
		tile.add(new Tile(2, 3, 21));
		tile.add(new Tile(3, 3, 22));
		bag = new Bag(tile, 5);
		bag.setTiles(tile);
		assertTrue(bag.getNumberOfTiles() == tile.size());
		assertTrue(bag.getTiles().size() == tile.size());
		
	}

}
