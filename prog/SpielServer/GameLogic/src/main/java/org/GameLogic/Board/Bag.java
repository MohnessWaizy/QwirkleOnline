package org.GameLogic.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.upb.swtpra1819interface.models.Tile;

/**
 * 
 * This class does manage the bag. From here, all tiles will be created. If the
 * game can request tiles from the bag, which will be given out.
 *
 */
public class Bag {

	/**
	 * The bag is backed by an array list
	 */
	private ArrayList<Tile> bag;
	/**
	 * Given by config
	 */
	private int tilesPerHand;
	/**
	 * Random instance for random inserting of tiles into the bag
	 */
	private Random random;

	/**
	 * <p>
	 * Constructs a bag with the size of <i>tileTypesCount * tileTypesCount *
	 * tileFrequency</i>.
	 * </p>
	 * 
	 * @param tileTypesCount
	 *            From config
	 * @param tileFrequency
	 *            From config
	 * @param tilesPerHand
	 *            From config
	 */
	public Bag(int tileTypesCount, int tileFrequency, int tilesPerHand) {
		this.tilesPerHand = tilesPerHand;
		this.bag = new ArrayList<Tile>(tileTypesCount * tileTypesCount * tileFrequency);
		this.random = new Random();

		int tileId = 0;

		// Create all tiles
		for (; tileFrequency > 0; tileFrequency--) {
			// Loop colors
			for (int i = 0; i < tileTypesCount; i++) {
				// Loop shapes
				for (int j = 0; j < tileTypesCount; j++) {
					bag.add(new Tile(i, j, tileId));
					tileId++;
				}
			}
		}

		// Randomize list generated
		Collections.shuffle(bag);
	}

	/**
	 * 
	 * @param takeTurnTiles
	 *            Tiles that are put back into the bag
	 * @return A list with tiles taken from the stack, having the same size as the
	 *         parameter exchangeTiles list
	 */
	public ArrayList<Tile> swapTiles(List<Tile> takeTurnTiles) {
		List<Tile> newHandTiles = bag.subList(0, takeTurnTiles.size());
		ArrayList<Tile> handTiles = new ArrayList<Tile>(newHandTiles);

		newHandTiles.clear();
		
		for (Tile tile : takeTurnTiles) {
			bag.add(random.nextInt(bag.size()), tile);
		}
		
		return handTiles;
	}

	/**
	 * Give out the starting Tiles.
	 * 
	 * @return A list with tiles
	 */
	public ArrayList<Tile> takeStartTiles() {
		return takeTiles(this.tilesPerHand);
	}

	/**
	 * Give out tiles according to the number requested, but not more then are
	 * available.
	 * 
	 * @param count
	 *            Requested number of tiles.
	 * @return A list with tiles
	 */
	public ArrayList<Tile> takeTiles(int count) {
		// can not take more tiles than are on the bag
		count = Math.min(count, bag.size());

		// subList is only a view !
		List<Tile> newHandTiles = bag.subList(0, count);
		ArrayList<Tile> handTiles = new ArrayList<Tile>(newHandTiles);

		// delete tiles that are giving out
		newHandTiles.clear();

		return handTiles;
	}

	/**
	 * Checks if a player can swap his desired amount of tiles.
	 * 
	 * @param tiles
	 *            Number of tiles a player wants to have
	 * @return True if the bag has more tiles than requested
	 */
	public boolean canSwapTileAmount(int tiles) {
		return bag.size() >= tiles;
	}

	/**
	 * Get size of bag.
	 * 
	 * @return The size of the bag
	 */

	public int getBagSize() {
		return bag.size();
	}

	/**
	 * Check if the bag is empty.
	 * 
	 * @return True if the bag is empty
	 */
	public boolean isEmpty() {
		return bag.isEmpty();
	}

	/**
	 * Get the bag.
	 * 
	 * @return Reference of the bag
	 */

	public List<Tile> getBag() {
		return bag;
	}

	/**
	 * Refills the bag with tiles
	 * 
	 * @param tiles
	 *            List of tiles
	 */

	public void refill(List<Tile> tiles) {
		bag.addAll(tiles);
	}

}
