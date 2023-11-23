package org.AILogic;

import java.util.ArrayList;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.Board.MapLogic;
import org.GameLogic.Board.Tuple;

import com.google.common.collect.HashBasedTable;

import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Tile;

/**
 * Calculate the best move for the AI. This class have a local copy of the map,
 * to calculate the move.
 */
public class AiLogic {
	/*
	 * local copy of the map
	 */
	private MapLogic map;

	/*
	 * config of the game
	 */
	private Configuration config;

	/*
	 * Outer tiles of a horizontal line.
	 */
	private HashBasedTable<Integer, Integer, Tile> horizontalHead;

	/*
	 * Outer tiles of a vertical line.
	 */
	private HashBasedTable<Integer, Integer, Tile> verticalHead;

	/*
	 * Expected number of row and cells per row
	 */
	public static final int EXPECTED = 256;

	/*
	 * Array with HashMaps to sort hand by color and shape. ColorId - 1 and ShapeId
	 * - 1 in array!
	 */
	private ArrayList<Tile>[] colorHand;
	private ArrayList<Tile>[] shapeHand;
	private ArrayList<Tile> hand;

	/*
	 * Algorithm to find a move
	 */
	private FindMoveStrategy findMove;

	/*
	 * penalty points for a wrong/slow move
	 */
	private boolean pointsForWrongMove;
	private boolean pointsForSlowMove;
	private int penaltyPoints;

	/*
	 * maximum number of different colors and shapes
	 */
	private int colorShapeCount;

	/**
	 * Initialise the logic to calculate the best move
	 * 
	 * @param config Configuration of the current game
	 */
	public AiLogic(Configuration config) {
		this.config = config;
		map = new MapLogic(config.getColorShapeCount());
		horizontalHead = HashBasedTable.create(EXPECTED, EXPECTED);
		verticalHead = HashBasedTable.create(EXPECTED, EXPECTED);

		colorShapeCount = config.getColorShapeCount();
		colorHand = new ArrayList[colorShapeCount];
		shapeHand = new ArrayList[colorShapeCount];
		hand = new ArrayList<Tile>();
		for (int i = 0; i < colorShapeCount; i++) {
			colorHand[i] = new ArrayList<Tile>();
			shapeHand[i] = new ArrayList<Tile>();
		}

		/*
		 * Set default Algorithm for findMove
		 */
		findMove = new DetailedFindMove();

		/*
		 * set penaltyPoints, if they are negative
		 */
		penaltyPoints = 0;
		pointsForWrongMove = false;
		pointsForSlowMove = false;

		if (config.getWrongMove().name() == "POINT_LOSS") {
			if (penaltyPoints > config.getWrongMovePenalty()) {
				penaltyPoints = config.getWrongMovePenalty();
				pointsForWrongMove = true;
			}
		}

		if (config.getSlowMove().name() == "POINT_LOSS") {
			if (penaltyPoints > config.getSlowMovePenalty()) {
				penaltyPoints = config.getSlowMovePenalty();
				pointsForWrongMove = false;
				pointsForSlowMove = true;
			}
		}
	}

	public void setStrategy(FindMoveStrategy strategy) {
		findMove = strategy;

	}

	/**
	 * Add new tiles to the hand of the AI. The hand is sorted once according to
	 * colour and once according to shape.
	 * 
	 * @param tiles An ArrayList of all new tiles
	 */
	public void addTilesToHand(ArrayList<Tile> tiles) {
		for (Tile tile : tiles) {
			colorHand[tile.getColor()].add(tile);
			shapeHand[tile.getShape()].add(tile);
			hand.add(tile);
		}
	}

	/**
	 * Remove tiles from hand of the AI.
	 * 
	 * @param tiles An ArrayList of all new tiles
	 */
	public void removeTilesFromHand(ArrayList<Tile> tiles) {
		for (Tile tile : tiles) {
			for (Tile handTile : colorHand[tile.getColor()]) {
				if (tile.getUniqueId() == handTile.getUniqueId()) {
					colorHand[tile.getColor()].remove(handTile);
					break;
				}
			}
			for (Tile handTile : shapeHand[tile.getShape()]) {
				if (tile.getUniqueId() == handTile.getUniqueId()) {
					shapeHand[tile.getShape()].remove(handTile);
					break;
				}
			}
			for (Tile handTile : hand) {
				if (tile.getUniqueId() == handTile.getUniqueId()) {
					hand.remove(handTile);
					break;
				}
			}
		}
	}

	/**
	 * Remove all tiles from Hand of the AI
	 */
	public void removeAllTilesFormHand() {
		for (int i = 0; i < colorShapeCount; i++) {
			colorHand[i].clear();
			shapeHand[i].clear();
		}
		hand.clear();
	}

	public ArrayList<Tile>[] getColorHand() {
		return colorHand;
	}

	public ArrayList<Tile>[] getShapeHand() {
		return shapeHand;
	}

	public ArrayList<Tile> getHand() {
		return hand;
	}

	public int getScore(ArrayList<Tuple<Coordinate, Tile>> move) {
		return map.getScore(move);
	}

	public HashBasedTable<Integer, Integer, Tile> getHozirontalHead() {
		return horizontalHead;
	}

	public HashBasedTable<Integer, Integer, Tile> getVerticalHead() {
		return verticalHead;
	}

	/**
	 * Update the local map of the AI with new tiles
	 * 
	 * @param tilesOnPosition An ArrayList of all new tiles to put on the map
	 */
	public void updateMap(ArrayList<Tuple<Coordinate, Tile>> coordTiles) {
		/*
		 * Add tiles to local map
		 */
		map.doMove(coordTiles);

		/*
		 * Update tables
		 */
		updateLineHead(coordTiles);

	}

	/**
	 * Update the horizontal and vertical head with new tiles
	 * 
	 * @param coordTiles new tiles to add to the head
	 */
	private void updateLineHead(ArrayList<Tuple<Coordinate, Tile>> coordTiles) {
		Coordinate coord;
		Coordinate left;
		Coordinate right;
		Coordinate up;
		Coordinate down;
		boolean isLeft;
		boolean isRight;
		boolean isUp;
		boolean isDown;

		/*
		 * Add all tiles to the head
		 */
		for (Tuple<Coordinate, Tile> coordTile : coordTiles) {

			coord = coordTile.getFirst();
			left = coord.left();
			right = coord.right();
			up = coord.up();
			down = coord.down();
			isLeft = map.isTileOn(left);
			isRight = map.isTileOn(right);
			isUp = map.isTileOn(up);
			isDown = map.isTileOn(down);

			/*
			 * Update horizontal
			 */
			if (isLeft && isRight) {
				horizontalHead.remove(coord.getX(), coord.getY());
				if (map.isTileOn(left.left())) {
					horizontalHead.remove(left.getX(), left.getY());
				}
				if (map.isTileOn(right.right())) {
					horizontalHead.remove(right.getX(), right.getY());
				}
			} else if (isLeft) {
				horizontalHead.put(coord.getX(), coord.getY(), coordTile.getSecond());
				if (map.isTileOn(left.left())) {
					horizontalHead.remove(left.getX(), left.getY());
				}
			} else if (isRight) {
				horizontalHead.put(coord.getX(), coord.getY(), coordTile.getSecond());
				if (map.isTileOn(right.right())) {
					horizontalHead.remove(right.getX(), right.getY());
				}
			} else {
				horizontalHead.put(coord.getX(), coord.getY(), coordTile.getSecond());
			}

			/*
			 * Update vertical
			 */
			if (isUp && isDown) {
				verticalHead.remove(coord.getX(), coord.getY());
				if (map.isTileOn(up.up())) {
					verticalHead.remove(up.getX(), up.getY());
				}
				if (map.isTileOn(down.down())) {
					verticalHead.remove(down.getX(), down.getY());
				}
			} else if (isUp) {
				verticalHead.put(coord.getX(), coord.getY(), coordTile.getSecond());
				if (map.isTileOn(up.up())) {
					verticalHead.remove(up.getX(), up.getY());
				}
			} else if (isDown) {
				verticalHead.put(coord.getX(), coord.getY(), coordTile.getSecond());
				if (map.isTileOn(down.down())) {
					verticalHead.remove(down.getX(), down.getY());
				}
			} else {
				verticalHead.put(coord.getX(), coord.getY(), coordTile.getSecond());
			}

		}
	}

	/**
	 * Find the best valid move. If there is no move, return null.
	 * 
	 * @return An ArrayList with the move or null if no move is possible
	 */
	public ArrayList<Tuple<Coordinate, Tile>> findMove() {
		ArrayList<Tuple<Coordinate, Tile>> correctMove = findMove.findMove(map, horizontalHead, verticalHead, colorHand,
				shapeHand, hand, config.getColorShapeCount());
		/*
		 * if no move was found, return null, so that AI swap tiles.
		 */
		if (correctMove == null) {
			return null;
		}
		/*
		 * score of the current move
		 */
		int score = 0;
		score = map.getScore(correctMove);
		/*
		 * if AI get pionts with wrong move
		 */
		if (pointsForWrongMove && score < -penaltyPoints) {
			return wrongMove();
		}
		/*
		 * if AI get pionts with slow move
		 */
		if (pointsForSlowMove && score < -penaltyPoints) {
			return (new ArrayList<Tuple<Coordinate, Tile>>());
		}

		return correctMove;
	}

	/**
	 * Makes a wrong move
	 * 
	 * @return A wrong move, or null if there is no move
	 */
	private ArrayList<Tuple<Coordinate, Tile>> wrongMove() {
		ArrayList<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();

		/*
		 * No tiles on hand
		 */
		if (hand.isEmpty()) {
			return null;
		}

		/*
		 * Add tile to position (0,0)
		 */
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), hand.get(0)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 1), hand.get(0)));
		return move;
	}
}
