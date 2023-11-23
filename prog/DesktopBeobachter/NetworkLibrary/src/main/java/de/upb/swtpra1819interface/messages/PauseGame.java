package de.upb.swtpra1819interface.messages;

import java.util.Objects;


public class PauseGame
  extends Message
{
  public static final int uniqueID = 403;
  
  public PauseGame()
  {
    super(403);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PauseGame)) {
      return false;
    }
    PauseGame that = (PauseGame)o;
    return Objects.equals(Integer.valueOf(getUniqueId()), Integer.valueOf(that.getUniqueId()));
  }
}
