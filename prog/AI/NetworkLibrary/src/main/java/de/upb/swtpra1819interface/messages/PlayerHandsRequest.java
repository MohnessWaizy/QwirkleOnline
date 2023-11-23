package de.upb.swtpra1819interface.messages;

import java.util.Objects;


public class PlayerHandsRequest
  extends Message
{
  public static final int uniqueID = 425;
  
  public PlayerHandsRequest()
  {
    super(425);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PlayerHandsRequest)) {
      return false;
    }
    PlayerHandsRequest that = (PlayerHandsRequest)o;
    return Objects.equals(Integer.valueOf(getUniqueId()), Integer.valueOf(that.getUniqueId()));
  }
}
