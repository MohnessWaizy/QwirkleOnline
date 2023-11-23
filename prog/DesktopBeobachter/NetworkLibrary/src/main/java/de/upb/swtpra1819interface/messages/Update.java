package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.TileOnPosition;
import java.util.Collection;
import java.util.Objects;








public class Update
  extends Message
{
  public static final int uniqueID = 416;
  private Collection<TileOnPosition> updates;
  private int numberTilesInBag;
  
  public Update(Collection<TileOnPosition> updates, int numberTilesInBag)
  {
    super(416);
    this.updates = updates;
    this.numberTilesInBag = numberTilesInBag;
  }
  
  public Collection<TileOnPosition> getUpdates() {
    return updates;
  }
  
  public int getNumberTilesInBag() {
    return numberTilesInBag;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Update)) {
      return false;
    }
    Update update = (Update)o;
    return (getNumberTilesInBag() == update.getNumberTilesInBag()) && 
      (Objects.equals(getUpdates(), update.getUpdates()));
  }
}
