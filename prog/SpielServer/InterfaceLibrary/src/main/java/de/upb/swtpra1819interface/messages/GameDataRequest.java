package de.upb.swtpra1819interface.messages;

import java.util.Objects;


public class GameDataRequest
  extends Message
{
  public static final int uniqueID = 498;
  
  public GameDataRequest()
  {
    super(498);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof GameDataRequest)) {
      return false;
    }
    GameDataRequest that = (GameDataRequest)o;
    return Objects.equals(Integer.valueOf(getUniqueId()), Integer.valueOf(that.getUniqueId()));
  }
}
