package org.GameLogic.DataStructures;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Tile;

/**
 * A class for a player that contains additionally the players score and tiles.
 */
public class Player extends Participant {

	/**
	 * Score of a player
	 */
	private int score;
	/**
	 * Ever tile is unique, so we can use a set here to fasten things up
	 */
	private Set<Tile> tiles;

	/**
	 * Construct a player with an association to a client instance
	 * 
	 * @param client
	 */
	public Player(Client client) {
		super(client);

		this.score = 0;
		this.tiles = new LinkedHashSet<Tile>(12);
	}

	/**
	 * Modify a score of a this player
	 * 
	 * @param by
	 *            Negative or positive integer
	 */
	public void modifyScore(int by) {
		score = score + by;
	}

	/**
	 * Add tiles to the hand of this player
	 * 
	 * @param toBeAddedTiles
	 * @return True if successful
	 */
	public boolean addTiles(List<Tile> toBeAddedTiles) {
		return tiles.addAll(toBeAddedTiles);
	}

	/**
	 * Remove tiles from the hand of this player
	 * 
	 * @param toBeRemovedTiles
	 * @return True if successful
	 */
	public boolean removeTiles(List<Tile> toBeRemovedTiles) {
		return tiles.removeAll(toBeRemovedTiles);
	}

	/**
	 * 
	 * @return True if player has no more tiles on his hand
	 */
	public boolean hasNoTiles() {
		return tiles.isEmpty();
	}

	/**
	 * Checks if the player has all the tiles he wants to put on the board
	 * 
	 * @param toBePlayedTiles
	 *            List of tiles to be checked
	 * @return True if a player has these tiles
	 */
	public boolean hasTiles(List<Tile> toBePlayedTiles) {
		// Every tile in toBePlayedTiles needs one match in tiles
		for (Tile tile : toBePlayedTiles) {
			if (!tiles.contains(tile)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @return Current count of tiles this player has
	 */
	public int getTilesOnHandCount() {
		return tiles.size();
	}

	/*
	 * Getters
	 */

	public int getScore() {
		return score;
	}

	public Set<Tile> getTiles() {
		return tiles;
	}

	/*
	 * Object stuff
	 */

	@Override
	public String toString() {
		return "Player [id=" + getId() + ", name=" + getName() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return other.client.equals(this.client);
	}

}
