package de.upb.cs.swtpra_04.qwirkle.model.game;

import android.util.Log;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.upb.cs.swtpra_04.qwirkle.view.BoardFragment;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;

/**
 * Holds data for the board
 * <p>
 * Interface:   GameData.GameDataListener
 * Used to get notice of changes to the game data
 */
public class Board implements GameData.GameDataListener {

    private static final String TAG = "Board";

    private BoardFragment context;
    private List<BoardTile> tilesOnBoard = new ArrayList<>();
    private List<BoardTile> newOnBoard = new ArrayList<>(); // Tiles in here have a frame around them on the board
    private List<BoardTile> hoveringTiles = new ArrayList<>();
    private List<BoardTile> dummyTiles = new ArrayList<>();

    private boolean isLayingDummyTiles = false;
    private Table<Integer, Integer, Boolean> usedFields;

    private boolean paused;

    private GameData mGameData;

    private int curRowCnt = 1; // count of rows and columns initialized in the grid
    private int curColCnt = 1; // initially, there is only one cell

    private HashMap<Integer, Integer> playerTileMapping;


    int[] zeroZero = {0, 0}; // {x, y} of the first tile as reference, x: column ; y: row
    /*
        -----
        | | | <- y-coordinate
        -----
        | | | <-
        -----
         ^ ^
         | |
         x-coordinate
     */


    /**
     * Constructor
     *
     * @param context  parent Activity
     * @param gameData data of current game
     */
    public Board(BoardFragment context, GameData gameData) {
        this.context = context;
        this.mGameData = gameData;
        mGameData.listenToGameData(this);
        usedFields = HashBasedTable.create();
    }

    /**
     * Tries to add a single Tile to the board.
     * Throws IllegalArgumentException if the position of the tile is already blocked by a different tile.
     *
     * @param newTile Tile to be added to the board
     * @return true, if successful; false if tried to set tile outside of currently defined grid
     */
    private boolean addTile(BoardTile newTile) {
        // Tile already on the board, return true
        if (tilesOnBoard.contains(newTile)) return true;

        int posX = newTile.getX() + zeroZero[0]; // actual position on the board instead of
        int posY = -(newTile.getY()) + zeroZero[1]; // relative position to the first tile

        Log.d(TAG, "Row: " + curRowCnt + " Col: " + curColCnt + " posX: " + posX + " posY: " + posY
                + " zeroZero: (" + zeroZero[0] + "," + zeroZero[1] + ")");

        if (posY > curRowCnt - 1 || posX > curColCnt - 1 || posY < 0 || posX < 0) {
            return false; // trying to set a tile outside the grid
        }

        for (BoardTile t : tilesOnBoard) { // check if position is free for the new tile
            if (t.getAbsX() == posX && t.getAbsY() == posY) {
                throw new IllegalArgumentException("Tried to place new tile on already occupied position. x=" + t.getX() + " y=" + t.getY());
            }
        }

        // setting absolute values for the tile
        newTile.setAbsX(posX);
        newTile.setAbsY(posY);

        // adding view to the grid (uses absolute coordinates of the tile)
        context.addTileToGrid(newTile);

        // as tile is new, it will have a frame around it now. To remove it later:
        newOnBoard.add(newTile);

        // adding tile to the tile list
        tilesOnBoard.add(newTile);


        // adding a new row or column if new tile is set to the edge of the grid
        if (!isLayingDummyTiles) {
            if (posX == curColCnt - 1) addColumnRight();
            if (posY == curRowCnt - 1) addRowBottom();
            if (posX == 0) addColumnLeft();
            if (posY == 0) addRowTop();
        }

        return true;
    }

    /**
     * Updates the Mapping for the Player-Tile Mapping on Update Response
     *
     * @param tiles Liste of last layed tiles
     * @param id    Player Id of the current player
     */
    private void updateMapping(List<BoardTile> tiles, int id) {
        for (BoardTile tile : tiles) {
            this.playerTileMapping.put(tile.getId(), id);
        }
    }

    /**
     * Mapps the layed tiles to the given PlayerId and marks them on the field
     *
     * @param id Id of the player which tiles should be marked
     */
    private void mapPlayerTiles(int id) {
        if (!tilesOnBoard.isEmpty() && !playerTileMapping.isEmpty()) {
            for (BoardTile tile : tilesOnBoard) {
                if (this.playerTileMapping.get(tile.getId()) == id) {
                    tile.markTile(true);
                } else {
                    tile.markTile(false);
                }
            }
        }
    }


    /**
     * The function lays out Dummy tiles that can be clicked to add a tile to the Field.
     */
    public void layoutDummyTiles() {
        isLayingDummyTiles = true;
        //If no tile is on Board place dummy tile on 0,0
        if (tilesOnBoard.isEmpty()) {
            BoardTile dummyTile = new BoardTile(new TileOnPosition(0, 0, new Tile(0, 12, 0)), true);
            dummyTile.setAbsX(dummyTile.getX() + zeroZero[0]);
            dummyTile.setAbsY(-(dummyTile.getY()) + zeroZero[1]);
            context.layoutDummyTile(dummyTile);
            dummyTiles.add(dummyTile);
        } else {

            for (BoardTile tile : tilesOnBoard) {
                if (usedFields.get(tile.getX() - 1, tile.getY()) == null || !usedFields.get(tile.getX() - 1, tile.getY())) {
                    BoardTile dummyTile = new BoardTile(new TileOnPosition(tile.getX() - 1, tile.getY(), new Tile(0, 12, 0)), true);
                    dummyTile.setAbsX(dummyTile.getX() + zeroZero[0]);
                    dummyTile.setAbsY(-(dummyTile.getY()) + zeroZero[1]);
                    context.layoutDummyTile(dummyTile);
                    dummyTiles.add(dummyTile);
                }
                if (usedFields.get(tile.getX() + 1, tile.getY()) == null || !usedFields.get(tile.getX() + 1, tile.getY())) {
                    BoardTile dummyTile = new BoardTile(new TileOnPosition(tile.getX() + 1, tile.getY(), new Tile(0, 12, 0)), true);
                    dummyTile.setAbsX(dummyTile.getX() + zeroZero[0]);
                    dummyTile.setAbsY(-(dummyTile.getY()) + zeroZero[1]);
                    context.layoutDummyTile(dummyTile);
                    dummyTiles.add(dummyTile);
                }
                if (usedFields.get(tile.getX(), tile.getY() - 1) == null || !usedFields.get(tile.getX(), tile.getY() - 1)) {
                    BoardTile dummyTile = new BoardTile(new TileOnPosition(tile.getX(), tile.getY() - 1, new Tile(0, 12, 0)), true);
                    dummyTile.setAbsX(dummyTile.getX() + zeroZero[0]);
                    dummyTile.setAbsY(-(dummyTile.getY()) + zeroZero[1]);
                    context.layoutDummyTile(dummyTile);
                    dummyTiles.add(dummyTile);
                }
                if (usedFields.get(tile.getX(), tile.getY() + 1) == null || !usedFields.get(tile.getX(), tile.getY() + 1)) {
                    BoardTile dummyTile = new BoardTile(new TileOnPosition(tile.getX(), tile.getY() + 1, new Tile(0, 12, 0)), true);
                    dummyTile.setAbsX(dummyTile.getX() + zeroZero[0]);
                    dummyTile.setAbsY(-(dummyTile.getY()) + zeroZero[1]);
                    context.layoutDummyTile(dummyTile);
                    dummyTiles.add(dummyTile);
                }
            }
        }
    }

    /**
     * Removes all dummy tiles on the field
     */
    public void removeDummyTiles() {
        for (BoardTile tile : dummyTiles) {
            context.removeTileFromGrid(tile);
        }
        dummyTiles.clear();
        isLayingDummyTiles = false;
    }


    /**
     * Adds a hover tile to the field.
     * It is just temporary on the Field until the server accepts the move of the Hovered Tiles
     *
     * @param tile The tile that should be placed
     */
    public void addHoverTile(BoardTile tile) {
        tile.setNewTile(true);
        hoveringTiles.add(tile);
        usedFields.put(tile.getX(), tile.getY(), true);
        addTile(tile);
    }


    /**
     * Make the hovering tiles disappear so the Update from the server can lay tiles
     */
    public void playMove() {
        clearMove();
    }


    /**
     * Clears all tiles that a hovering at the Moment
     */
    public void clearMove() {
        removeTiles(hoveringTiles);
        for (BoardTile tile : hoveringTiles) {
            usedFields.put(tile.getX(), tile.getY(), false);
        }
        hoveringTiles.clear();

    }


    /**
     * Tries to add multiple Tiles to the board
     * Throws IllegalArgumentException if the position of the tile is already blocked by a different tile.
     *
     * @param list List of Tiles to be added to the board
     */
    public void addTiles(List<BoardTile> list) {
        for (BoardTile tile : list) {
            usedFields.put(tile.getX(), tile.getY(), true);
        }

        // delete frames around current new tiles, they will be old now
        removeNewTiles();

        int loop = list.size(); // Stops this from going on forever
        List<BoardTile> listCopy = new ArrayList<BoardTile>(list);

        while (listCopy.size() > 0 && loop >= 0) {
            loop--;
            Iterator<BoardTile> iterator = listCopy.iterator();
            while (iterator.hasNext()) {
                BoardTile next;
                if (addTile((next = iterator.next()))) {
                    iterator.remove();
                }
            }
        }

        if (loop < 0) {
            Log.e(TAG, "NOT ALL TILES COULD BE PLACED");
        }
    }

    /**
     * Removes the new Tile Border
     */
    private void removeNewTiles() {
        for (BoardTile t : newOnBoard) {
            t.setNewTile(false);
        }
        newOnBoard.clear();
    }

    /**
     * removes a single Tile from the board
     *
     * @param tile Tile to be removed
     */
    private void removeTile(BoardTile tile) {
        context.removeTileFromGrid(tile);
        tilesOnBoard.remove(tile);
    }


    /**
     * removes multiple Tiles from the board
     *
     * @param list List of Tiles to be removed
     */
    public void removeTiles(List<BoardTile> list) {
        for (BoardTile t : list) {
            removeTile(t);
        }
    }

    /**
     * Extending grid by adding rows and columns
     */
    private void addRowTop() {
        context.addRowTop(tilesOnBoard);
        zeroZero[1]++;
        curRowCnt++;
    }

    /**
     * Extending grid by adding rows and columns and when necessary moving the views on the grid
     */
    private void addRowBottom() {
        context.addRowBottom();
        curRowCnt++;
    }

    /**
     * Extending grid by adding rows and columns and when necessary moving the views on the grid
     */
    private void addColumnRight() {
        context.addColumnRight();
        curColCnt++;
    }

    /**
     * Extending grid by adding rows and columns and when necessary moving the views on the grid
     */
    private void addColumnLeft() {
        context.addColumnLeft(tilesOnBoard);
        curColCnt++;
        zeroZero[0]++;
    }

    /**
     *
     * @return tilesOnBoard
     */
    public List<BoardTile> getTilesOnBoard() {
        return this.tilesOnBoard;
    }

    /**
     *
     * @param tiles
     */
    public void setTilesOnBoard(List<BoardTile> tiles) {
        this.tilesOnBoard = tiles;
    }

    /**
     * Reacts to updates to the game data
     */
    public void onGameDataUpdate() {
    }

    /**
     * Gets the Hovering tiles as a List of TileOnPosition for the Playtiles request
     *
     * @return List<TileOnPosition> The tiles which the player placed on the Board
     */
    public List<TileOnPosition> getHoveringTiles() {
        List<TileOnPosition> tiles = new ArrayList<TileOnPosition>();
        for (BoardTile tile : hoveringTiles) {
            tiles.add(tile.toTileOnPosition());
        }
        return tiles;
    }

    /**
     *
     * @return tiles
     */
    public List<de.upb.cs.swtpra_04.qwirkle.model.game.Tile> getHoveringTilesAsTile() {
        List<de.upb.cs.swtpra_04.qwirkle.model.game.Tile> tiles = new ArrayList<de.upb.cs.swtpra_04.qwirkle.model.game.Tile>();
        for (BoardTile tile : hoveringTiles) {
            tiles.add(tile.toTile());
        }
        return tiles;
    }

    /**
     *
     * @return dummyTiles
     */
    public List<BoardTile> getDummyTiles() {
        return this.dummyTiles;
    }
}
