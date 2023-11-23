package de.upb.swtpra1819interface.messages;

import java.util.Objects;


public class ResumeGame
  extends Message
{
  public static final int uniqueID = 404;
  
  public ResumeGame()
  {
    super(404);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ResumeGame)) {
      return false;
    }
    ResumeGame that = (ResumeGame)o;
    return Objects.equals(Integer.valueOf(getUniqueId()), Integer.valueOf(that.getUniqueId()));
  }
}
