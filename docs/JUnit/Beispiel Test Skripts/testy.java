package org.AILogic;

import java.util.ArrayList;
import java.util.HashMap;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.Board.Tuple;
import org.junit.Test;

import com.google.common.collect.HashBasedTable;

import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.SlowMove;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.WrongMove;

import static org.junit.Assert.assertEquals;

public class AiLogicTest {

	private AiLogic logic;
	private Configuration config;

	public AiLogicTest() {
		config = new Configuration(7, 4, 6, 30, 5, WrongMove.NOTHING, 1, SlowMove.POINT_LOSS, 1, 2);
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

		assertEquals("Wrong number of tiles in horizontalHead", 3, horizontalHead.size());
		assertEquals("Wrong number of tiles in verticalHead", 2, verticalHead.size());

		// Add new tile
		ArrayList<Tuple<Coordinate, Tile>> move2 = new ArrayList<Tuple<Coordinate, Tile>>();

		move2.add(new Tuple<Coordinate, Tile>(new Coordinate(0, -1), new Tile(0, 2, 4)));

		logic.updateMap(move2);

		horizontalHead = logic.getHozirontalHead();
		verticalHead = logic.getVerticalHead();

		assertEquals("Wrong number of tiles in horizontalHead", 4, horizontalHead.size());
		assertEquals("Wrong number of tiles in verticalHead", 2, verticalHead.size());

		// Add three new tiles
		ArrayList<Tuple<Coordinate, Tile>> move3 = new ArrayList<Tuple<Coordinate, Tile>>();

		move3.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 0), new Tile(0, 2, 5)));
		move3.add(new Tuple<Coordinate, Tile>(new Coordinate(-1, 0), new Tile(0, 1, 6)));
		move3.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 2), new Tile(0, 4, 7)));
		move3.add(new Tuple<Coordinate, Tile>(new Coordinate(4, 6), new Tile(0, 3, 8)));

		logic.updateMap(move3);

		horizontalHead = logic.getHozirontalHead();
		verticalHead = logic.getVerticalHead();

		assertEquals("Wrong number of tiles in horizontalHead", 7, horizontalHead.size());
		assertEquals("Wrong number of tiles in verticalHead", 6, verticalHead.size());
	}

	@Test
	public void findMoveTest() {
		// add tiles to map
		ArrayList<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();

		move.add(new Tuple<Coordinate, Tile>(new Coordinate(0, 1), new Tile(0, 0, 31)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 1), new Tile(0, 1, 32)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(4, 1), new Tile(0, 2, 33)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(4, 2), new Tile(1, 2, 34)));
		move.add(new Tuple<Coordinate, Tile>(new Coordinate(5, 0), new Tile(2, 2, 35)));
		//move.add(new Tuple<Coordinate, Tile>(new Coordinate(6, 0), new Tile(2, 4, 36)));

		logic.updateMap(move);
		move.clear();

		// no tiles fit
		ArrayList<Tile> hand = new ArrayList<Tile>();
		hand.add(new Tile(6, 3, 4));
		hand.add(new Tile(6, 5, 5));
		hand.add(new Tile(3, 5, 6));
		hand.add(new Tile(3, 3, 7));
		hand.add(new Tile(4, 3, 8));
		hand.add(new Tile(5, 5, 9));

		logic.addTilesToHand(hand);

		assertEquals("a move hat wrong number of tiles", null, logic.findMove());

		// add tiles to hand (two tiles are Ok)
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

		assertEquals("the move has wrong numbers of tiles", 2, firstMove.size());
		assertEquals("not all tiles were used", true, tileList.contains(arrayHand[0].get(0)));
		assertEquals("not all tiles were used", true, tileList.contains(arrayHand[0].get(1)));

		// test right - left
		// change tiles in hand (four tiles are Ok)
		
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
		
		tileList.clear();
	
		//test Symbols
		removeHand.add(new Tile(0, 4, 3));
		removeHand.add(new Tile(0, 6, 10));
		removeHand.add(new Tile(0, 3, 1));
		removeHand.add(new Tile(0, 5, 2));

		logic.removeTilesFromHand(removeHand);
		
		removeHand.clear();

		addHand.add(new Tile(3, 2, 24));
		addHand.add(new Tile(4, 5, 25));
		addHand.add(new Tile(5, 5, 26));
		addHand.add(new Tile(5, 6, 27));

		logic.addTilesToHand(addHand);
		
		addHand.clear();
		
		//test1: a move with the most score was found
		ArrayList<Tuple<Coordinate, Tile>> verticalMove = logic.findMove();
		ArrayList<Tile> tileListVertical = getTileList(verticalMove);
		ArrayList<Coordinate> coordinateList = getCoordinateList(verticalMove);
		
		assertEquals("the move has wrong numbers of tiles", 1, verticalMove.size());
		assertEquals("not all tiles were used", true, tileList.contains(arrayHand[0].get(0)));
		assertEquals(" was found not the best move", true, coordinateList.contains(new Coordinate(4, 0)));
		
		verticalMove.clear();
		coordinateList.clear();
		
		//test2: a move with the most score was found
		//add to Hand second tile with shape 2
		removeHand.add(new Tile(4, 5, 25));

		logic.removeTilesFromHand(removeHand);
		
		removeHand.clear();
		
		addHand.add(new Tile(5, 2, 28));

		logic.addTilesToHand(addHand);
		
		addHand.clear();
		verticalMove = logic.findMove();
		tileListVertical = getTileList(verticalMove);
		coordinateList = getCoordinateList(verticalMove);
		
		assertEquals("the move has wrong numbers of tiles", 2, verticalMove.size());
		assertEquals(" was found not the best move", true, coordinateList.contains(new Coordinate(5, 1)));
		//assertEquals(" was found not the best move", true, coordinateList.contains(new Coordinate(5, 2)));
		
		System.out.println(verticalMove.get(1).getFirst());
		System.out.println(verticalMove.get(1).getSecond().getUniqueId());
		
		//move vertical with shapes
		
		
		
		//move.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 0), new Tile(0, 2, 36)));
		//move.add(new Tuple<Coordinate, Tile>(new Coordinate(1, 2), new Tile(0, 0, 37)));
		//move.add(new Tuple<Coordinate, Tile>(new Coordinate(2, 2), new Tile(0, 1, 38)));
		//logic.updateMap(move);
		//move.clear();
	}

}
