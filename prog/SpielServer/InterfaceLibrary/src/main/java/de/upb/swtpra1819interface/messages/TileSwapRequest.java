package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Tile;
import java.util.Collection;
import java.util.Objects;







public class TileSwapRequest
  extends Message
{
  public static final int uniqueID = 411;
  private Collection<Tile> tiles;
  
  public TileSwapRequest(Collection<Tile> tiles)
  {
    super(411);
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
    if (!(o instanceof TileSwapRequest)) {
      return false;
    }
    TileSwapRequest that = (TileSwapRequest)o;
    return Objects.equals(getTiles(), that.getTiles());
  }
}
