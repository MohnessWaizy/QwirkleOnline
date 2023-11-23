package org.GameLogic;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.Board.MapLogic;
import org.GameLogic.Board.Tuple;
import org.junit.Test;

import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.SlowMove;
import de.upb.swtpra1819interface.models.WrongMove;

import static org.junit.Assert.assertEquals;

public class MapLogicTest {

	@Test
	public void validateMoveTest() {

		WrongMove wrongMove = WrongMove.NOTHING;
		SlowMove slowMove = SlowMove.POINT_LOSS;

		Configuration config = new Configuration(7, 2, 6, 2, 2, wrongMove, 0, slowMove, 0, 2);

		MapLogic map = new MapLogic(config.getColorShapeCount());

		// thrid map to create a Qwirkles
		MapLogic thirdMap = new MapLogic(config.getColorShapeCount());

		// fill third map
		ArrayList<Tuple<Coordinate, Tile>> fillMap = new ArrayList<Tuple<Coordinate, Tile>>();
		fillMap.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 1, 1)));
		fillMap.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 1), newTile(2, 1, 2)));
		fillMap.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 2), newTile(3, 1, 3)));
		fillMap.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 3), newTile(4, 1, 4)));
		fillMap.add(new Tuple<Coordinate, Tile>(new Coordinate(-1, 3), newTile(5, 2, 4)));

		thirdMap.doMove(fillMap);

		ArrayList<Tuple<Coordinate, Tile>> moveWithGap = new ArrayList<Tuple<Coordinate, Tile>>();
		moveWithGap.add(new Tuple<Coordinate, Tile>(new Coordinate(-1, 0), newTile(5, 2, 1)));
		moveWithGap.add(new Tuple<Coordinate, Tile>(new Coordinate(-1, 2), newTile(7, 2, 3)));

		// create moves
		ArrayList<Tuple<Coordinate, Tile>> wrongFirstMove = new ArrayList<Tuple<Coordinate, Tile>>();
		wrongFirstMove.add(new Tuple<Coordinate, Tile>(new Coordinate(-1, -1), newTile(0, 0, 1)));

		// single tile on empty map
		ArrayList<Tuple<Coordinate, Tile>> moveFirstTile = new ArrayList<Tuple<Coordinate, Tile>>();
		moveFirstTile.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 0, 1)));

		// tile with same color under tile one
		ArrayList<Tuple<Coordinate, Tile>> moveWithSameColorLine = new ArrayList<Tuple<Coordinate, Tile>>();
		moveWithSameColorLine.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 0), newTile(2, 1, 1)));

		// tile with different color and shape
		ArrayList<Tuple<Coordinate, Tile>> moveWithDifferentColorShape = new ArrayList<Tuple<Coordinate, Tile>>();
		moveWithDifferentColorShape.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -1), newTile(3, 5, 6)));

		// tile with same shape besides tile one
		ArrayList<Tuple<Coordinate, Tile>> moveWithSameShapeLine = new ArrayList<Tuple<Coordinate, Tile>>();
		moveWithSameShapeLine.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 1), newTile(4, 0, 2)));

		// tile with different shape as line but same color tile as tile besides
		ArrayList<Tuple<Coordinate, Tile>> moveWithDifferntColorLine = new ArrayList<Tuple<Coordinate, Tile>>();
		moveWithDifferntColorLine.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 0), newTile(5, 1, 2)));

		// tile with different color as line but same shape tile as tile besides
		ArrayList<Tuple<Coordinate, Tile>> moveWithDifferntShapeLine = new ArrayList<Tuple<Coordinate, Tile>>();
		moveWithDifferntShapeLine.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 2), newTile(6, 1, 2)));

		// tiles in horizontal line
		ArrayList<Tuple<Coordinate, Tile>> moveInHorizontalLine = new ArrayList<Tuple<Coordinate, Tile>>();
		moveInHorizontalLine.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 0), newTile(7, 4, 1)));
		moveInHorizontalLine.add(new Tuple<Coordinate, Tile>(new Coordinate(3, 0), newTile(8, 3, 1)));

		// same tile in line
		ArrayList<Tuple<Coordinate, Tile>> sameTileInLine = new ArrayList<Tuple<Coordinate, Tile>>();
		sameTileInLine.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 2), newTile(9, 0, 4)));
		sameTileInLine.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 3), newTile(10, 0, 5)));
		sameTileInLine.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 4), newTile(11, 0, 2)));

		// tiles in vertrical line
		ArrayList<Tuple<Coordinate, Tile>> moveInVerticalLine = new ArrayList<Tuple<Coordinate, Tile>>();
		moveInVerticalLine.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 2), newTile(12, 0, 4)));
		moveInVerticalLine.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 3), newTile(13, 0, 5)));
		moveInVerticalLine.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 4), newTile(14, 0, 3)));
		moveInVerticalLine.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 5), newTile(15, 0, 7)));

		// tiles not in one line
		ArrayList<Tuple<Coordinate, Tile>> moveNotInALine = new ArrayList<Tuple<Coordinate, Tile>>();
		moveNotInALine.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 4), newTile(16, 0, 4)));
		moveNotInALine.add(new Tuple<Coordinate, Tile>(new Coordinate(4, 0), newTile(17, 3, 1)));

		// color line down to create scenario for next test
		ArrayList<Tuple<Coordinate, Tile>> helpMove = new ArrayList<Tuple<Coordinate, Tile>>();
		helpMove.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 1), newTile(18, 4, 3)));
		helpMove.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 2), newTile(19, 4, 4)));

		// tile between two lines
		ArrayList<Tuple<Coordinate, Tile>> moveBetweenLines = new ArrayList<Tuple<Coordinate, Tile>>();
		moveBetweenLines.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 2), newTile(20, 2, 4)));
		moveBetweenLines.add(new Tuple<Coordinate, Tile>(new Coordinate(3, 2), newTile(30, 3, 4)));

		// tile with no neighboring tile
		ArrayList<Tuple<Coordinate, Tile>> moveWithNoNeighbour = new ArrayList<Tuple<Coordinate, Tile>>();
		moveWithNoNeighbour.add(new Tuple<Coordinate, Tile>(new Coordinate(3, 3), newTile(21, 4, 1)));

		// line with tiles not connected to other tiles
		ArrayList<Tuple<Coordinate, Tile>> lineWithNoNeighbour = new ArrayList<Tuple<Coordinate, Tile>>();
		lineWithNoNeighbour.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 4), newTile(22, 4, 2)));
		lineWithNoNeighbour.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 5), newTile(23, 4, 3)));

		// move in different lines
		ArrayList<Tuple<Coordinate, Tile>> moveInDifferentLines = new ArrayList<Tuple<Coordinate, Tile>>();
		moveInDifferentLines.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 4), newTile(24, 0, 2)));
		moveInDifferentLines.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 5), newTile(25, 0, 3)));
		moveInDifferentLines.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 5), newTile(26, 0, 3)));

		// horizontal line, to create scenario for next test
		ArrayList<Tuple<Coordinate, Tile>> helpMove2 = new ArrayList<Tuple<Coordinate, Tile>>();
		helpMove2.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 5), newTile(27, 1, 7)));
		helpMove2.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 5), newTile(28, 4, 7)));

		// move connects two lines with same tile in it
		ArrayList<Tuple<Coordinate, Tile>> moveBetweenLineWithSameTile = new ArrayList<Tuple<Coordinate, Tile>>();
		moveBetweenLineWithSameTile.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 3), newTile(27, 4, 5)));
		moveBetweenLineWithSameTile.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 4), newTile(28, 4, 7)));

		// assert statements
		// tile on empty field
		assertEquals("Tile is not on coordinate (0,0)", false, map.validateMove(wrongFirstMove));

		// tile on empty field
		assertEquals("Tile could not placed on empty board", true, map.validateMove(moveFirstTile));
		map.doMove(moveFirstTile);

		// tile on not empty field
		assertEquals("Tile is placed on a not empty field", false, map.validateMove(moveFirstTile));

		// tile on line with the same color
		assertEquals("Tile with same color is not validate", true, map.validateMove(moveWithSameColorLine));
		map.doMove(moveWithSameColorLine);

		// tile on line with same shape
		assertEquals("Tile with same shape is not validate", true, map.validateMove(moveWithSameShapeLine));
		map.doMove(moveWithSameShapeLine);

		// tile with different color and shape
		assertEquals("Tile with different color and shape is validate", false,
				map.validateMove(moveWithDifferentColorShape));

		// tile on line with different color
		assertEquals("Tile with wrong color is validate", false, map.validateMove(moveWithDifferntColorLine));

		// tile on line with different shape
		assertEquals("Tile with wrong shape is validate", false, map.validateMove(moveWithDifferntShapeLine));

		// all tiles in a horizontal line
		assertEquals("Tiles in horizontal line are not validate", true, map.validateMove(moveInHorizontalLine));
		map.doMove(moveInHorizontalLine);

		// same tile in line
		assertEquals("Same tile in line is validate", false, map.validateMove(sameTileInLine));

		// all tiles in a vertical line
		assertEquals("Tiles in vertical line are not validate", true, map.validateMove(moveInVerticalLine));
		map.doMove(moveInVerticalLine);

		// tiles not in a line
		assertEquals("Tiles not in a line are validate", false, map.validateMove(moveNotInALine));

		// help move to create szenario for next test
		assertEquals("help line is not validate", true, map.validateMove(helpMove));
		map.doMove(helpMove);

		// tile between two correct lines
		assertEquals("tiles between two color lines is validate", true, map.validateMove(moveBetweenLines));
		map.doMove(moveBetweenLines);

		// tile between two correct lines
		assertEquals("tile with no neighbour is validate", false, map.validateMove(moveWithNoNeighbour));

		// tile between two correct lines
		assertEquals("line with no neighbour is validate", false, map.validateMove(lineWithNoNeighbour));

		// tiles are not in one line
		assertEquals("tiles in different line are validate", false, map.validateMove(moveInDifferentLines));

		// help move to create szenario for next test
		assertEquals("help line 2 is not validate", true, map.validateMove(helpMove2));
		map.doMove(helpMove2);

		// tiles are not in one line
		assertEquals("", false, thirdMap.validateMove(moveWithGap));

	}

	@Test
	public void getScoreTest() {
		WrongMove wrongMove = WrongMove.NOTHING;
		SlowMove slowMove = SlowMove.POINT_LOSS;

		Configuration config = new Configuration(7, 2, 6, 2, 2, wrongMove, 0, slowMove, 0, 2);

		MapLogic map = new MapLogic(config.getColorShapeCount());

		// second map to create a Qwirkles
		MapLogic secondMap = new MapLogic(config.getColorShapeCount());

		// create moves

		// tiles in horizontal line
		ArrayList<Tuple<Coordinate, Tile>> moveFirstTiles = new ArrayList<Tuple<Coordinate, Tile>>();
		moveFirstTiles.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 0, 1)));
		moveFirstTiles.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 0), newTile(2, 1, 1)));
		moveFirstTiles.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 0), newTile(7, 4, 1)));
		moveFirstTiles.add(new Tuple<Coordinate, Tile>(new Coordinate(3, 0), newTile(8, 3, 1)));

		// tiles in vertrical line
		ArrayList<Tuple<Coordinate, Tile>> moveInVerticalLine = new ArrayList<Tuple<Coordinate, Tile>>();
		moveInVerticalLine.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 1), newTile(4, 0, 2)));
		moveInVerticalLine.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 2), newTile(12, 0, 4)));
		moveInVerticalLine.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 3), newTile(13, 0, 5)));
		moveInVerticalLine.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 4), newTile(14, 0, 3)));
		moveInVerticalLine.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 5), newTile(15, 0, 7)));

		// color line down to create scenario for next test
		ArrayList<Tuple<Coordinate, Tile>> helpMove = new ArrayList<Tuple<Coordinate, Tile>>();
		helpMove.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 1), newTile(18, 4, 3)));
		helpMove.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 2), newTile(19, 4, 4)));

		// tile between two lines
		ArrayList<Tuple<Coordinate, Tile>> moveBetweenLines = new ArrayList<Tuple<Coordinate, Tile>>();
		moveBetweenLines.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 2), newTile(20, 2, 4)));
		moveBetweenLines.add(new Tuple<Coordinate, Tile>(new Coordinate(3, 2), newTile(30, 3, 4)));

		// horizontal line, to create scenario for next test
		ArrayList<Tuple<Coordinate, Tile>> helpMove2 = new ArrayList<Tuple<Coordinate, Tile>>();
		helpMove2.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 5), newTile(27, 1, 7)));

		// move with two lines
		ArrayList<Tuple<Coordinate, Tile>> moveWithMulitpleLines = new ArrayList<Tuple<Coordinate, Tile>>();
		moveWithMulitpleLines.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 5), newTile(28, 4, 7)));
		moveWithMulitpleLines.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 4), newTile(29, 4, 2)));
		moveWithMulitpleLines.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 3), newTile(30, 4, 5)));

		// qwirkle on first move
		ArrayList<Tuple<Coordinate, Tile>> qwirkleOnFirstMove = new ArrayList<Tuple<Coordinate, Tile>>();
		qwirkleOnFirstMove.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 4, 1)));
		qwirkleOnFirstMove.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 1), newTile(2, 4, 2)));
		qwirkleOnFirstMove.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 2), newTile(3, 4, 3)));
		qwirkleOnFirstMove.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 3), newTile(4, 4, 4)));
		qwirkleOnFirstMove.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 4), newTile(5, 4, 5)));
		qwirkleOnFirstMove.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 5), newTile(6, 4, 6)));
		qwirkleOnFirstMove.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 6), newTile(7, 4, 7)));

		// help move for double qwirkle
		ArrayList<Tuple<Coordinate, Tile>> helpForDoubleQwirkle = new ArrayList<Tuple<Coordinate, Tile>>();
		helpForDoubleQwirkle.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 0), newTile(8, 1, 1)));
		helpForDoubleQwirkle.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 0), newTile(9, 2, 1)));
		helpForDoubleQwirkle.add(new Tuple<Coordinate, Tile>(new Coordinate(3, 0), newTile(10, 3, 1)));
		helpForDoubleQwirkle.add(new Tuple<Coordinate, Tile>(new Coordinate(4, 0), newTile(11, 5, 1)));
		helpForDoubleQwirkle.add(new Tuple<Coordinate, Tile>(new Coordinate(5, 0), newTile(12, 6, 1)));

		// double qwirkle
		ArrayList<Tuple<Coordinate, Tile>> doubleQwirkle = new ArrayList<Tuple<Coordinate, Tile>>();
		doubleQwirkle.add(new Tuple<Coordinate, Tile>(new Coordinate(6, 0), newTile(13, 7, 1)));
		doubleQwirkle.add(new Tuple<Coordinate, Tile>(new Coordinate(6, 1), newTile(14, 6, 1)));
		doubleQwirkle.add(new Tuple<Coordinate, Tile>(new Coordinate(6, 2), newTile(15, 5, 1)));
		doubleQwirkle.add(new Tuple<Coordinate, Tile>(new Coordinate(6, 3), newTile(16, 4, 1)));
		doubleQwirkle.add(new Tuple<Coordinate, Tile>(new Coordinate(6, 4), newTile(17, 3, 1)));
		doubleQwirkle.add(new Tuple<Coordinate, Tile>(new Coordinate(6, 5), newTile(18, 2, 1)));
		doubleQwirkle.add(new Tuple<Coordinate, Tile>(new Coordinate(6, 6), newTile(19, 1, 1)));

		// assert statements
		// tile on empty field
		assertEquals("wrong score for first tile on map", 4, map.getScore(moveFirstTiles));
		map.doMove(moveFirstTiles);

		// all tiles in a vertical line
		assertEquals("wrong score for line", 6, map.getScore(moveInVerticalLine));
		map.doMove(moveInVerticalLine);

		// help move to create szenario for next test
		assertEquals("wrong score for help move", 3, map.getScore(helpMove));
		map.doMove(helpMove);

		// tile between two correct lines
		assertEquals("wrong score for move between lines", 4, map.getScore(moveBetweenLines));
		map.doMove(moveBetweenLines);

		// help move to create szenario for next test
		assertEquals("wrong score for help 2 move", 2, map.getScore(helpMove2));
		map.doMove(helpMove2);

		// move with two lines
		assertEquals("wrong score for move with two lines", 9, map.getScore(moveWithMulitpleLines));
		map.doMove(moveWithMulitpleLines);

		// qwirkle on first move
		assertEquals("wrong score for qwirkle on first move", 14, secondMap.getScore(qwirkleOnFirstMove));
		secondMap.doMove(qwirkleOnFirstMove);

		// help move for double qwirkle
		assertEquals("wrong score for help move", 6, secondMap.getScore(helpForDoubleQwirkle));
		secondMap.doMove(helpForDoubleQwirkle);

		// horizontal qwirkle
		assertEquals("wrong score for double qwirkle", 28, secondMap.getScore(doubleQwirkle));
		secondMap.doMove(doubleQwirkle);

		// second map to create a Qwirkles
		MapLogic thirdMap = new MapLogic(config.getColorShapeCount());

		// create moves

		/*
		 * Create a third map to test another scenario
		 */
		
		ArrayList<Tuple<Coordinate, Tile>> addToMap = new ArrayList<Tuple<Coordinate, Tile>>();
		addToMap.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 0, 1)));
		addToMap.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 0), newTile(2, 0, 2)));
		addToMap.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 1), newTile(2, 1, 3)));
		addToMap.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 2), newTile(2, 2, 4)));

		thirdMap.doMove(addToMap);

		ArrayList<Tuple<Coordinate, Tile>> moveWithTileBelow = new ArrayList<Tuple<Coordinate, Tile>>();
		moveWithTileBelow.add(new Tuple<Coordinate, Tile>(new Coordinate(1, -1), newTile(2, 3, 5)));
		
		assertEquals("wrong score", 4, thirdMap.getScore(moveWithTileBelow));
	}

	@Test
	public void isTileOnCoordinateTest() {
		/*
		 * build map
		 */
		MapLogic map = new MapLogic(6);
		ArrayList<Tuple<Coordinate, Tile>> tiles = new ArrayList<Tuple<Coordinate, Tile>>();
		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 1, 1)));
		map.doMove(tiles);

		try {
			Class param[] = { List.class };
			Method method = map.getClass().getDeclaredMethod("isTileOnCoordinate", param);
			method.setAccessible(true);

			List<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 1, 1)));

			/*
			 * tile on coordinate
			 */
			boolean isTileOn = (boolean) method.invoke(map, move);
			assertEquals("Tile is on position", true, isTileOn);
			move.clear();

			/*
			 * no tile on coordinate
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 0), newTile(1, 1, 1)));
			isTileOn = (boolean) method.invoke(map, move);
			assertEquals("No tile is on position", false, isTileOn);
			move.clear();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void tileOnZeroTest() {
		/*
		 * build map
		 */
		MapLogic map = new MapLogic(6);

		try {
			Class param[] = { List.class };
			Method method = map.getClass().getDeclaredMethod("tileOnZero", param);
			method.setAccessible(true);

			List<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 1, 1)));

			/*
			 * tile on zero
			 */
			boolean isTileOn = (boolean) method.invoke(map, move);
			assertEquals("Tile is on zero", true, isTileOn);
			move.clear();

			/*
			 * no tile on zero
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 0), newTile(1, 1, 1)));
			isTileOn = (boolean) method.invoke(map, move);
			assertEquals("Tile is not on zero", false, isTileOn);
			move.clear();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void isConnectedHorizontalVerticalTest() {
		/*
		 * build map
		 */
		MapLogic map = new MapLogic(6);
		ArrayList<Tuple<Coordinate, Tile>> tiles = new ArrayList<Tuple<Coordinate, Tile>>();
		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 1, 1)));
		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 1), newTile(2, 2, 1)));
		map.doMove(tiles);

		try {
			Class param[] = { List.class };
			Method method = map.getClass().getDeclaredMethod("isConnectedHorizontalVertical", param);
			method.setAccessible(true);

			List<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();

			/*
			 * horizontal line with no gap
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -1), newTile(3, 3, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 2), newTile(4, 4, 1)));
			boolean isConnected = (boolean) method.invoke(map, move);
			assertEquals("Move has no horizontal gap", true, isConnected);
			move.clear();

			/*
			 * horizontal line with gap
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -2), newTile(3, 3, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 2), newTile(4, 4, 1)));
			isConnected = (boolean) method.invoke(map, move);
			assertEquals("Move has horizontal gap", false, isConnected);
			move.clear();

			/*
			 * vertical line with no gap
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 0), newTile(3, 3, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(-1, 0), newTile(4, 4, 1)));
			isConnected = (boolean) method.invoke(map, move);
			assertEquals("Move has no vertical gap", true, isConnected);
			move.clear();

			/*
			 * vertical line with gap
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 0), newTile(3, 3, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(-1, 0), newTile(4, 4, 1)));
			isConnected = (boolean) method.invoke(map, move);
			assertEquals("Move has vertical gap", false, isConnected);
			move.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void isHorizontalTest() {
		/*
		 * build map
		 */
		MapLogic map = new MapLogic(6);

		try {
			Class param[] = { List.class };
			Method method = map.getClass().getDeclaredMethod("isHorizontal", param);
			method.setAccessible(true);

			List<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();

			/*
			 * horizontal move
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 1, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 0), newTile(1, 1, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 0), newTile(1, 1, 1)));

			boolean isHorizontal = (boolean) method.invoke(map, move);
			assertEquals("Move is horizontal", true, isHorizontal);
			move.clear();

			/*
			 * not horizontal move
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 1, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 0), newTile(1, 1, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 1), newTile(1, 1, 1)));

			isHorizontal = (boolean) method.invoke(map, move);
			assertEquals("Move is not horizontal", false, isHorizontal);
			move.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void isVerticalTest() {
		/*
		 * build map
		 */
		MapLogic map = new MapLogic(6);

		try {
			Class param[] = { List.class };
			Method method = map.getClass().getDeclaredMethod("isVertical", param);
			method.setAccessible(true);

			List<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();

			/*
			 * horizontal move
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 1, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 1), newTile(1, 1, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 2), newTile(1, 1, 1)));

			boolean isHorizontal = (boolean) method.invoke(map, move);
			assertEquals("Move is vertical", true, isHorizontal);
			move.clear();

			/*
			 * not horizontal move
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 1, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 1), newTile(1, 1, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 2), newTile(1, 1, 1)));

			isHorizontal = (boolean) method.invoke(map, move);
			assertEquals("Move is not vertical", false, isHorizontal);
			move.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void sameColorShapeInMoveTest() {
		/*
		 * build map
		 */
		MapLogic map = new MapLogic(6);

		try {
			Class param[] = { List.class };
			Method method = map.getClass().getDeclaredMethod("sameColorShapeInMove", param);
			method.setAccessible(true);

			List<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();

			/*
			 * same shape
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -1), newTile(1, 2, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -2), newTile(2, 1, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -3), newTile(3, 3, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -4), newTile(4, 5, 1)));
			boolean isConnected = (boolean) method.invoke(map, move);
			assertEquals("Tiles in move have the same shape", true, isConnected);
			move.clear();

			/*
			 * different shapes
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -1), newTile(1, 2, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -2), newTile(2, 1, 2)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -3), newTile(3, 3, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -4), newTile(4, 5, 1)));
			isConnected = (boolean) method.invoke(map, move);
			assertEquals("Tiles in move have the same shape", false, isConnected);
			move.clear();

			/*
			 * same color
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -1), newTile(1, 1, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -2), newTile(2, 1, 2)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -3), newTile(3, 1, 3)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -4), newTile(4, 1, 4)));
			isConnected = (boolean) method.invoke(map, move);
			assertEquals("Tiles in move have the same color", true, isConnected);
			move.clear();

			/*
			 * different color
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -1), newTile(1, 1, 1)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -2), newTile(2, 1, 2)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -3), newTile(3, 3, 3)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -4), newTile(4, 1, 2)));
			isConnected = (boolean) method.invoke(map, move);
			assertEquals("Tiles in move have the same color", false, isConnected);
			move.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void isHorizontalCompatibleTest() {
		/*
		 * build map
		 */
		MapLogic map = new MapLogic(6);
		ArrayList<Tuple<Coordinate, Tile>> tiles = new ArrayList<Tuple<Coordinate, Tile>>();
		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 3, 1)));
		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 1), newTile(2, 2, 1)));

		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(-2, 0), newTile(3, 3, 2)));
		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(-3, 0), newTile(4, 3, 3)));

		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 0), newTile(5, 5, 5)));
		map.doMove(tiles);

		try {
			Class param[] = { Coordinate.class, int.class, int.class };
			Method method = map.getClass().getDeclaredMethod("isHorizontalCompatible", param);
			method.setAccessible(true);

			ArrayList<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();

			/*
			 * compatible move the tile left and right
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(-1, 0), newTile(6, 3, 4)));
			map.doMove(move);

			boolean isConnected = (boolean) method.invoke(map, new Coordinate(-1, 0), 3, 4);
			assertEquals("Move is compatible", true, isConnected);
			move.clear();

			/*
			 * compatible move on left and right side of an existing line
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(-4, 0), newTile(7, 3, 5)));
			map.doMove(move);

			isConnected = (boolean) method.invoke(map, new Coordinate(-4, 0), 3, 5);
			assertEquals("Move is compatible", true, isConnected);
			move.clear();

			/*
			 * tile is not connected
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(-5, 0), newTile(6, 4, 5)));
			map.doMove(move);

			isConnected = (boolean) method.invoke(map, new Coordinate(-5, 0), 4, 5);
			assertEquals("Move is not compatible", false, isConnected);
			move.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void isVerticalCompatibleTest() {
		/*
		 * build map
		 */
		MapLogic map = new MapLogic(6);
		ArrayList<Tuple<Coordinate, Tile>> tiles = new ArrayList<Tuple<Coordinate, Tile>>();
		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 3, 1)));
		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 1), newTile(2, 2, 1)));

		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(-2, 0), newTile(3, 3, 2)));
		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(-3, 0), newTile(4, 3, 3)));

		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 0), newTile(5, 5, 5)));
		map.doMove(tiles);

		try {
			Class param[] = { Coordinate.class, int.class, int.class };
			Method method = map.getClass().getDeclaredMethod("isVerticalCompatible", param);
			method.setAccessible(true);

			ArrayList<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();

			/*
			 * compatible move the tile left and right
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 2), newTile(6, 1, 1)));
			map.doMove(move);

			boolean isConnected = (boolean) method.invoke(map, new Coordinate(0, 2), 1, 1);
			assertEquals("Move is compatible", true, isConnected);
			move.clear();

			/*
			 * tile is already in line
			 */
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 3), newTile(7, 1, 1)));
			map.doMove(move);

			isConnected = (boolean) method.invoke(map, new Coordinate(0, 3), 1, 1);
			assertEquals("Move is not compatible", false, isConnected);
			move.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void compatibleWithTilesOnMapTest() {
		/*
		 * build map
		 */
		MapLogic map = new MapLogic(6);
		ArrayList<Tuple<Coordinate, Tile>> tiles = new ArrayList<Tuple<Coordinate, Tile>>();
		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 3, 1)));
		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 1), newTile(2, 2, 1)));

		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(-2, 0), newTile(3, 3, 3)));
		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(-3, 0), newTile(4, 3, 2)));

		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 0), newTile(5, 5, 5)));
		map.doMove(tiles);

		try {
			Class param[] = { List.class };
			Method method = map.getClass().getDeclaredMethod("compatibleWithTilesOnMap", param);
			method.setAccessible(true);

			ArrayList<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();

			move.add(new Tuple<Coordinate, Tile>(new Coordinate(-4, 0), newTile(6, 3, 4)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(-1, 0), newTile(6, 3, 5)));
			map.doMove(move);

			boolean isConnected = (boolean) method.invoke(map, move);
			assertEquals("Move is compatible", true, isConnected);
			move.clear();

			move.add(new Tuple<Coordinate, Tile>(new Coordinate(-5, 0), newTile(6, 3, 5)));
			map.doMove(move);

			isConnected = (boolean) method.invoke(map, move);
			assertEquals("same tile in line", false, isConnected);
			move.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void gapOnMapTest() {
		/*
		 * build map
		 */
		MapLogic map = new MapLogic(6);
		ArrayList<Tuple<Coordinate, Tile>> tiles = new ArrayList<Tuple<Coordinate, Tile>>();
		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), newTile(1, 3, 1)));
		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 1), newTile(2, 2, 1)));

		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(-2, 0), newTile(3, 3, 3)));
		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(-3, 0), newTile(4, 3, 2)));

		tiles.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 0), newTile(5, 5, 5)));
		map.doMove(tiles);

		try {
			Class param[] = { List.class };
			Method method = map.getClass().getDeclaredMethod("gapOnMap", param);
			method.setAccessible(true);

			ArrayList<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();

			move.add(new Tuple<Coordinate, Tile>(new Coordinate(-4, 0), newTile(6, 3, 4)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(-1, 0), newTile(6, 3, 5)));

			boolean isConnected = (boolean) method.invoke(map, move);
			assertEquals("Move is connected", false, isConnected);
			move.clear();

			move.add(new Tuple<Coordinate, Tile>(new Coordinate(-5, 1), newTile(6, 3, 5)));
			move.add(new Tuple<Coordinate, Tile>(new Coordinate(-6, 1), newTile(6, 3, 4)));

			isConnected = (boolean) method.invoke(map, move);
			assertEquals("line is not connected to map", true, isConnected);
			move.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Tile newTile(int id, int color, int shape) {
		return new Tile(color, shape, id);
	}

}
