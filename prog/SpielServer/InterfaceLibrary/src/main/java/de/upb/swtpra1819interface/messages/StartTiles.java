package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Tile;
import java.util.Collection;
import java.util.Objects;







public class StartTiles
  extends Message
{
  public static final int uniqueID = 408;
  private Collection<Tile> tiles;
  
  public StartTiles(Collection<Tile> tiles)
  {
    super(408);
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
    if (!(o instanceof StartTiles)) {
      return false;
    }
    StartTiles that = (StartTiles)o;
    return Objects.equals(getTiles(), that.getTiles());
  }
}
