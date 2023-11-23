package de.upb.cs.swtpra_04.qwirkle.controller;

import java.util.ArrayList;
import java.util.HashMap;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Tile;

/**
 * controller for observer client
 */
public interface GameControllerSpectator extends GameController {
    /**
     *
     * @param hands
     */
    public void updatePlayerHands(HashMap<Client, ArrayList<Tile>> hands);

    /**
     *
     * @param bag
     */
    public void updateBag(ArrayList<Tile> bag);

}
