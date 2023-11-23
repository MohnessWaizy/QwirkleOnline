package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Tile;
import java.util.Collection;
import java.util.Objects;







public class TileSwapResponse
  extends Message
{
  public static final int uniqueID = 413;
  private Collection<Tile> tiles;
  
  public TileSwapResponse(Collection<Tile> tiles)
  {
    super(413);
    this.tiles = tiles;
  }
  
  public Collection<Tile> getTiles() {
    return tiles;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TileSwapResponse)) {
      return false;
    }
    TileSwapResponse that = (TileSwapResponse)o;
    return Objects.equals(getTiles(), that.getTiles());
  }
}
