package de.upb.cs.swtpra_04.qwirkle.model.game;

import de.upb.cs.swtpra_04.qwirkle.R;

/**
 * Holds a single field which contains every vector layout for the tiles
 */
public class TileIds {
    public static int[] IDS = // IDS [shape (0-11) 12(DummyTile) ]
            {
                    R.drawable.tile_1, R.drawable.tile_2, R.drawable.tile_3, R.drawable.tile_4,
                    R.drawable.tile_5, R.drawable.tile_6, R.drawable.tile_7, R.drawable.tile_8,
                    R.drawable.tile_9, R.drawable.tile_10, R.drawable.tile_11, R.drawable.tile_12,
                    R.drawable.add_tile
            };

    public static int[] COLORS =    // COLORS [color (0-11) ]
            {
                    R.color.Tile01, R.color.Tile02, R.color.Tile03, R.color.Tile04,
                    R.color.Tile05, R.color.Tile06, R.color.Tile07, R.color.Tile08,
                    R.color.Tile09, R.color.Tile10, R.color.Tile11, R.color.Tile12,
            };
}
