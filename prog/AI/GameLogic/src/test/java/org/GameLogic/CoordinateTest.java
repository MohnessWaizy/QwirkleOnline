package org.GameLogic;

import static org.junit.Assert.*;

import org.GameLogic.Board.Coordinate;
import org.junit.Test;

import de.upb.swtpra1819interface.models.Tile;

public class CoordinateTest {

	@Test
	public void equalsTest() {
		Coordinate coord = new Coordinate(1,1);
		Coordinate secondCoord = new Coordinate(1,1);
		Coordinate thirdCoord = new Coordinate(1,2);
		Tile tile = new Tile(1,1,1);
		
		assertEquals(true, coord.equals(coord));
		assertEquals(true, coord.equals(secondCoord));
		assertEquals(false, coord.equals(thirdCoord));
		assertEquals(false, coord.equals(null));
		assertEquals(false, coord.equals(tile));
	}
	
	@Test
	public void toStringTest() {
		Coordinate coord = new Coordinate(1,1);
		
		assertEquals("Coordinate [xCoord=1, yCoord=1]", coord.toString());
	}

}
