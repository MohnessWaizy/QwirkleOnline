package de.upb.cs.swtpra_04.qwirkle.model.game;

/**
 * Holds data for a tile shown in a cell in the bag
 */
public class BagTile extends Tile {

    private int countInBag;

    /**
     * Constructor
     *
     * @param shape Shape of the Tile
     * @param color Color of the Tile
     */
    public BagTile(int shape, int color) {
        super(shape, color);
        this.countInBag = 0;
    }

    /**
     * Constructor
     *
     * @param shape Shape of the Tile
     * @param color Color of the Tile
     * @param nb    Count of identical Tiles in teh bag
     */
    public BagTile(int shape, int color, int nb) {
        super(shape, color);
        this.countInBag = nb;
    }

    /**
     *
     * @return countInBag
     */
    public int getCountInBag() {
        return countInBag;
    }

    /**
     *
     * @param number
     */
    public void setCountInBag(int number) {
        if (number >= 0) {
            this.countInBag = number;
        } else {
            this.countInBag = 0;
        }
    }

}

