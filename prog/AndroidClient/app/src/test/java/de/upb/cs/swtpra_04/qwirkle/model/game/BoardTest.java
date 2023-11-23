package de.upb.cs.swtpra_04.qwirkle.model.game;

import org.junit.Test;

import java.util.ArrayList;

import de.upb.cs.swtpra_04.qwirkle.view.BoardFragment;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;

import static org.junit.Assert.*;

public class BoardTest {

    BoardFragment context = new BoardFragment();
    GameData gameData = new GameData(GameState.NOT_STARTED,false);
    Board board = new Board(context, gameData);


    @Test
    public void layoutDummyTiles() {
        board.layoutDummyTiles();

        assertTrue("If no tile layed make dummy Tile on 0,0", board.getDummyTiles().get(0).getX()==0 && board.getDummyTiles().get(0).getY()==0);

        board.removeDummyTiles();

        ArrayList<BoardTile> tiles = new ArrayList<BoardTile>();
        tiles.add(new BoardTile(new TileOnPosition(0, 0, new Tile(0, 0, 0)), false));
        board.setTilesOnBoard(tiles);
        board.layoutDummyTiles();

        assertTrue("Layout left", board.getDummyTiles().get(0).getX() == -1 && board.getDummyTiles().get(0).getY() == 0);
        assertTrue("Layout right", board.getDummyTiles().get(1).getX() == 1 && board.getDummyTiles().get(1).getY() == 0);
        assertTrue("Layout up", board.getDummyTiles().get(2).getX() == 0 && board.getDummyTiles().get(2).getY() == -1);
        assertTrue("Layout left", board.getDummyTiles().get(3).getX() == 0 && board.getDummyTiles().get(3).getY() == 1);
    }

    @Test
    public void removeDummyTiles() {
        board.layoutDummyTiles();

        board.removeDummyTiles();

        assertTrue(board.getDummyTiles().isEmpty());
    }

    @Test
    public void addHoverTile() {
    }

    @Test
    public void playMove() {
    }

    @Test
    public void clearMove() {
    }

    @Test
    public void addTiles() {
    }

    @Test
    public void removeTiles() {
    }

    @Test
    public void getTilesOnBoard() {
    }

    @Test
    public void onGameDataUpdate() {
    }

    @Test
    public void getHoveringTiles() {
    }

    @Test
    public void getHoveringTilesAsTile() {
    }
}