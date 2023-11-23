package de.upb.swtpra1819interface.messages;

import java.util.Objects;


public class AbortGame
  extends Message
{
  public static final int uniqueID = 402;
  
  public AbortGame()
  {
    super(402);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AbortGame)) {
      return false;
    }
    AbortGame that = (AbortGame)o;
    return Objects.equals(Integer.valueOf(getUniqueId()), Integer.valueOf(that.getUniqueId()));
  }
}
