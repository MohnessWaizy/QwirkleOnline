package de.upb.cs.swtpra_04.qwirkle.controller;

import de.upb.cs.swtpra_04.qwirkle.model.game.BoardTile;

/**
 * Control Interface to create communication between a boardFragment and a GameActivity
 */
public interface BoardFragmentInterface {
    /**
     * Called when board fragment should show a tile
     * @param boardTiles
     */
    public void showTile(BoardTile boardTiles);

    /**
     * Called to clear recently hovered tiles
     */
    public void clearTiles();

    /**
     * Called to play the hovered tiles
     */
    public void playTiles();
}
