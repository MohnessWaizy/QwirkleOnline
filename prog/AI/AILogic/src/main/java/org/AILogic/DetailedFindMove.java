package org.AILogic;

import java.util.ArrayList;
import java.util.Iterator;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.Board.MapLogic;
import org.GameLogic.Board.Tuple;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table.Cell;

import de.upb.swtpra1819interface.models.Tile;

/**
 * This implementation of findMove finds the best move and checks out all the
 * possibilities. The runtime is not relevant.
 *
 */
public class DetailedFindMove implements FindMoveStrategy {

	/*
	 * Get from AiLogic
	 */
	private MapLogic map;
	private ArrayList<Tile>[] colorHand;
	private ArrayList<Tile>[] shapeHand;
	private ArrayList<Tile> hand;
	private int maxColorShape;

	/*
	 * Calculate
	 */
	private ArrayList<Tile> tilesInLine;

	/**
	 * Initialise the strategy
	 */
	public DetailedFindMove() {
		tilesInLine = new ArrayList<Tile>();
	}

	@Override
	public ArrayList<Tuple<Coordinate, Tile>> findMove(MapLogic map,
			HashBasedTable<Integer, Integer, Tile> horizontalHead, HashBasedTable<Integer, Integer, Tile> verticalHead,
			ArrayList<Tile>[] colorHand, ArrayList<Tile>[] shapeHand, ArrayList<Tile> hand, int maxColorShape) {

		this.map = map;
		this.colorHand = colorHand;
		this.shapeHand = shapeHand;
		this.hand = hand;
		this.maxColorShape = maxColorShape;

		ArrayList<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();
		ArrayList<Tuple<Coordinate, Tile>> helpMove = new ArrayList<Tuple<Coordinate, Tile>>();
		int maxScore = 0;
		int score = 0;

		/*
		 * find the longest line for the first move, if the map is empty
		 */
		if (map.getTilesPlayed() == 0) {
			int lineLength = 0;
			ArrayList<Tile> maxLine = new ArrayList<Tile>();
			ArrayList<Tile> colorHandWithNoDuplicates = new ArrayList<Tile>();
			ArrayList<Tile> shapeHandWithNoDuplicates = new ArrayList<Tile>();

			for (int i = 0; i < maxColorShape; i++) {
				colorHandWithNoDuplicates = new ArrayList<Tile>();
				shapeHandWithNoDuplicates = new ArrayList<Tile>();
				/*
				 * delete duplicates from hand
				 */
				for (Tile tile : colorHand[i]) {
					if (!isDuplicate(tile, colorHandWithNoDuplicates)) {
						colorHandWithNoDuplicates.add(tile);
					}
				}
				/*
				 * delete duplicates from hand
				 */
				for (Tile tile : shapeHand[i]) {
					if (!isDuplicate(tile, shapeHandWithNoDuplicates)) {
						shapeHandWithNoDuplicates.add(tile);
					}
				}

				if (colorHandWithNoDuplicates.size() > lineLength) {
					lineLength = colorHandWithNoDuplicates.size();
					maxLine = colorHandWithNoDuplicates;
				}
				if (shapeHandWithNoDuplicates.size() > lineLength) {
					lineLength = shapeHandWithNoDuplicates.size();
					maxLine = shapeHandWithNoDuplicates;
				}
			}

			/*
			 * add Coordinate to Tile
			 */
			for (int i = 0; i < lineLength; i++) {
				move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, i), maxLine.get(i)));
			}

			/*
			 * Check, if there is no move
			 */
			if (move == null || move.size() == 0) {
				return null;
			} else {
				return move;
			}
		}

		/*
		 * iterate through all horizontal possible positions where a tile can be placed.
		 */
		for (Cell<Integer, Integer, Tile> tile : horizontalHead.cellSet()) {
			Coordinate coord = Coordinate.of(tile.getRowKey(), tile.getColumnKey());

			/*
			 * move horizontal
			 */
			helpMove = moveHorizontalVertical(coord, tile.getValue(), 'r');
			if (helpMove != null) {
				score = map.getScore(helpMove);
				if (score > maxScore) {
					maxScore = score;
					move = (ArrayList<Tuple<Coordinate, Tile>>) helpMove.clone();
				}
			}

			/*
			 * move beside the line
			 */
			helpMove = moveBesideLine(coord, tile.getValue(), 'r');
			if (helpMove == null) {
				continue;
			}

			score = map.getScore(helpMove);
			if (score > maxScore) {
				maxScore = score;
				move = (ArrayList<Tuple<Coordinate, Tile>>) helpMove.clone();
			}

		}

		/*
		 * iterate through all vertical possible positions where a tile can be placed.
		 */
		for (Cell<Integer, Integer, Tile> tile : verticalHead.cellSet()) {
			Coordinate coord = Coordinate.of(tile.getRowKey(), tile.getColumnKey());

			/*
			 * move vertical
			 */
			helpMove = moveHorizontalVertical(coord, tile.getValue(), 'd');
			if (helpMove != null) {
				score = map.getScore(helpMove);
				if (score > maxScore) {
					maxScore = score;
					move = (ArrayList<Tuple<Coordinate, Tile>>) helpMove.clone();
				}
			}

			/*
			 * move beside the line
			 */
			helpMove = moveBesideLine(coord, tile.getValue(), 'd');
			if (helpMove == null) {
				continue;
			}

			score = map.getScore(helpMove);
			if (score > maxScore) {
				maxScore = score;
				move = (ArrayList<Tuple<Coordinate, Tile>>) helpMove.clone();
			}

		}

		/*
		 * Check, if there is no move
		 */
		if (move == null || move.size() == 0) {
			return null;
		} else {
			return move;
		}
	}

	/**
	 * Update Map, Hand and maxColorShape
	 * 
	 * @param map           Map with tiles
	 * @param colorHand     hand ordered by color
	 * @param shapeHand     hand ordered by shape
	 * @param hand          hand with tiles
	 * @param maxColorShape number of dirrerent colors and shapes
	 */
	public void updateData(MapLogic map, ArrayList<Tile>[] colorHand, ArrayList<Tile>[] shapeHand, ArrayList<Tile> hand,
			int maxColorShape) {
		this.map = map;
		this.colorHand = colorHand;
		this.shapeHand = shapeHand;
		this.hand = hand;
		this.maxColorShape = maxColorShape;
	}

	/**
	 * Finds the best horizontal/vertical move. If no move is found it return null.
	 * 
	 * @param coord The coordinate from which the algorithm is started.
	 * @param tile  The tile from which the algorithm is started.
	 * @param dir   'r' for horizontal move and 'd' for vertical move.
	 * @return Best horizontal/vertical move. null if there is no move
	 */
	private ArrayList<Tuple<Coordinate, Tile>> moveHorizontalVertical(Coordinate coord, Tile tile, char dir) {
		return moveHorizontalVertical(coord, tile, dir, null);
	}

	/**
	 * Finds the best horizontal/vertical move. If no move is found it return null.
	 * 
	 * @param coord The coordinate from which the algorithm is started.
	 * @param tile  The tile from which the algorithm is started.
	 * @param       'r' for horizontal move and 'd' for vertical move.
	 * @param tuple Tuple that will be added to the current move. Set null, of there
	 *              is no tile in this move already.
	 * @return Best horizontal/vertical move. null if there is no move
	 */
	private ArrayList<Tuple<Coordinate, Tile>> moveHorizontalVertical(Coordinate coord, Tile tile, char dir,
			Tuple<Coordinate, Tile> tuple) {
		char otherDir;
		if (dir == 'r') {
			otherDir = 'l';
		} else if (dir == 'd') {
			otherDir = 'u';
		} else {
			/*
			 * It's not supposed to be reached.
			 */
			System.err.println("wrong parameter in startMove - dir has to be 'r' or 'd'");
			return null;
		}

		Coordinate rightDownCoord = coord.next(dir); // first direction
		Coordinate leftUpCoord = coord.next(otherDir); // second direction
		boolean isTileOnRightDown = map.isTileOn(rightDownCoord);
		boolean isTileOnLeftUp = map.isTileOn(leftUpCoord);
		int color = tile.getColor();
		int shape = tile.getShape();
		ArrayList<Tile> currentHand;
		ArrayList<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();
		if (tuple != null) {
			move.add(tuple);
		}

		/*
		 * Check the directions in which the algorithm must go.
		 */
		if (!isTileOnRightDown && !isTileOnLeftUp) {
			/*
			 * move right/down with color
			 */
			currentHand = (ArrayList<Tile>) colorHand[color].clone();
			move = moveLine(currentHand, coord, color, true, dir, move);
			/*
			 * other direction, if tiles left in hand
			 */
			if (currentHand.size() > 0) {
				move = moveLine(currentHand, coord, color, true, otherDir, move);
			}

			/*
			 * move right/down with shape
			 */
			ArrayList<Tuple<Coordinate, Tile>> shapeMove = new ArrayList<Tuple<Coordinate, Tile>>();
			if (tuple != null) {
				shapeMove.add(tuple);
			}

			currentHand = (ArrayList<Tile>) shapeHand[shape].clone();
			shapeMove = moveLine(currentHand, coord, shape, false, dir, shapeMove);
			/*
			 * other direction, if tiles left in hand
			 */
			if (currentHand.size() > 0) {
				shapeMove = moveLine(currentHand, coord, shape, false, otherDir, shapeMove);
			}

			/*
			 * move left/up with color
			 */
			ArrayList<Tuple<Coordinate, Tile>> upColorMove = new ArrayList<Tuple<Coordinate, Tile>>();
			if (tuple != null) {
				upColorMove.add(tuple);
			}

			currentHand = (ArrayList<Tile>) colorHand[color].clone();
			upColorMove = moveLine(currentHand, coord, color, true, otherDir, upColorMove);

			/*
			 * move left/up with shape
			 */
			ArrayList<Tuple<Coordinate, Tile>> upShapeMove = new ArrayList<Tuple<Coordinate, Tile>>();
			if (tuple != null) {
				upShapeMove.add(tuple);
			}

			currentHand = (ArrayList<Tile>) shapeHand[shape].clone();
			upShapeMove = moveLine(currentHand, coord, shape, false, otherDir, upShapeMove);

			/*
			 * find the move with the highest score
			 */
			int maxScore = 0;
			int score = 0;
			maxScore = map.getScore(move);
			score = map.getScore(shapeMove);
			if (maxScore < score) {
				maxScore = score;
				move = shapeMove;
			}
			score = map.getScore(upColorMove);
			if (maxScore < score) {
				maxScore = score;
				move = upColorMove;
			}
			score = map.getScore(upShapeMove);
			if (maxScore < score) {
				maxScore = score;
				move = upShapeMove;
			}

		} else if (isTileOnRightDown && map.getTile(rightDownCoord).getColor() == color) {
			currentHand = (ArrayList<Tile>) colorHand[color].clone();
			move = moveLine(currentHand, coord, color, true, otherDir, move);
			/*
			 * other direction, if tiles left in hand
			 */
			if (currentHand.size() > 0) {
				move = moveLine(currentHand, coord, color, true, dir, move);
			}
		} else if (isTileOnRightDown && map.getTile(rightDownCoord).getShape() == shape) {
			currentHand = (ArrayList<Tile>) shapeHand[shape].clone();
			move = moveLine(currentHand, coord, shape, false, otherDir, move);
			/*
			 * other direction, if tiles left in hand
			 */
			if (currentHand.size() > 0) {
				move = moveLine(currentHand, coord, shape, false, dir, move);
			}
		} else if (isTileOnLeftUp && map.getTile(leftUpCoord).getColor() == color) {
			currentHand = (ArrayList<Tile>) colorHand[color].clone();
			move = moveLine(currentHand, coord, color, true, dir, move);
			/*
			 * other direction, if tiles left in hand
			 */
			if (currentHand.size() > 0) {
				move = moveLine(currentHand, coord, color, true, otherDir, move);
			}
		} else if (isTileOnLeftUp && map.getTile(leftUpCoord).getShape() == shape) {
			currentHand = (ArrayList<Tile>) shapeHand[shape].clone();
			move = moveLine(currentHand, coord, shape, false, dir, move);
			/*
			 * other direction, if tiles left in hand
			 */
			if (currentHand.size() > 0) {
				move = moveLine(currentHand, coord, shape, false, otherDir, move);
			}
		}
		/*
		 * return move
		 */
		if (move != null && move.size() != 0) {
			return move;
		} else {
			return null;
		}
	}

	/**
	 * Find the best move, adjacent to another row or null if there is no move
	 * 
	 * @param coord The coordinate from which the algorithm is started.
	 * @param tile  The tile from which the algorithm is started.
	 * @param dir   'r' for horizontal move and 'd' for vertical move.
	 * @return Best horizontal/vertical move beside a line. null if there is no
	 *         move.
	 */
	private ArrayList<Tuple<Coordinate, Tile>> moveBesideLine(Coordinate coord, Tile tile, char dir) {
		char otherDir;
		char othterOtherDir;
		if (dir == 'r') {
			otherDir = 'l';
			othterOtherDir = 'd';
		} else if (dir == 'd') {
			otherDir = 'u';
			othterOtherDir = 'r';
		} else {
			/*
			 * It's not supposed to be reached.
			 */
			System.err.println("wrong parameter in startMove - dir has to be 'r' or 'd'");
			return null;
		}

		Coordinate rightDownCoord = coord.next(dir); // first direction
		Coordinate leftUpCoord = coord.next(otherDir); // second direction
		boolean isTileOnRightDown = map.isTileOn(rightDownCoord);
		boolean isTileOnLeftUp = map.isTileOn(leftUpCoord);
		int color = tile.getColor();
		int shape = tile.getShape();
		ArrayList<Tile> currentHand;
		/*
		 * right/down move
		 */
		ArrayList<Tuple<Coordinate, Tile>> move = null;

		/*
		 * save current move
		 */
		ArrayList<Tuple<Coordinate, Tile>> currentMove;
		int moveLength = 0;
		Tuple<Coordinate, Tile> tuple;
		ArrayList<Tuple<Coordinate, Tile>> setTile = new ArrayList<Tuple<Coordinate, Tile>>();

		/*
		 * Check the directions in which the algorithm must go.
		 */
		if (!isTileOnRightDown && !isTileOnLeftUp) {
			/*
			 * left/up move
			 */
			ArrayList<Tuple<Coordinate, Tile>> leftUpMove = null;

			currentHand = (ArrayList<Tile>) hand.clone();

			/*
			 * Check all tiles on hand
			 */
			for (Tile currentTile : currentHand) {
				if (currentTile.getColor() == color || currentTile.getShape() == shape) {

					/*
					 * move right/down
					 */
					tuple = new Tuple<Coordinate, Tile>(rightDownCoord, currentTile);
					setTile.clear();
					setTile.add(tuple);
					if (map.validateMove(setTile)) {
						currentMove = moveHorizontalVertical(rightDownCoord, currentTile, othterOtherDir, tuple);
						if (currentMove != null && currentMove.size() > moveLength) {
							moveLength = currentMove.size();
							move = (ArrayList<Tuple<Coordinate, Tile>>) currentMove.clone();
						}
					}

					/*
					 * move left/up
					 */
					tuple = new Tuple<Coordinate, Tile>(leftUpCoord, currentTile);
					setTile.clear();
					setTile.add(tuple);
					if (map.validateMove(setTile)) {
						moveLength = 0;

						currentMove = moveHorizontalVertical(leftUpCoord, currentTile, othterOtherDir, tuple);
						if (currentMove != null && currentMove.size() > moveLength) {
							moveLength = currentMove.size();
							leftUpMove = (ArrayList<Tuple<Coordinate, Tile>>) currentMove.clone();
						}
					}
				}
			}

			/*
			 * find move with higher score
			 */
			int score = map.getScore(move);
			if (score < map.getScore(leftUpMove)) {
				move = leftUpMove;
			}
		} else if (isTileOnRightDown && map.getTile(rightDownCoord).getColor() == color) {
			currentHand = (ArrayList<Tile>) colorHand[color].clone();

			/*
			 * Check all tiles on hand
			 */
			for (Tile currentTile : currentHand) {
				tuple = new Tuple<Coordinate, Tile>(leftUpCoord, currentTile);
				setTile.clear();
				setTile.add(tuple);
				if (map.validateMove(setTile)) {
					currentMove = moveHorizontalVertical(leftUpCoord, currentTile, othterOtherDir, tuple);
					if (currentMove != null && currentMove.size() > moveLength) {
						moveLength = currentMove.size();
						move = (ArrayList<Tuple<Coordinate, Tile>>) currentMove.clone();
					}
				}
			}
		} else if (isTileOnRightDown && map.getTile(rightDownCoord).getShape() == shape) {
			currentHand = (ArrayList<Tile>) shapeHand[shape].clone();

			/*
			 * Check all tiles on hand
			 */
			for (Tile currentTile : currentHand) {
				tuple = new Tuple<Coordinate, Tile>(leftUpCoord, currentTile);
				setTile.clear();
				setTile.add(tuple);
				if (map.validateMove(setTile)) {
					currentMove = moveHorizontalVertical(leftUpCoord, currentTile, othterOtherDir, tuple);
					if (currentMove != null && currentMove.size() > moveLength) {
						moveLength = currentMove.size();
						move = (ArrayList<Tuple<Coordinate, Tile>>) currentMove.clone();
					}
				}
			}
		} else if (isTileOnLeftUp && map.getTile(leftUpCoord).getColor() == color) {
			currentHand = (ArrayList<Tile>) colorHand[color].clone();

			/*
			 * Check all tiles on hand
			 */
			for (Tile currentTile : currentHand) {
				tuple = new Tuple<Coordinate, Tile>(rightDownCoord, currentTile);
				setTile.clear();
				setTile.add(tuple);
				if (map.validateMove(setTile)) {
					currentMove = moveHorizontalVertical(rightDownCoord, currentTile, othterOtherDir, tuple);
					if (currentMove != null && currentMove.size() > moveLength) {
						moveLength = currentMove.size();
						move = (ArrayList<Tuple<Coordinate, Tile>>) currentMove.clone();
					}
				}
			}
		} else if (isTileOnLeftUp && map.getTile(leftUpCoord).getShape() == shape) {
			currentHand = (ArrayList<Tile>) shapeHand[shape].clone();

			/*
			 * Check all tiles on hand
			 */
			for (Tile currentTile : currentHand) {
				tuple = new Tuple<Coordinate, Tile>(rightDownCoord, currentTile);
				setTile.clear();
				setTile.add(tuple);
				if (map.validateMove(setTile)) {
					currentMove = moveHorizontalVertical(rightDownCoord, currentTile, othterOtherDir, tuple);
					if (currentMove != null && currentMove.size() > moveLength) {
						moveLength = currentMove.size();
						move = (ArrayList<Tuple<Coordinate, Tile>>) currentMove.clone();
					}
				}
			}
		}
		/*
		 * return move
		 */
		if (move != null && move.size() != 0) {
			return move;
		} else {
			return null;
		}

	}

	/**
	 * Remove tile from hand
	 * 
	 * @param tile tile that should be removed
	 */
	private void removeTileFromHand(Tile tile) {
		colorHand[tile.getColor()].remove(tile);
		shapeHand[tile.getShape()].remove(tile);
		hand.remove(tile);
	}

	/**
	 * Add tile from hand
	 * 
	 * @param tile tile that should be added
	 */
	private void addTileToHand(Tile tile) {
		colorHand[tile.getColor()].add(tile);
		shapeHand[tile.getShape()].add(tile);
		hand.add(tile);
	}

	/**
	 * Finds the best move in the line.
	 * 
	 * @param currentHand The current hand with the corresponding tiles.
	 * @param coord       The coordinate from which the algorithm is started.
	 * @param id          The id of the color or shape
	 * @param isColorLine Whether the line is a color line
	 * @param dir         Direction in which the algorithm should run. Left = 'l',
	 *                    Right = 'r', Up = 'u', Down = 'd'.
	 * @return ArrayList with the longest move. Empty ArrayList, if there is no
	 *         move.
	 */
	private ArrayList<Tuple<Coordinate, Tile>> moveLine(ArrayList<Tile> currentHand, Coordinate coord, int id,
			boolean isColorLine, char dir) {
		return moveLine(currentHand, coord, id, isColorLine, dir, null);
	}

	/**
	 * Finds the best move in the line.
	 * 
	 * @param currentHand The current hand with the corresponding tiles.
	 * @param coord       The coordinate from which the algorithm is started.
	 * @param id          The id of the color or shape
	 * @param isColorLine Whether the line is a color line
	 * @param dir         Direction in which the algorithm should run. Left = 'l',
	 *                    Right = 'r', Up = 'u', Down = 'd'.
	 * @param move        If there is a move already. If not set to null.
	 * @return ArrayList with the longest move. Empty ArrayList, if there is no
	 *         move.
	 */
	private ArrayList<Tuple<Coordinate, Tile>> moveLine(ArrayList<Tile> currentHand, Coordinate coord, int id,
			boolean isColorLine, char dir, ArrayList<Tuple<Coordinate, Tile>> move) {
		/*
		 * Number of tiles with correct color
		 */
		int maxMoveLength = currentHand.size();
		ArrayList<Tuple<Coordinate, Tile>> currentMove = new ArrayList<Tuple<Coordinate, Tile>>();
		/*
		 * add move, if it is not null
		 */
		if (move != null && move.size() != 0) {
			currentMove.addAll(move);
		}

		/*
		 * no tiles with right color
		 */
		if (maxMoveLength == 0) {
			return currentMove;
		}

		/*
		 * Get free coord to the right
		 */
		Coordinate[] coords = new Coordinate[maxMoveLength];

		maxMoveLength = findFreeCoords(currentHand, coords, coord, id, isColorLine, dir);

		if (maxMoveLength == 0) {
			return currentMove;
		}

		/*
		 * find longest move to the right
		 */
		int moveLength = 0;
		Tuple<Coordinate, Tile> coordTile;

		for (int pos = 0; pos < maxMoveLength; pos++) {
			for (Tile tile : currentHand) {
				coordTile = new Tuple<Coordinate, Tile>(coords[pos], tile);
				currentMove.add(coordTile);
				if (map.validateMove(currentMove)) {
					currentHand.remove(tile);
					moveLength++;
					break;
				} else {
					currentMove.remove(coordTile);
				}
			}

			if (moveLength == maxMoveLength) {
				return currentMove;
			}
		}
		return currentMove;
	}

	/**
	 * Find all possible coordinates. This will check duplicates on a horizontal
	 * plane. The maximum length of the move is limited to the number of coordinates
	 * in this array.
	 * 
	 * @param currentHand The current hand with the corresponding tiles.
	 * @param coords      All free Coordinates.
	 * @param coord       The coordinate from which the algorithm is started.
	 * @param id          The id of the color or shape
	 * @param isColorLine Whether the line is a color line
	 * @param dir         Direction in which the algorithm should run. Left = 'l',
	 *                    Right = 'r', Up = 'u', Down = 'd'.
	 * @return number of free Coordinates in Array.
	 */
	private int findFreeCoords(ArrayList<Tile> currentHand, Coordinate[] coords, Coordinate coord, int id,
			boolean isColorLine, char dir) {
		Coordinate currentCoord = coord;
		/*
		 * move to the end of the line
		 */
		while (map.isTileOn(currentCoord.next(dir))) {
			currentCoord = currentCoord.next(dir);
		}

		/*
		 * save current line
		 */
		tilesInLine.clear();
		switch (dir) {
		case 'r':
			tilesInLine = getTilesInLine(currentCoord, 'l');
			break;
		case 'l':
			tilesInLine = getTilesInLine(currentCoord, 'r');
			break;
		case 'u':
			tilesInLine = getTilesInLine(currentCoord, 'd');
			break;
		case 'd':
			tilesInLine = getTilesInLine(currentCoord, 'u');
			break;
		default:
			break;
		}

		if (tilesInLine.size() >= maxColorShape || currentHand.size() == 0) {
			return 0;
		}

		/*
		 * Delete duplicates on hand
		 */
		Iterator<Tile> it = currentHand.iterator();
		while (it.hasNext()) {
			Tile tileOnHand = it.next();
			if (isDuplicate(tileOnHand, tilesInLine)) {
				it.remove();
			}
		}

		/*
		 * Number of tiles with correct color/shape
		 */
		int maxMoveLength = 0;

		// Coordinate currentCoord = coord;

		/*
		 * Find all Coordinates where a tile can be placed
		 */
		for (int i = 0; i < currentHand.size(); i++) {
			currentCoord = nextCoord(tilesInLine.size() + maxMoveLength, currentCoord, id, isColorLine, dir);
			if (currentCoord != null) {
				maxMoveLength++;
				coords[i] = currentCoord;
			} else {
				break;
			}
		}
		return maxMoveLength;
	}

	/**
	 * Get all tiles starting from coordinate.
	 * 
	 * @param coord The coordinate from which the algorithm is started.
	 * @param dir   Direction in which the algorithm should run. Left = 'l', Right =
	 *              'r', Up = 'u', Down = 'd'.
	 * @return ArrayList with all Tiles from coord to the first gap
	 */
	private ArrayList<Tile> getTilesInLine(Coordinate coord, char dir) {
		Coordinate currentCoord = coord;
		ArrayList<Tile> tileList = new ArrayList<Tile>();

		if (!map.isTileOn(coord)) {
			return tileList;
		}

		tileList.add(map.getTile(currentCoord));

		while (map.isTileOn(currentCoord.next(dir))) {
			currentCoord = currentCoord.next(dir);
			tileList.add(map.getTile(currentCoord));
		}
		return tileList;
	}

	/**
	 * Checks whether the element appears in the line.
	 * 
	 * @param tile      Tile which is tested.
	 * @param lineTiles Current line on the map.
	 * @return true, if tile is in line and false if not.
	 */
	private boolean isDuplicate(Tile tile, ArrayList<Tile> lineTiles) {
		for (Tile lineTile : lineTiles) {
			if (tile.getColor() == lineTile.getColor() && tile.getShape() == lineTile.getShape()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Find the next free coordinate. The maximum length of a line is checked and
	 * whether tiles occur twice when two lines are connected. It is also checked
	 * whether the next tile matches the color vertically. It is not checked whether
	 * tiles on the hand occur twice in adjacent horizontal lines.
	 * 
	 * @param lineLength
	 * @param currentCoord The coordinate from which the algorithm is started.
	 * @param id           The id of the color or shape
	 * @param isColorLine  Whether the line is a color line
	 * @param dir          Direction in which the algorithm should run. Left = 'l',
	 *                     Right = 'r', Up = 'u', Down = 'd'.
	 * @return Next free Coordinate, or null if there is no free Coordinate.
	 */
	private Coordinate nextCoord(int lineLength, Coordinate currentCoord, int id, boolean isColorLine, char dir) {
		Coordinate freeCoord;
		Tile currentTile;

		if (lineLength >= maxColorShape) {
			return null;
		}

		currentCoord = currentCoord.next(dir);
		lineLength++;

		/*
		 * Go right until you reach the first gap.
		 */
		while (map.isTileOn(currentCoord) && ((isColorLine && map.getTile(currentCoord).getColor() == id)
				|| (!isColorLine && map.getTile(currentCoord).getShape() == id))) {
			currentTile = map.getTile(currentCoord);
			if (lineLength >= maxColorShape || isDuplicate(currentTile, tilesInLine)) {
				return null;
			}
			tilesInLine.add(currentTile);
			currentCoord = currentCoord.next(dir);
			lineLength++;
		}

		if (lineLength > maxColorShape) {
			return null;
		}
		freeCoord = currentCoord;
		currentCoord = currentCoord.next(dir);
		lineLength++;

		/*
		 * Go right until you reach the next gap.
		 */
		while (map.isTileOn(currentCoord) && ((isColorLine && map.getTile(currentCoord).getColor() == id)
				|| (!isColorLine && map.getTile(currentCoord).getShape() == id))) {
			currentTile = map.getTile(currentCoord);
			if (lineLength > maxColorShape || isDuplicate(currentTile, tilesInLine)) {
				return null;
			}
			currentCoord = currentCoord.next(dir);
			lineLength++;
		}
		return freeCoord;
	}

}
