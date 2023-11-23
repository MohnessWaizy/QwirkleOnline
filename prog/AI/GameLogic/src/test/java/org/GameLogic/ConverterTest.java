package org.GameLogic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.GameLogic.Board.Coordinate;
import org.GameLogic.Board.Tuple;
import org.GameLogic.Util.Converter;
import org.junit.Test;

import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;

public class ConverterTest {

	@Test
	public void toNetworkTileOnPositionTest() {
		Converter conv = new Converter();
		
		ArrayList<Tuple<Coordinate, Tile>> tuple = new ArrayList<Tuple<Coordinate, Tile>>();
		tuple.add(new Tuple<Coordinate, Tile>(new Coordinate(1,1), new Tile(1,1,1)));
		tuple.add(new Tuple<Coordinate, Tile>(new Coordinate(1,2), new Tile(2,1,2)));
		
		List<TileOnPosition> tilesOnPosition = conv.toNetworkTileOnPosition(tuple);
		
		assertEquals(2,tilesOnPosition.size());
		assertEquals(1,tilesOnPosition.get(0).getCoordX());
		assertEquals(2,tilesOnPosition.get(1).getCoordY());
	}
	
	@Test
	public void toGameCoordTileTest() {
		Converter conv = new Converter();
		
		List<TileOnPosition> tilesOnPosition = new ArrayList<TileOnPosition>();
		tilesOnPosition.add(new TileOnPosition(1,1,new Tile(1,1,1)));
		tilesOnPosition.add(new TileOnPosition(1,2,new Tile(2,1,2)));
		
		ArrayList<Tuple<Coordinate, Tile>> tuple = conv.toGameCoordTile(tilesOnPosition);
		
		assertEquals(2,tuple.size());
		assertEquals(1,tuple.get(0).getFirst().getX());
		assertEquals(2,tuple.get(1).getFirst().getY());
	}

}
