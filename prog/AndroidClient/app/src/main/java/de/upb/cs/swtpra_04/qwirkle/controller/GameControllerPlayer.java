package de.upb.cs.swtpra_04.qwirkle.controller;

import java.util.Collection;

import de.upb.swtpra1819interface.models.Tile;

/**
 * controller for player client
 */
public interface GameControllerPlayer extends GameController {
    /**
     * sets tiles that are given for a player at the beginning of game
     *
     * @param tiles tiles that the player start with in the hand
     */
    public void startTiles(Collection<Tile> tiles);

    /**
     * adds new tile in hand of player
     *
     * @param tiles new tiles
     */
    public void addTilesToHand(Collection<Tile> tiles);

    /**
     * checks whether it is valid to swap tiles
     *
     * @param isValid boolean possible to swap
     */
    public void tileSwapValid(boolean isValid);

    /**
     * check if the given move is valid
     *
     * @param isValid boolean possible to make the move
     */
    public void moveValid(boolean isValid);

}
