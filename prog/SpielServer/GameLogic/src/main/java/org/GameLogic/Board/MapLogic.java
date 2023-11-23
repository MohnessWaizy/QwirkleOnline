package org.GameLogic.Board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.GameLogic.DataStructures.Tuple;
import org.GameLogic.Handler.GameHandler;

import de.upb.swtpra1819interface.models.Tile;

/**
 * This class will take care of placing tiles on the board. It operates on the
 * data structure {@link Map}.
 */
public class MapLogic {

	/**
	 * Primary map
	 */
	private Map map;
	/**
	 * Given by config
	 */
	private int maxColorShape;
	/**
	 * The last available turn of the game
	 */
	private List<Tuple<Coordinate, Tile>> lastMove = null;

	public MapLogic(int colorShapeCount) {
		map = new Map();
		this.maxColorShape = colorShapeCount;
	}

	/**
	 * {@link Map#getBoard Delegates to map}
	 * 
	 * @return Board
	 */
	public HashMap<Coordinate, Tile> getBoard() {
		return map.getBoard();
	}

	/**
	 * Tells the {@linkplain GameHandler} if a move of a player is right and put
	 * tiles on map
	 * 
	 * @param tilesWithCoordinate List of tiles that are put on a certain coordinate
	 * @return true if the move is valid
	 */
	public boolean validateMove(List<Tuple<Coordinate, Tile>> tilesWithCoordinate) {

		if (isTileOnCoordinate(tilesWithCoordinate)) {
			return false;
		} else if (!tileOnZero(tilesWithCoordinate)) {
			return false;
		} else if (!sameColorShapeInMove(tilesWithCoordinate)) {
			return false;
		} else if (!isConnectedHorizontalVertical(tilesWithCoordinate)) {
			return false;
		} else if (gapOnMap(tilesWithCoordinate)) {
			return false;
		}

		/*
		 * add tiles to check if they are compatible to tiles on map
		 */
		map.addTiles(tilesWithCoordinate);

		if (!compatibleWithTilesOnMap(tilesWithCoordinate)) {
			/*
			 * Remove tiles if move is incorrect
			 */
			map.removeTiles(tilesWithCoordinate);
			return false;
		}

		/*
		 * if move is validate, save move in lastTurn
		 */
		lastMove = tilesWithCoordinate;
		return true;
	}

	/**
	 * 
	 * @return List of last validate move
	 */
	public List<Tuple<Coordinate, Tile>> getLastMove() {
		return lastMove;
	}

	/**
	 * Checks if the player has placed tiles on empty coordinates
	 * 
	 * @param moves List of tiles that are put on a certain coordinate
	 * @return true if tile is already on Coordinate
	 */
	private boolean isTileOnCoordinate(List<Tuple<Coordinate, Tile>> moves) {
		for (Tuple<Coordinate, Tile> tile : moves) {
			if (map.isTileOn(tile.getFirst())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param moves List of tiles that are put on a certain coordinate
	 * @return true if a tile is on coordinate (0,0)
	 */
	private boolean tileOnZero(List<Tuple<Coordinate, Tile>> moves) {
		if (map.getTilesPlayed() == 0) {
			boolean tileOnZero = false;

			for (Tuple<Coordinate, Tile> tile : moves) {
				if (tile.getFirst().getX() == 0 && tile.getFirst().getY() == 0) {
					tileOnZero = true;
				}
			}

			return tileOnZero;
		}
		return true;
	}

	/**
	 * Checks, if move is horizontal or vertical and if there are gaps in move.
	 * 
	 * @param moves List of tiles that are put on a certain coordinate
	 * @return false if no valid, else true
	 */
	private boolean isConnectedHorizontalVertical(List<Tuple<Coordinate, Tile>> moves) {
		if (isHorizontal(moves)) {
			map.addTiles(moves);

			Coordinate coord = moves.get(0).getFirst();
			ArrayList<Tile> move = new ArrayList<Tile>();
			for (Tuple<Coordinate, Tile> tile : moves) {
				move.add(tile.getSecond());
			}
			move.remove(moves.get(0).getSecond());
			while (map.isTileOn(coord.left())) {
				Tile tile = map.getTile(coord.left());
				if (move.contains(tile)) {
					move.remove(tile);
				}
				coord = coord.left();
			}

			coord = moves.get(0).getFirst();

			while (map.isTileOn(coord.right())) {
				Tile tile = map.getTile(coord.right());
				if (move.contains(tile)) {
					move.remove(tile);
				}
				coord = coord.right();
			}

			map.removeTiles(moves);

			return move.size() == 0;
		} else if (isVertical(moves)) {
			map.addTiles(moves);

			Coordinate coord = moves.get(0).getFirst();
			ArrayList<Tile> move = new ArrayList<Tile>();
			for (Tuple<Coordinate, Tile> tile : moves) {
				move.add(tile.getSecond());
			}
			move.remove(moves.get(0).getSecond());
			while (map.isTileOn(coord.up())) {
				Tile tile = map.getTile(coord.up());
				if (move.contains(tile)) {
					move.remove(tile);
				}
				coord = coord.up();
			}

			coord = moves.get(0).getFirst();

			while (map.isTileOn(coord.down())) {
				Tile tile = map.getTile(coord.down());
				if (move.contains(tile)) {
					move.remove(tile);
				}
				coord = coord.down();
			}

			map.removeTiles(moves);

			return move.size() == 0;
		} else {
			return false;
		}

	}

	/**
	 * Checks if the player has laid tiles in a horizontal line
	 * 
	 * @param moves List of tiles that are put on a certain coordinate
	 * @return true, if all tiles are in horizontal line and with same color or
	 *         shape
	 */
	private boolean isHorizontal(List<Tuple<Coordinate, Tile>> moves) {
		int horizontalCoord = moves.get(0).getFirst().getY();

		for (int i = 1; i < moves.size(); i++) {
			if (horizontalCoord != moves.get(i).getFirst().getY()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks if the player has laid tiles in a vertical line
	 * 
	 * @param moves List of tiles that are put on a certain coordinate
	 * @return true, if all tiles are in vertical line and with same color or shape
	 */
	private boolean isVertical(List<Tuple<Coordinate, Tile>> moves) {
		int verticalCoord = moves.get(0).getFirst().getX();

		for (int i = 1; i < moves.size(); i++) {
			if (verticalCoord != moves.get(i).getFirst().getX()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Check if all tiles in this move have the same color or shape
	 * 
	 * @param moves List of tiles that are put on a certain coordinate
	 * @return true if tiles in this move have same color or shape
	 */
	private boolean sameColorShapeInMove(List<Tuple<Coordinate, Tile>> moves) {
		Tile firstTile = moves.get(0).getSecond();
		int color = firstTile.getColor();
		int shape = firstTile.getShape();
		boolean sameColor = true;
		boolean sameShape = true;

		for (int i = 1; i < moves.size(); i++) {
			Tile currentTile = moves.get(i).getSecond();
			if (color != currentTile.getColor()) {
				sameColor = false;

			} else if (shape != currentTile.getShape()) {
				sameShape = false;
			}
		}
		return (sameColor || sameShape);
	}

	/**
	 * Check if all tiles are compatible with the tiles on the map
	 * 
	 * @param moves List of tiles that are put on a certain coordinate
	 * @return true if tiles are compatible with tiles on map
	 */
	private boolean compatibleWithTilesOnMap(List<Tuple<Coordinate, Tile>> moves) {
		for (Tuple<Coordinate, Tile> tile : moves) {
			int colorId = tile.getSecond().getColor();
			int shapeId = tile.getSecond().getShape();

			/*
			 * checks, if tile has left or right connected tiles
			 */
			if (map.isTileOn(tile.getFirst().left()) || map.isTileOn(tile.getFirst().right())) {
				if (!isHorizontalCompatible(tile.getFirst(), colorId, shapeId)) {
					return false;
				}
			}
			/*
			 * checks, if tile has up or down connected tiles
			 */
			if (map.isTileOn(tile.getFirst().up()) || map.isTileOn(tile.getFirst().down())) {
				if (!isVerticalCompatible(tile.getFirst(), colorId, shapeId)) {
					return false;
				}
			}

		}
		return true;
	}

	/**
	 * Check if tile is horizontal compatible
	 * 
	 * @param coord   Coordinate of the currently checked tile
	 * @param colorId ColorId of the currently checked tile
	 * @param shapeId ShapeId of the currently checked tile
	 * @return true if tile is horizontal compatible
	 */
	private boolean isHorizontalCompatible(Coordinate coord, int colorId, int shapeId) {
		Coordinate currentCoord = coord;
		Coordinate compareCoord;

		int currentColorId;
		int currentShapeId;

		/*
		 * checks, if horizontal line is a color line
		 */
		boolean isColorLine;

		/*
		 * current tile has connected tile on left and right side
		 */
		if (map.isTileOn(currentCoord.right()) && map.isTileOn(currentCoord.left())) {
			int compareColorId;
			int compareShapeId;

			isColorLine = (map.getTile(currentCoord).getColor() == map.getTile(currentCoord.left()).getColor());

			while (map.isTileOn(currentCoord)) {
				compareCoord = currentCoord.left();
				currentColorId = map.getTile(currentCoord).getColor();
				currentShapeId = map.getTile(currentCoord).getShape();

				while (map.isTileOn(compareCoord)) {
					compareColorId = map.getTile(compareCoord).getColor();
					compareShapeId = map.getTile(compareCoord).getShape();

					if ((isColorLine && (currentShapeId == compareShapeId || currentColorId != compareColorId))
							|| (!isColorLine
									&& (currentShapeId != compareShapeId || currentColorId == compareColorId))) {
						return false;
					}
					compareCoord = compareCoord.left();

				}
				currentCoord = currentCoord.right();
			}
		}
		/*
		 * current tile has connected tile only on left side
		 */
		else if (map.isTileOn(currentCoord.left())) {
			isColorLine = (map.getTile(currentCoord).getColor() == map.getTile(currentCoord.left()).getColor());

			currentCoord = currentCoord.left();

			while (map.isTileOn(currentCoord)) {
				currentColorId = map.getTile(currentCoord).getColor();
				currentShapeId = map.getTile(currentCoord).getShape();

				if ((isColorLine && (currentShapeId == shapeId || currentColorId != colorId))
						|| (!isColorLine && (currentShapeId != shapeId || currentColorId == colorId))) {
					return false;
				}
				currentCoord = currentCoord.left();
			}
		}
		/*
		 * current tile has connected tile only on left side
		 */
		else if (map.isTileOn(currentCoord.right())) {
			isColorLine = (map.getTile(currentCoord).getColor() == map.getTile(currentCoord.right()).getColor());

			currentCoord = currentCoord.right();

			while (map.isTileOn(currentCoord)) {
				currentColorId = map.getTile(currentCoord).getColor();
				currentShapeId = map.getTile(currentCoord).getShape();

				if ((isColorLine && (currentShapeId == shapeId || currentColorId != colorId))
						|| (!isColorLine && (currentShapeId != shapeId || currentColorId == colorId))) {
					return false;
				}
				currentCoord = currentCoord.right();
			}
		}
		return true;
	}

	/**
	 * Check if tile is vertical compatible
	 * 
	 * @param coord   Coordinate of the currently checked tile
	 * @param colorId ColorId of the currently checked tile
	 * @param shapeId ShapeId of the currently checked tile
	 * @return true if tile is vertical compatible
	 */
	private boolean isVerticalCompatible(Coordinate coord, int colorId, int shapeId) {
		Coordinate currentCoord = coord;
		Coordinate compareCoord;

		int currentColorId;
		int currentShapeId;

		/*
		 * checks, if vertical line is a color line
		 */
		boolean isColorLine;

		/*
		 * current tile has connected tile on down and up side
		 */
		if (map.isTileOn(currentCoord.up()) && map.isTileOn(currentCoord.down())) {
			int compareColorId;
			int compareShapeId;

			isColorLine = (map.getTile(currentCoord).getColor() == map.getTile(currentCoord.down()).getColor());

			while (map.isTileOn(currentCoord)) {
				compareCoord = currentCoord.down();
				currentColorId = map.getTile(currentCoord).getColor();
				currentShapeId = map.getTile(currentCoord).getShape();

				while (map.isTileOn(compareCoord)) {
					compareColorId = map.getTile(compareCoord).getColor();
					compareShapeId = map.getTile(compareCoord).getShape();

					if ((isColorLine && (currentShapeId == compareShapeId || currentColorId != compareColorId))
							|| (!isColorLine
									&& (currentShapeId != compareShapeId || currentColorId == compareColorId))) {
						return false;
					}
					compareCoord = compareCoord.down();

				}
				currentCoord = currentCoord.up();
			}
		}
		/*
		 * current tile has connected tile only on down side
		 */
		else if (map.isTileOn(currentCoord.down())) {
			isColorLine = (map.getTile(currentCoord).getColor() == map.getTile(currentCoord.down()).getColor());

			currentCoord = currentCoord.down();

			while (map.isTileOn(currentCoord)) {
				currentColorId = map.getTile(currentCoord).getColor();
				currentShapeId = map.getTile(currentCoord).getShape();

				if ((isColorLine && (currentShapeId == shapeId || currentColorId != colorId))
						|| (!isColorLine && (currentShapeId != shapeId || currentColorId == colorId))) {
					return false;
				}
				currentCoord = currentCoord.down();
			}
		}
		/*
		 * current tile has connected tile only on down side
		 */
		else if (map.isTileOn(currentCoord.up())) {
			isColorLine = (map.getTile(currentCoord).getColor() == map.getTile(currentCoord.up()).getColor());

			currentCoord = currentCoord.up();

			while (map.isTileOn(currentCoord)) {
				currentColorId = map.getTile(currentCoord).getColor();
				currentShapeId = map.getTile(currentCoord).getShape();

				if ((isColorLine && (currentShapeId == shapeId || currentColorId != colorId))
						|| (!isColorLine && (currentShapeId != shapeId || currentColorId == colorId))) {
					return false;
				}
				currentCoord = currentCoord.up();
			}
		}
		return true;
	}

	/**
	 * Verifies that all tiles on the map are connected
	 * 
	 * @param moves List of tiles that are put on a certain coordinate
	 * @return true if a gap is on the map
	 */
	private boolean gapOnMap(List<Tuple<Coordinate, Tile>> moves) {
		// first tile on map
		if (map.getTilesPlayed() == 0) {
			return false;
		}

		List<Tuple<Coordinate, Tile>> notConnectedTiles = new ArrayList<Tuple<Coordinate, Tile>>();
		for (Tuple<Coordinate, Tile> tile : moves) {
			notConnectedTiles.add(tile);
		}

		int i = 0;
		while (i < notConnectedTiles.size() && !notConnectedTiles.isEmpty()) {
			List<Tuple<Coordinate, Tile>> toRemove = new ArrayList<Tuple<Coordinate, Tile>>();
			for (Tuple<Coordinate, Tile> tile : notConnectedTiles) {
				if (map.isTileOn(tile.getFirst().down()) || map.isTileOn(tile.getFirst().left())
						|| map.isTileOn(tile.getFirst().right()) || map.isTileOn(tile.getFirst().up())) {
					map.addTile(tile);
					toRemove.add(tile);
				}
			}
			notConnectedTiles.removeAll(toRemove);
			i++;
		}

		// remove Tiles
		for (Tuple<Coordinate, Tile> tile : moves) {
			map.removeTile(tile);
		}

		return notConnectedTiles.size() != 0;
	}

	/**
	 * After a validated move, doMove can be called to calculate the points of the
	 * actual move
	 * 
	 * @param moves Tiles that are played on certain coordinates
	 * @return Points of actual score
	 */
	public int doMove(List<Tuple<Coordinate, Tile>> moves) {
		int layedTiles = moves.size();
		/*
		 * First move on map
		 */
		if (map.getTilesPlayed() == layedTiles) {
			if (layedTiles == maxColorShape) {
				return layedTiles * 2;
			}
			return layedTiles;
		}

		int score = 0;
		Coordinate leftCoord, rightCoord, upCoord, downCoord;
		/*
		 * the counters count the tiles in a line to check if a line is a qwirkle
		 */
		int horizontalCounter = 0;
		int verticalCounter = 0;

		if (moves.size() == 1) {
			/*
			 * tiles horizontal
			 */
			leftCoord = moves.get(0).getFirst();
			rightCoord = moves.get(0).getFirst().right();

			if (map.isTileOn(leftCoord.left()) || map.isTileOn(rightCoord)) {
				while (map.isTileOn(leftCoord)) {
					horizontalCounter++;
					score++;
					leftCoord = leftCoord.left();
				}
				while (map.isTileOn(rightCoord)) {
					horizontalCounter++;
					score++;
					rightCoord = rightCoord.right();
				}

				/*
				 * Player layed a qwirkle and gets double points
				 */
				if (horizontalCounter == maxColorShape) {
					score = score + maxColorShape;
				}
			}

			/*
			 * tiles vertical
			 */
			upCoord = moves.get(0).getFirst();
			downCoord = moves.get(0).getFirst().down();

			if (map.isTileOn(upCoord.up()) || map.isTileOn(downCoord)) {
				while (map.isTileOn(upCoord)) {
					verticalCounter++;
					score++;
					upCoord = upCoord.up();
				}
				while (map.isTileOn(downCoord)) {
					verticalCounter++;
					score++;
					downCoord = downCoord.down();
				}

				/*
				 * Player layed a qwirkle and gets double points
				 */
				if (verticalCounter == maxColorShape) {
					score = score + maxColorShape;
				}
			}
		}

		/*
		 * if move is horizontal
		 */
		else if (isHorizontal(moves)) {
			leftCoord = moves.get(0).getFirst();
			rightCoord = moves.get(0).getFirst().right();

			while (map.isTileOn(leftCoord)) {
				horizontalCounter++;
				score++;
				leftCoord = leftCoord.left();
			}
			while (map.isTileOn(rightCoord)) {
				horizontalCounter++;
				score++;
				rightCoord = rightCoord.right();
			}

			/*
			 * Player layed a qwirkle and gets double points
			 */
			if (horizontalCounter == maxColorShape) {
				score = score + maxColorShape;
			}

			/*
			 * Check vertical direction
			 */
			for (Tuple<Coordinate, Tile> tile : moves) {
				upCoord = tile.getFirst().up();
				downCoord = tile.getFirst().down();
				verticalCounter = 0;

				if (map.isTileOn(upCoord) || map.isTileOn(downCoord)) {
					verticalCounter++;
					score++;
				}

				while (map.isTileOn(upCoord)) {
					verticalCounter++;
					score++;
					upCoord = upCoord.up();
				}
				while (map.isTileOn(downCoord)) {
					verticalCounter++;
					score++;
					downCoord = downCoord.down();
				}

				/*
				 * Player layed a qwirkle and gets double points
				 */
				if (verticalCounter == maxColorShape) {
					score = score + maxColorShape;
				}
			}
		}
		/*
		 * if move is vertical
		 */
		else {
			upCoord = moves.get(0).getFirst();
			downCoord = moves.get(0).getFirst().down();

			while (map.isTileOn(upCoord)) {
				verticalCounter++;
				score++;
				upCoord = upCoord.up();
			}
			while (map.isTileOn(downCoord)) {
				verticalCounter++;
				score++;
				downCoord = downCoord.down();
			}

			/*
			 * Player layed a qwirkle and gets double points
			 */
			if (verticalCounter == maxColorShape) {
				score = score + maxColorShape;
			}

			/*
			 * Check horizontal direction
			 */
			for (Tuple<Coordinate, Tile> tile : moves) {
				leftCoord = tile.getFirst().left();
				rightCoord = tile.getFirst().right();
				horizontalCounter = 0;

				if (map.isTileOn(leftCoord) || map.isTileOn(rightCoord)) {
					horizontalCounter++;
					score++;
				}

				while (map.isTileOn(leftCoord)) {
					horizontalCounter++;
					score++;
					leftCoord = leftCoord.left();
				}
				while (map.isTileOn(rightCoord)) {
					horizontalCounter++;
					score++;
					rightCoord = rightCoord.right();
				}

				/*
				 * Player layed a qwirkle and gets double points
				 */
				if (horizontalCounter == maxColorShape) {
					score = score + maxColorShape;
				}
			}
		}

		return score;
	}

	/**
	 * Put tiles on map without validation. Only for test cases!
	 * 
	 * @param moves Tiles that are played on certain coordinates
	 */
	public void doMoveWithoutValidate(List<Tuple<Coordinate, Tile>> moves) {
		map.addTiles(moves);
	}

}
