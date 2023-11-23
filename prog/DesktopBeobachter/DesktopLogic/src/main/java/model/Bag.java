package model;

import java.util.ArrayList;
import java.util.List;
import de.upb.swtpra1819interface.models.Tile;

/**
 * the bag of tiles
 *
 */
public class Bag {
	
	private List<Tile> tiles;
	private int numberOfTiles;
	
	/**
	 * constructor
	 */
	public Bag() {
		this.tiles = new ArrayList<Tile>();
		this.numberOfTiles = 0;
	}
	
	/**
	 * constructor
	 * 
	 * @param tiles 
	 * @param numberOfTiles
	 */
	public Bag(List<Tile> tiles, int numberOfTiles) {
		this.tiles = tiles;
		this.numberOfTiles = numberOfTiles;
	}

	/**
	 * @return tiles in bag
	 */
	public List<Tile> getTiles() {
		return tiles;
	}

	/**
	 * @param tiles
	 */
	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
		this.setNumberOfTiles(this.tiles.size());
	}

	/**
	 * @return number of tiles in bag
	 */
	public int getNumberOfTiles() {
		return numberOfTiles;
	}

	/**
	 * @param numberOfTiles
	 */
	public void setNumberOfTiles(int numberOfTiles) {
		this.numberOfTiles = numberOfTiles;
	}
}
