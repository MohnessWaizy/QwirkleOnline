package de.upb.cs.swtpra_04.qwirkle.model.game;

import org.junit.Test;

import static org.junit.Assert.*;

public class BagTileTest {

    private int countInBag;
    private int shape;
    private BagTile bagTile;

    @Test
    public void setCountInBagPositiv() throws Exception {
            int shape = 4;
            int color = 4;
            int expected = 3;
            BagTile bagTile = new BagTile(shape, color);
            bagTile.setCountInBag(3);
            assertTrue(bagTile.getCountInBag() == expected);
    }
    @Test
    public void setCountInBagNegativ() throws Exception {
        int shape = 4;
        int color = 4;
        int expected = 0;
        BagTile bagTile = new BagTile(shape, color);
        bagTile.setCountInBag(-1);
        assertTrue(bagTile.getCountInBag() == expected);
    }
}