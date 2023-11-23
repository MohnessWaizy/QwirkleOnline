package de.upb.cs.swtpra_04.qwirkle.model.game;

import org.junit.Test;

import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;

import static org.junit.Assert.*;

public class BoardTileTest {

    TileOnPosition tile;
    boolean isDummyTile =  false;

    @Test
    public void toTileOnPosition() {
        TileOnPosition expected = tile;

        tile = new TileOnPosition(1,3,new Tile(1,2,3));
        BoardTile bt = new BoardTile(tile, isDummyTile);

        assertTrue(bt.toTileOnPosition().equals(tile));
    }

    @Test
    public void toTile(){
        TileOnPosition expected = tile;

        tile = new TileOnPosition(1,3,new Tile(1,2,3));
        BoardTile bt = new BoardTile(tile, isDummyTile);


        assertTrue(bt.toTile().getId() == 3);
    }

}