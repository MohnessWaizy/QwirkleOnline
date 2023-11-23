package org.AILogic;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.Board.MapLogic;
import org.GameLogic.Board.Tuple;
import org.junit.Test;

import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.SlowMove;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.WrongMove;

/**
 * This algorithm looks at every possible move and calculates the best one.
 */
public class DetailedFindMoveTest {

	private DetailedFindMove dfm;

	public DetailedFindMoveTest() {
		dfm = new DetailedFindMove();
		setUp();
	}

	private void setUp() {
		Configuration config = new Configuration(8, 4, 6, 30, 5, WrongMove.NOTHING, 1, SlowMove.POINT_LOSS, 1, 2);
		MapLogic map = new MapLogic(config.getColorShapeCount());
		int maxColorShape = config.getColorShapeCount();

		/*
		 * Set color hand
		 */
		ArrayList<Tile>[] colorHand = new ArrayList[maxColorShape];
		colorHand[0] = new ArrayList<Tile>();
		colorHand[0].add(new Tile(0, 3, 21));
		colorHand[0].add(new Tile(0, 6, 22));
		colorHand[0].add(new Tile(0, 7, 23));

		ArrayList<Tile>[] shapeHand = new ArrayList[maxColorShape];

		ArrayList<Tile> hand = new ArrayList<Tile>();

		/*
		 * Set tiles on map
		 */
		ArrayList<Tuple<Coordinate, Tile>> move = new ArrayList<Tuple<Coordinate, Tile>>();
		move.add(new Tuple<Coordinate, Tile>(Coordinate.of(0, 0), new Tile(0, 0, 1)));
		move.add(new Tuple<Coordinate, Tile>(Coordinate.of(1, 0), new Tile(0, 1, 2)));
		move.add(new Tuple<Coordinate, Tile>(Coordinate.of(2, 0), new Tile(0, 2, 3)));
		move.add(new Tuple<Coordinate, Tile>(Coordinate.of(4, 0), new Tile(0, 4, 6)));
		move.add(new Tuple<Coordinate, Tile>(Coordinate.of(5, 0), new Tile(0, 5, 7)));
		move.add(new Tuple<Coordinate, Tile>(Coordinate.of(-2, 0), new Tile(0, 3, 4)));
		move.add(new Tuple<Coordinate, Tile>(Coordinate.of(-3, 0), new Tile(0, 4, 5)));

		move.add(new Tuple<Coordinate, Tile>(Coordinate.of(0, 10), new Tile(0, 1, 4)));
		move.add(new Tuple<Coordinate, Tile>(Coordinate.of(1, 10), new Tile(1, 1, 5)));

		move.add(new Tuple<Coordinate, Tile>(Coordinate.of(0, 15), new Tile(0, 1, 4)));
		move.add(new Tuple<Coordinate, Tile>(Coordinate.of(1, 15), new Tile(1, 1, 5)));
		move.add(new Tuple<Coordinate, Tile>(Coordinate.of(3, 15), new Tile(1, 1, 5)));

		map.doMove(move);

		dfm.updateData(map, colorHand, shapeHand, hand, maxColorShape);
	}

	@Test
	public void getTilesInLineTest() {
		try {
			Coordinate coord = new Coordinate(2, 0);

			Class<?> clazz = DetailedFindMove.class;
			// Object obj = clazz.newInstance();
			Class param[] = { Coordinate.class, char.class };
			Method method = dfm.getClass().getDeclaredMethod("getTilesInLine", param);
			method.setAccessible(true);

			char dir = 'l';

			ArrayList<Tile> line = (ArrayList<Tile>) method.invoke(dfm, coord, dir);
			ArrayList<Tile> exp = new ArrayList<Tile>();
			exp.add(new Tile(0, 2, 3));
			exp.add(new Tile(0, 1, 2));
			exp.add(new Tile(0, 0, 1));

			assertEquals("The correct number of tiles was not output", 3, line.size());
			assertEquals("Wrong tile in List", exp.get(0), line.get(0));
			assertEquals("Wrong tile in List", exp.get(1), line.get(1));
			assertEquals("Wrong tile in List", exp.get(2), line.get(2));

			coord = new Coordinate(-2, 0);
			line = (ArrayList<Tile>) method.invoke(dfm, coord, dir);
			assertEquals("The correct number of tiles was not output", 2, line.size());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void nextCoordTest() {
		try {

			Class<?> clazz = DetailedFindMove.class;
			// Object obj = clazz.newInstance();
			Class param[] = { int.class, Coordinate.class, int.class, boolean.class, char.class };
			Method method = dfm.getClass().getDeclaredMethod("nextCoord", param);
			method.setAccessible(true);

			// Parameter
			int lineLength = 3;
			Coordinate currentCoord = new Coordinate(2, 0);
			int color = 0;
			boolean isColorLine = true;
			char dir = 'r';

			currentCoord = (Coordinate) method.invoke(dfm, lineLength, currentCoord, color, isColorLine, dir);
			assertEquals(3, currentCoord.getX());

			lineLength = 4;
			currentCoord = (Coordinate) method.invoke(dfm, lineLength, currentCoord, color, isColorLine, dir);
			assertEquals(6, currentCoord.getX());

			lineLength = 7;
			currentCoord = (Coordinate) method.invoke(dfm, lineLength, currentCoord, color, isColorLine, dir);
			assertEquals(7, currentCoord.getX());

			lineLength = 8;
			currentCoord = (Coordinate) method.invoke(dfm, lineLength, currentCoord, color, isColorLine, dir);
			assertEquals(null, currentCoord);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void isDuplicateTest() {
		try {

			Class<?> clazz = DetailedFindMove.class;
			// Object obj = clazz.newInstance();
			Class param[] = { Tile.class, ArrayList.class };
			Method method = dfm.getClass().getDeclaredMethod("isDuplicate", param);
			method.setAccessible(true);

			// Parameter
			ArrayList<Tile> tiles = new ArrayList<Tile>();
			tiles.add(new Tile(1, 1, 1));
			tiles.add(new Tile(1, 4, 2));
			tiles.add(new Tile(5, 1, 3));

			/*
			 * tile already in list
			 */
			Tile tile = new Tile(1, 1, 2);
			assertEquals(true, (boolean) method.invoke(dfm, tile, tiles));

			/*
			 * new tile
			 */
			tile = new Tile(2, 4, 3);
			assertEquals(false, (boolean) method.invoke(dfm, tile, tiles));

			/*
			 * tile already in list
			 */
			tile = new Tile(5, 1, 5);
			assertEquals(true, (boolean) method.invoke(dfm, tile, tiles));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void findFreeCoordsTest() {
		try {

			Class<?> clazz = DetailedFindMove.class;
			Class param[] = { ArrayList.class, Coordinate[].class, Coordinate.class, int.class, boolean.class,
					char.class };
			Method method = dfm.getClass().getDeclaredMethod("findFreeCoords", param);
			method.setAccessible(true);

			// Parameter
			ArrayList<Tile> hand = new ArrayList<Tile>();
			Coordinate[] coords = new Coordinate[3];
			Coordinate coord = new Coordinate(2, 0);
			int color = 0;
			boolean isColorLine = true;
			char dir = 'r';

			/*
			 * Empty hand
			 */
			int line = (int) method.invoke(dfm, hand, coords, coord, color, isColorLine, dir);
			assertEquals(0, line);

			/*
			 * Hand with tiles
			 */
			hand.add(new Tile(0, 3, 60));
			hand.add(new Tile(0, 4, 70));
			hand.add(new Tile(0, 5, 80));
			hand.add(new Tile(0, 7, 40));
			line = (int) method.invoke(dfm, hand, coords, coord, color, isColorLine, dir);

			assertEquals(3, line);

			// Parameter
			hand.clear();
			coords = new Coordinate[3];
			coord = new Coordinate(2, 0);
			color = 0;
			isColorLine = true;
			dir = 'r';

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void moveLineTest() {
		try {

			Class<?> clazz = DetailedFindMove.class;
			Class param[] = { ArrayList.class, Coordinate.class, int.class, boolean.class, char.class };
			Method method = dfm.getClass().getDeclaredMethod("moveLine", param);
			method.setAccessible(true);

			// Parameter
			ArrayList<Tile> currentHand = new ArrayList<Tile>();
			currentHand.add(new Tile(0, 3, 21));
			currentHand.add(new Tile(0, 6, 22));
			currentHand.add(new Tile(0, 7, 23));
			Coordinate coord = new Coordinate(2, 0);
			int color = 0;
			boolean isColorLine = true;
			char dir = 'r';

			ArrayList<Tuple<Coordinate, Tile>> move = (ArrayList<Tuple<Coordinate, Tile>>) method.invoke(dfm,
					currentHand, coord, color, isColorLine, dir);
			assertEquals(3, move.size());

			// Parameter
			currentHand.clear();
			currentHand.add(new Tile(0, 3, 21));
			coord = new Coordinate(1, 10);
			color = 1;
			isColorLine = false;
			dir = 'r';

			move = (ArrayList<Tuple<Coordinate, Tile>>) method.invoke(dfm, currentHand, coord, color, isColorLine, dir);
			assertEquals(0, move.size());

			currentHand.add(new Tile(3, 1, 21));

			move = (ArrayList<Tuple<Coordinate, Tile>>) method.invoke(dfm, currentHand, coord, color, isColorLine, dir);
			assertEquals(1, move.size());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void moveBesideLineTest() {
		/*
		 * create new map
		 */

		Configuration config = new Configuration(8, 4, 6, 30, 5, WrongMove.NOTHING, 1, SlowMove.POINT_LOSS, 1, 2);
		MapLogic map = new MapLogic(config.getColorShapeCount());
		int maxColorShape = config.getColorShapeCount();

		/*
		 * Set color hand
		 */
		ArrayList<Tile>[] colorHand = new ArrayList[maxColorShape];
		colorHand[0] = new ArrayList<Tile>();
		colorHand[1] = new ArrayList<Tile>();
		colorHand[2] = new ArrayList<Tile>();
		colorHand[3] = new ArrayList<Tile>();
		colorHand[4] = new ArrayList<Tile>();
		colorHand[5] = new ArrayList<Tile>();
		colorHand[6] = new ArrayList<Tile>();
		colorHand[7] = new ArrayList<Tile>();
		colorHand[3].add(new Tile(3, 1, 21));
		colorHand[3].add(new Tile(3, 2, 22));
		colorHand[3].add(new Tile(3, 3, 23));

		colorHand[0].add(new Tile(0, 4, 21));

		ArrayList<Tile>[] shapeHand = new ArrayList[maxColorShape];
		shapeHand[0] = new ArrayList<Tile>();
		shapeHand[1] = new ArrayList<Tile>();
		shapeHand[2] = new ArrayList<Tile>();
		shapeHand[3] = new ArrayList<Tile>();
		shapeHand[4] = new ArrayList<Tile>();
		shapeHand[5] = new ArrayList<Tile>();
		shapeHand[6] = new ArrayList<Tile>();
		shapeHand[7] = new ArrayList<Tile>();
		shapeHand[1].add(new Tile(3, 1, 21));
		shapeHand[2].add(new Tile(3, 2, 22));
		shapeHand[3].add(new Tile(3, 3, 23));

		shapeHand[4].add(new Tile(0, 4, 21));

		ArrayList<Tile> hand = new ArrayList<Tile>();
		hand.addAll(colorHand[3]);
		hand.addAll(colorHand[0]);

		/*
		 * Set tiles on map
		 */
		ArrayList<Tuple<Coordinate, Tile>> addToMap = new ArrayList<Tuple<Coordinate, Tile>>();
		addToMap.add(new Tuple<Coordinate, Tile>(Coordinate.of(0, 0), new Tile(0, 0, 1)));
		addToMap.add(new Tuple<Coordinate, Tile>(Coordinate.of(1, 0), new Tile(0, 1, 2)));
		addToMap.add(new Tuple<Coordinate, Tile>(Coordinate.of(2, 0), new Tile(0, 2, 3)));
		addToMap.add(new Tuple<Coordinate, Tile>(Coordinate.of(3, 0), new Tile(0, 3, 4)));

		map.doMove(addToMap);

		dfm.updateData(map, colorHand, shapeHand, hand, maxColorShape);

		try {

			Class<?> clazz = DetailedFindMove.class;
			Class param[] = { Coordinate.class, Tile.class, char.class };
			Method method = dfm.getClass().getDeclaredMethod("moveBesideLine", param);
			method.setAccessible(true);

			// Parameter
			Coordinate coord = new Coordinate(1, 0);
			Tile tile = new Tile(0, 1, 2);
			char dir = 'd';

			ArrayList<Tuple<Coordinate, Tile>> move = (ArrayList<Tuple<Coordinate, Tile>>) method.invoke(dfm, coord,
					tile, dir);

			assertEquals(3, move.size());

			// Parameter
			coord = new Coordinate(0, 0);
			tile = new Tile(0, 0, 1);
			dir = 'r';

			move = (ArrayList<Tuple<Coordinate, Tile>>) method.invoke(dfm, coord, tile, dir);

			assertEquals(1, move.size());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
