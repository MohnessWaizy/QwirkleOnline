package de.upb.cs.swtpra_04.qwirkle.model.game;

import java.util.ArrayList;
import java.util.List;

import de.upb.cs.swtpra_04.qwirkle.view.BagFragment;
import de.upb.swtpra1819interface.models.Tile;

/**
 * Holds the data for the bag
 */
public class Bag {

    private BagFragment context;

    private int CSC; // colorShapeCount

    private BagTile[][] tiles;
    private int totalInBag;

    /**
     * Constructor
     *
     * @param context   parent context which displays this data
     * @param CSC       count of different colors and shapes
     * @param tileCount number of identical copies of each combination of color and shape
     */
    public Bag(BagFragment context, int CSC, int tileCount) {
        this.context = context;
        this.CSC = CSC;

        tiles = new BagTile[CSC][CSC];

        // create list with all possible tiles for the game
        // i % CSC = color, j / CSC = shape
        for (int i = 0; i < Math.pow(CSC, 2); i++) {
            int shape = i / CSC;
            int color = i % CSC;
            tiles[shape][color] = new BagTile(shape, color, tileCount);
        }

        totalInBag = (int) Math.pow(CSC, 2);
    }

    /**
     * Updates the count of identical tiles in the bag
     *
     * @param newTileList List of all tiles currently in the bag
     */
    public void update(ArrayList<Tile> newTileList) {
        for (BagTile[] inner : tiles) {
            for (BagTile bagtile : inner) {
                bagtile.setCountInBag(0);
            }
        }

        for (Tile inttile : newTileList) {
            int shape = inttile.getShape();
            int color = inttile.getColor();
            int oldCount = tiles[shape][color].getCountInBag();

            tiles[shape][color].setCountInBag(oldCount + 1);
        }
        totalInBag = newTileList.size();
    }

    /**
     *
     * @param shape
     * @param color
     * @param newCount
     */
    public void setCountOfTile(int shape, int color, int newCount) {
        BagTile bt = tiles[shape][color];
        bt.setCountInBag(newCount);
    }

    /**
     * @return List representation of bag tiles
     */
    public List<BagTile> getTiles() {
        List<BagTile> res = new ArrayList<>();
        for (BagTile[] inner : tiles) {
            for (BagTile bagtile : inner) {
                res.add(bagtile);
            }
        }
        return res;
    }

    /**
     *
     * @return CSC
     */
    public int getCSC() {
        return CSC;
    }

    /**
     *
     * @param CSC
     */
    public void setCSC(int CSC) {
        this.CSC = CSC;
    }

    /**
     *
     * @return context
     */
    public BagFragment getContext() {
        return context;
    }

    /**
     *
     * @param context
     */
    public void setContext(BagFragment context) {
        this.context = context;
    }

    /**
     *
     * @return totalInBag
     */
    public int getTotalInBag() {
        return totalInBag;
    }

    /**
     *
     * @param totalInBag
     */
    public void setTotalInBag(int totalInBag) {
        this.totalInBag = totalInBag;
    }
}
