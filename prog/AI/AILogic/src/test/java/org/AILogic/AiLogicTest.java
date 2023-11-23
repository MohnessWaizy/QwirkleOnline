package org.AILogic;

import java.util.ArrayList;
import java.util.HashMap;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.Board.MapLogic;
import org.GameLogic.Board.Tuple;
import org.junit.Test;

import com.google.common.collect.HashBasedTable;

import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.SlowMove;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.WrongMove;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AiLogicTest {

	private AiLogic logic;
	private AiLogic logic1;
	private Configuration config;

	public AiLogicTest() {
		config = new Configuration(7, 4, 6, 30, 5, WrongMove.NOTHING, 1, SlowMove.POINT_LOSS, -1, 2);
		logic = new AiLogic(config);

	}

	/**
	 * Convert ArrayList<Tuple<Coordinate, Tile>> to ArrayList<Tile>
	 * 
	 * @param list List that will be converted
	 * @return ArrayList with tiles
	 */
	private ArrayList<Tile> getTileList(ArrayList<Tuple<Coordinate, Tile>> list) {
		if (list == null) {
			return null;
		}
		ArrayList<Tile> tileList = new ArrayList<Tile>();
		for (int i = 0; i < list.size(); i++) {
			tileList.add(list.get(i).getSecond());
		}
		return tileList;
	}

	/**
	 * Convert ArrayList<Tuple<Coordinate, Tile>> to ArrayCoordinate<Coordinate>
	 * 
	 * @param list List that will be converted
	 * @return ArrayList with Coordinates
	 */
	private ArrayList<Coordinate> getCoordinateList(ArrayList<Tuple<Coordinate, Tile>> list) {
		if (list == null) {
			return null;
		}
		ArrayList<Coordinate> coordinateList = new ArrayList<Coordinate>();
		for (int i = 0; i < list.size(); i++) {
			coordinateList.add(list.get(i).getFirst());
		}
		return coordinateList;
	}

	@Test
	public void addTilesToHandTest() {

		ArrayList<Tile> hand = new ArrayList<Tile>();
		hand.add(new Tile(0, 1, 1));
		hand.add(new Tile(1, 1, 2));
		hand.add(new Tile(2, 1, 3));
		hand.add(new Tile(3, 1, 4));
		hand.add(new Tile(4, 1, 5));
		hand.add(new Tile(5, 1, 6));

		logic.addTilesToHand(hand);

		ArrayList<Tile>[] colorHand = logic.getColorHand();

		int tileCount = 0;

		for (int i = 0; i < config.getColorShapeCount(); i++) {
			tileCount = tileCount + colorHand[i].size();
		}

		/*
		 * Check if all Tiles are on hand
		 */
		assertEquals("Wrong number of tiles in hand", tileCount, hand.size());

		/*
		 * Check if Tiles are sorted by color
		 */
		assertEquals("Hand is not sorted by color", 2, colorHand[2].get(0).getColor());

		ArrayList<Tile> removeHand = new ArrayList<Tile>();
		removeHand.add(new Tile(0, 1, 1));
		removeHand.add(new Tile(1, 1, 2));

		logic.removeTilesFromHand(removeHand);

		ArrayList<Tile> addHand = new ArrayList<Tile>();
		addHand.add(new Tile(0, 3, 7));
		addHand.add(new Tile(2, 5, 8));

		logic.addTilesToHand(addHand);

		colorHand = logic.getColorHand();
		tileCount = 0;

		for (int i = 0; i < config.getColorShapeCount(); i++) {
			tileCount = tileCount + colorHand[i].size();
		}

		/*
		 * Check if all Tiles are on hand
		 */
		assertEquals("Wrong number of tiles after add new tiles", tileCount, hand.size());

		/*
		 * Check if Tiles are sorted by color after add new
		 */
		assertEquals("New Tiles are not sorted", 2, colorHand[2].size());

		/*
		 * Check if Tile is played at the beginning
		 */
		assertEquals("Tile is played on wrong position in ArrayList", 7, colorHand[0].get(0).getUniqueId());

	}

	@Test
	public void removeTilesFormHandTest() {
		ArrayList<Tile> hand = new ArrayList<Tile>();
		hand.add(new Tile(0, 1, 1));
		hand.add(new Tile(1, 1, 2));
		hand.add(new Tile(2, 1, 3));
		hand.add(new Tile(3, 1, 4));
		hand.add(new Tile(4, 1, 5));
		hand.add(new Tile(5, 1, 6));

		logic.addTilesToHand(hand);

		ArrayList<Tile> removeHand = new ArrayList<Tile>();
		removeHand.add(new Tile(0, 1, 1));
		removeHand.add(new Tile(1, 1, 2));

		logic.removeTilesFromHand(removeHand);

		ArrayList<Tile>[] colorHand = logic.getColorHand();

		int tileCount = 0;

		for (int i = 0; i < config.getColorShapeCount(); i++) {
			tileCount = tileCount + colorHand[i].size();
		}

		/*
		 * check if Tiles are removed
		 */
		assertEquals("Wrong number of tiles in hand", tileCount, hand.size() - removeHand.size());
		assertEquals("Wrong number of tiles in hand", 4, logic.getHand().size());

		logic.removeTilesFromHand(removeHand);
	}

	@Test
	public void updateMapTest() {
		ArrayList<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();

		move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), new Tile(0, 0, 1)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 1), new Tile(0, 1, 2)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 2), new Tile(0, 3, 3)));

		logic.updateMap(move);

		HashBasedTable<Integer, Integer, Tile> horizontalHead = logic.getHozirontalHead();
		HashBasedTable<Integer, Integer, Tile> verticalHead = logic.getVerticalHead();

		/*
		 * check, if the number of tiles in horizontalHead and verticalHead are ok
		 */
		assertEquals("Wrong number of tiles in horizontalHead", 3, horizontalHead.size());
		assertEquals("Wrong number of tiles in verticalHead", 2, verticalHead.size());

		ArrayList<Tuple<Coordinate, Tile>> move2 = new ArrayList<Tuple<Coordinate, Tile>>();

		move2.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -1), new Tile(0, 2, 4)));

		logic.updateMap(move2);

		horizontalHead = logic.getHozirontalHead();
		verticalHead = logic.getVerticalHead();

		/*
		 * check, if the number of tiles in horizontalHead and verticalHead are ok
		 */
		assertEquals("Wrong number of tiles in horizontalHead", 4, horizontalHead.size());
		assertEquals("Wrong number of tiles in verticalHead", 2, verticalHead.size());

		ArrayList<Tuple<Coordinate, Tile>> move3 = new ArrayList<Tuple<Coordinate, Tile>>();

		move3.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 0), new Tile(0, 2, 5)));
		move3.add(new Tuple<Coordinate, Tile>(new Coordinate(-1, 0), new Tile(0, 1, 6)));
		move3.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 2), new Tile(0, 4, 7)));
		move3.add(new Tuple<Coordinate, Tile>(new Coordinate(4, 6), new Tile(0, 3, 8)));

		logic.updateMap(move3);

		horizontalHead = logic.getHozirontalHead();
		verticalHead = logic.getVerticalHead();

		/*
		 * check, if the number of tiles in horizontalHead and verticalHead are ok
		 */
		assertEquals("Wrong number of tiles in horizontalHead", 7, horizontalHead.size());
		assertEquals("Wrong number of tiles in verticalHead", 6, verticalHead.size());
	}

	@Test
	public void findMoveTest() {
		ArrayList<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();

		move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 1), new Tile(0, 0, 31)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 1), new Tile(0, 1, 32)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(4, 1), new Tile(0, 2, 33)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(4, 2), new Tile(1, 2, 34)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(5, 0), new Tile(2, 2, 35)));

		logic.updateMap(move);
		move.clear();

		ArrayList<Tile> hand = new ArrayList<Tile>();
		hand.add(new Tile(6, 3, 4));
		hand.add(new Tile(6, 5, 5));
		hand.add(new Tile(3, 5, 6));
		hand.add(new Tile(3, 3, 7));
		hand.add(new Tile(6, 3, 8));
		hand.add(new Tile(6, 5, 9));

		logic.addTilesToHand(hand);

		/*
		 * check if there are fitting tiles (there are nobody)
		 */
		assertEquals("a move hat wrong number of tiles", null, logic.findMove());

		ArrayList<Tile> removeHand = new ArrayList<Tile>();
		removeHand.add(new Tile(6, 3, 4));
		removeHand.add(new Tile(6, 5, 5));

		logic.removeTilesFromHand(removeHand);

		removeHand.clear();

		ArrayList<Tile> addHand = new ArrayList<Tile>();
		addHand.add(new Tile(0, 3, 1));
		addHand.add(new Tile(0, 5, 2));

		logic.addTilesToHand(addHand);

		addHand.clear();

		ArrayList<Tile>[] arrayHand = logic.getColorHand();

		ArrayList<Tuple<Coordinate, Tile>> firstMove = logic.findMove();
		ArrayList<Tile> tileList = getTileList(firstMove);

		/*
		 * check, if all tiles, that in move there are, are chosen
		 */
		assertEquals("the move has wrong numbers of tiles", 3, firstMove.size());

		/*
		 * test, there are in move tiles, that they have the same color and shape
		 */
		boolean temp = false;
		for (int i = 0; i < firstMove.size() - 1; i++) {
			for (int j = i + 1; j < firstMove.size(); j++) {
				if (firstMove.get(i).getSecond().getColor() == firstMove.get(j).getSecond().getColor()
						&& firstMove.get(i).getSecond().getShape() == firstMove.get(j).getSecond().getShape()) {
					temp = true;
				}
			}
			assertTrue("There are more tiles, that they have the same color and shape ", temp == false);
		}

		/*
		 * check, if the score is ok
		 */
		assertEquals("a score is wrong", 6, logic.getScore(firstMove));

		removeHand.add(new Tile(3, 5, 6));
		removeHand.add(new Tile(3, 3, 7));

		logic.removeTilesFromHand(removeHand);

		removeHand.clear();

		addHand.add(new Tile(0, 4, 3));
		addHand.add(new Tile(0, 6, 10));

		logic.addTilesToHand(addHand);

		addHand.clear();

		arrayHand = logic.getColorHand();

		ArrayList<Tuple<Coordinate, Tile>> secondMove = logic.findMove();
		tileList = getTileList(secondMove);

		assertEquals("the move has wrong numbers of tiles", 4, secondMove.size());
		assertEquals("not all tiles were used", true, tileList.contains(arrayHand[0].get(0)));
		assertEquals("not all tiles were used", true, tileList.contains(arrayHand[0].get(1)));
		assertEquals("not all tiles were used", true, tileList.contains(arrayHand[0].get(2)));
		assertEquals("not all tiles were used", true, tileList.contains(arrayHand[0].get(3)));
		assertEquals("a score is wrong", 14, logic.getScore(secondMove));

		tileList.clear();

		removeHand.add(new Tile(0, 4, 3));
		removeHand.add(new Tile(0, 6, 10));
		removeHand.add(new Tile(0, 3, 1));
		removeHand.add(new Tile(0, 5, 2));

		logic.removeTilesFromHand(removeHand);

		removeHand.clear();

		addHand.add(new Tile(3, 2, 24));
		addHand.add(new Tile(4, 5, 25));
		addHand.add(new Tile(5, 5, 26));
		addHand.add(new Tile(6, 6, 27));

		logic.addTilesToHand(addHand);

		addHand.clear();

		ArrayList<Tuple<Coordinate, Tile>> verticalMove = logic.findMove();
		ArrayList<Tile> tileListVertical = getTileList(verticalMove);
		ArrayList<Coordinate> coordinateList = getCoordinateList(verticalMove);

		ArrayList<Tile>[] arrayShapeHand = logic.getShapeHand();

		/*
		 * check, if the best move was found
		 */
		assertEquals("the move has wrong numbers of tiles", 1, verticalMove.size());
		assertEquals("not all tiles were used", true, tileListVertical.contains(arrayShapeHand[2].get(0)));
		assertEquals(" was found not the best move", true, coordinateList.contains(new Coordinate(4, 0)));
		assertEquals("a score is wrong", 5, logic.getScore(verticalMove));

		verticalMove.clear();
		coordinateList.clear();

		removeHand.add(new Tile(4, 5, 25));

		logic.removeTilesFromHand(removeHand);

		removeHand.clear();

		addHand.add(new Tile(5, 2, 28));

		logic.addTilesToHand(addHand);

		addHand.clear();

		verticalMove = logic.findMove();
		tileListVertical = getTileList(verticalMove);
		coordinateList = getCoordinateList(verticalMove);

		/*
		 * check, if the best move was found
		 */
		assertEquals("the move has wrong numbers of tiles", 2, verticalMove.size());
		assertEquals("was found not the best move", true, coordinateList.contains(new Coordinate(5, 1)));
		assertEquals("was found not the best move", true, coordinateList.contains(new Coordinate(5, 2)));
		assertEquals("not all tiles were used", true, tileListVertical.contains(arrayShapeHand[2].get(0)));
		assertEquals("a score is wrong", 7, logic.getScore(verticalMove));
		verticalMove.clear();
		coordinateList.clear();

		removeHand.add(new Tile(3, 2, 24));
		removeHand.add(new Tile(5, 2, 28));
		removeHand.add(new Tile(5, 5, 26));

		logic.removeTilesFromHand(removeHand);

		removeHand.clear();

		addHand.add(new Tile(3, 0, 1));
		addHand.add(new Tile(4, 0, 2));
		addHand.add(new Tile(5, 0, 3));

		logic.addTilesToHand(addHand);

		addHand.clear();

		verticalMove = logic.findMove();
		tileListVertical = getTileList(verticalMove);
		coordinateList = getCoordinateList(verticalMove);
		arrayShapeHand = logic.getShapeHand();

		/*
		 * check, if the best move was found with the most score
		 */
		assertEquals("the move has wrong numbers of tiles", 3, verticalMove.size());

		/*
		 * test, there are in move tiles, that they have the same color and shape
		 */
		temp = false;
		for (int i = 0; i < verticalMove.size() - 1; i++) {
			for (int j = i + 1; j < verticalMove.size(); j++) {
				if (verticalMove.get(i).getSecond().getColor() == verticalMove.get(j).getSecond().getColor()
						&& verticalMove.get(i).getSecond().getShape() == verticalMove.get(j).getSecond().getShape()) {
					temp = true;
				}
			}
			assertTrue("There are in move more tiles, that they have the same color and shape ", temp == false);
		}
		/*
		 * check, if the best move was found with the most score
		 */
		assertEquals("was found not the best move1", true, coordinateList.contains(new Coordinate(0, 0)));
		assertEquals("a score is wrong", 5, logic.getScore(verticalMove));

		move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -1), new Tile(1, 0, 37)));
		logic.updateMap(move);
		move.clear();

		verticalMove = logic.findMove();
		tileListVertical = getTileList(verticalMove);
		coordinateList = getCoordinateList(verticalMove);
		arrayShapeHand = logic.getShapeHand();

		assertEquals("the move has wrong numbers of tiles", 3, verticalMove.size());

		/*
		 * test, there are in move tiles, that they have the same color and shape
		 */
		temp = false;
		for (int i = 0; i < verticalMove.size() - 1; i++) {
			for (int j = i + 1; j < verticalMove.size(); j++) {
				if (verticalMove.get(i).getSecond().getColor() == verticalMove.get(j).getSecond().getColor()
						&& verticalMove.get(i).getSecond().getShape() == verticalMove.get(j).getSecond().getShape()) {
					temp = true;
				}
			}
			assertTrue("There are in move more tiles, that they have the same color and shape ", temp == false);
		}

		/*
		 * check, if the best move was found with the most score
		 */
		assertEquals("was found not the best move1", true, coordinateList.contains(new Coordinate(0, 0)));
		assertEquals("a score is wrong", 6, logic.getScore(verticalMove));

		move.add(new Tuple<Coordinate, Tile>(new Coordinate(3, -2), new Tile(2, 6, 38)));
		logic.updateMap(move);
		move.clear();

		removeHand.add(new Tile(3, 0, 1));
		removeHand.add(new Tile(4, 0, 2));
		removeHand.add(new Tile(5, 0, 3));
		removeHand.add(new Tile(6, 3, 8));
		removeHand.add(new Tile(6, 5, 9));
		removeHand.add(new Tile(6, 6, 27));
		logic.removeTilesFromHand(removeHand);

		removeHand.clear();

		addHand.add(new Tile(1, 1, 11));
		addHand.add(new Tile(1, 6, 12));
		addHand.add(new Tile(5, 5, 13));
		addHand.add(new Tile(3, 5, 14));
		addHand.add(new Tile(4, 4, 15));
		addHand.add(new Tile(5, 6, 16));
		logic.addTilesToHand(addHand);

		addHand.clear();

		ArrayList<Tuple<Coordinate, Tile>> chooseMove = logic.findMove();

		/*
		 * check, if the best move was found with the most score
		 */
		assertEquals("the move has wrong numbers of tiles", 2, chooseMove.size());
		assertEquals("a score is wrong", 4, logic.getScore(chooseMove));
	}

	@Test
	public void findMoveTestBigMap() {
		config = new Configuration(7, 4, 6, 30, 5, WrongMove.NOTHING, 1, SlowMove.POINT_LOSS, 1, 2);
		logic1 = new AiLogic(config);

		ArrayList<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();

		move.add(new Tuple<Coordinate, Tile>(new Coordinate(-2, 2), new Tile(0, 2, 0)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(-2, 1), new Tile(0, 0, 1)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(-1, -1), new Tile(1, 4, 2)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 2), new Tile(1, 2, 3)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 3), new Tile(1, 3, 4)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -1), new Tile(2, 4, 5)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 0), new Tile(2, 1, 6)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 2), new Tile(2, 2, 7)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(1, -1), new Tile(3, 4, 8)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 0), new Tile(3, 1, 9)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(4, -2), new Tile(3, 3, 10)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(4, -1), new Tile(3, 4, 11)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(2, -1), new Tile(4, 4, 12)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 0), new Tile(4, 1, 13)));

		logic1.updateMap(move);
		move.clear();

		ArrayList<Tile> hand = new ArrayList<Tile>();
		hand.add(new Tile(1, 3, 20));
		hand.add(new Tile(2, 3, 21));
		hand.add(new Tile(3, 3, 22));
		hand.add(new Tile(3, 3, 23));
		hand.add(new Tile(6, 2, 24));
		hand.add(new Tile(6, 5, 25));

		logic1.addTilesToHand(hand);

		ArrayList<Tuple<Coordinate, Tile>> chooseMove = logic1.findMove();

		/*
		 * test, there are in move tiles, that they have the same color and shape
		 */
		boolean temp = false;
		for (int i = 0; i < chooseMove.size() - 1; i++) {
			for (int j = i + 1; j < chooseMove.size(); j++) {
				if (chooseMove.get(i).getSecond().getColor() == chooseMove.get(j).getSecond().getColor()
						&& chooseMove.get(i).getSecond().getShape() == chooseMove.get(j).getSecond().getShape()) {
					temp = true;
				}
			}
			assertTrue("There are more tiles, that they have the same color and shape ", temp == false);
		}
		/*
		 * check, if the best move was found with the most score
		 */
		assertTrue(11 == logic1.getScore(chooseMove));
	}

	/*
	 * There are - 5 points when there is wrong move
	 */
	@Test
	public void findMoveTestBigMapWrongMovePlusPunkte() {
		config = new Configuration(6, 4, 6, 30, 5, WrongMove.POINT_LOSS, -5, SlowMove.POINT_LOSS, 1, 2);
		logic1 = new AiLogic(config);
		MapLogic map = new MapLogic(6);
		// add tiles to map
		ArrayList<Tuple<Coordinate, Tile>> addToMap = new ArrayList<Tuple<Coordinate, Tile>>();

		// Hand
		ArrayList<Tile> hand = new ArrayList<Tile>();
		hand.add(new Tile(1, 3, 20));
		hand.add(new Tile(2, 3, 21));
		hand.add(new Tile(3, 3, 22));
		hand.add(new Tile(3, 3, 23));
		hand.add(new Tile(5, 2, 24));
		hand.add(new Tile(5, 2, 24));

		logic1.addTilesToHand(hand);

		assertEquals(false, map.validateMove(logic1.findMove()));
	}

	@Test
	public void findMoveEmptyMap() {
		config = new Configuration(12, 4, 10, 30, 5, WrongMove.POINT_LOSS, 5, SlowMove.POINT_LOSS, 1, 2);
		logic1 = new AiLogic(config);
		MapLogic map = new MapLogic(12);

		// Hand
		ArrayList<Tile> hand = new ArrayList<Tile>();
		hand.add(new Tile(1, 3, 20));
		hand.add(new Tile(2, 3, 21));
		hand.add(new Tile(3, 3, 22));
		hand.add(new Tile(4, 3, 23));
		hand.add(new Tile(5, 3, 24));
		hand.add(new Tile(6, 3, 25));
		hand.add(new Tile(1, 3, 26));
		hand.add(new Tile(8, 2, 27));
		hand.add(new Tile(9, 2, 28));
		hand.add(new Tile(10, 2, 29));

		logic1.addTilesToHand(hand);

		assertEquals(6, logic1.findMove().size());
		
		logic1.removeAllTilesFormHand();
		hand.clear();
		
		hand.add(new Tile(1, 1, 20));
		hand.add(new Tile(1, 2, 21));
		hand.add(new Tile(1, 3, 22));
		hand.add(new Tile(1, 1, 23));
		hand.add(new Tile(5, 4, 24));
		hand.add(new Tile(6, 5, 25));
		hand.add(new Tile(5, 6, 26));
		hand.add(new Tile(8, 7, 27));
		hand.add(new Tile(9, 8, 28));
		hand.add(new Tile(10, 9, 29));
		
		logic1.addTilesToHand(hand);

		assertEquals(3, logic1.findMove().size());
	}
}
