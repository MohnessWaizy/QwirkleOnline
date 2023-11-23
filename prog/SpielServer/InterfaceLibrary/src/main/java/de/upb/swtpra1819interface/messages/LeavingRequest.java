package de.upb.swtpra1819interface.messages;

import java.util.Objects;


public class LeavingRequest
  extends Message
{
  public static final int uniqueID = 405;
  
  public LeavingRequest()
  {
    super(405);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LeavingRequest)) {
      return false;
    }
    LeavingRequest that = (LeavingRequest)o;
    return Objects.equals(Integer.valueOf(getUniqueId()), Integer.valueOf(that.getUniqueId()));
  }
}
