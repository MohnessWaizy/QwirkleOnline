package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Tile;
import java.util.Collection;
import java.util.Objects;







public class SendTiles
  extends Message
{
  public static final int uniqueID = 410;
  private Collection<Tile> tiles;
  
  public SendTiles(Collection<Tile> tiles)
  {
    super(410);
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
    if (!(o instanceof SendTiles)) {
      return false;
    }
    SendTiles sendTiles = (SendTiles)o;
    return Objects.equals(getTiles(), sendTiles.getTiles());
  }
}
