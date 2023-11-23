package de.upb.swtpra1819interface.messages;

import java.util.Objects;


public class GameListRequest
  extends Message
{
  public static final int uniqueID = 300;
  
  public GameListRequest()
  {
    super(300);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof GameListRequest)) {
      return false;
    }
    GameListRequest that = (GameListRequest)o;
    return Objects.equals(Integer.valueOf(getUniqueId()), Integer.valueOf(that.getUniqueId()));
  }
}
