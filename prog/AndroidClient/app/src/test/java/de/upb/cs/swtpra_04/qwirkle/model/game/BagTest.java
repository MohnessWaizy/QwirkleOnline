package de.upb.cs.swtpra_04.qwirkle.model.game;

import org.junit.Test;
import de.upb.cs.swtpra_04.qwirkle.view.BagFragment;

import static org.junit.Assert.*;

public class BagTest {

    private Bag bag;
    private BagFragment context;
    private int CSC = 1;
    private int tileCount = 2;


    @Test
    public void update() {
        bag = new Bag(context, CSC ,tileCount);
        bag.setContext(context);
        bag.setCSC(CSC);
        assertTrue(bag.getTotalInBag()==Math.pow(CSC,2));
    }
}