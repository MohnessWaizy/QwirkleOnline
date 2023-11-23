package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.TileOnPosition;
import java.util.Collection;
import java.util.Objects;







public class PlayTiles
  extends Message
{
  public static final int uniqueID = 414;
  private Collection<TileOnPosition> tiles;
  
  public PlayTiles(Collection<TileOnPosition> tiles)
  {
    super(414);
    this.tiles = tiles;
  }
  
  public Collection<TileOnPosition> getTiles() {
    return tiles;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PlayTiles)) {
      return false;
    }
    PlayTiles playTiles = (PlayTiles)o;
    return Objects.equals(getTiles(), playTiles.getTiles());
  }
}
