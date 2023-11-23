package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Tile;
import java.util.Collection;
import java.util.Objects;







public class BagResponse
  extends Message
{
  public static final int uniqueID = 424;
  private Collection<Tile> bag;
  
  public BagResponse(Collection<Tile> bag)
  {
    super(424);
    this.bag = bag;
  }
  
  public Collection<Tile> getBag() {
    return bag;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BagResponse)) {
      return false;
    }
    BagResponse that = (BagResponse)o;
    return Objects.equals(getBag(), that.getBag());
  }
}
