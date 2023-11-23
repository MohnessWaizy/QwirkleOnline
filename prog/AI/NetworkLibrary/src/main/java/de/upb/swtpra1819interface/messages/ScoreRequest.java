package de.upb.swtpra1819interface.messages;

import java.util.Objects;


public class ScoreRequest
  extends Message
{
  public static final int uniqueID = 417;
  
  public ScoreRequest()
  {
    super(417);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ScoreRequest)) {
      return false;
    }
    ScoreRequest that = (ScoreRequest)o;
    return Objects.equals(Integer.valueOf(getUniqueId()), Integer.valueOf(that.getUniqueId()));
  }
}
