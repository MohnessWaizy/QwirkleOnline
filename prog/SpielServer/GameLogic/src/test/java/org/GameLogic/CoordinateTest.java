package org.GameLogic;

import org.GameLogic.Board.Coordinate;
import org.junit.Test;

import junit.framework.TestCase;

public class CoordinateTest extends TestCase {

	@Test
	public void testLeftRightUpDown() {
		Coordinate base = Coordinate.of(0, 0);
		assertEquals("left", base.left(), Coordinate.of(-1, 0));
		assertEquals("right", base.right(), Coordinate.of(1, 0));
		assertEquals("down", base.down(), Coordinate.of(0, -1));
		assertEquals("up", base.up(), Coordinate.of(0, 1));
	}

}
