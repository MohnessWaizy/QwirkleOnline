package de.upb.swtpra1819interface.messages;

import java.util.Objects;


public class TotalTimeRequest
  extends Message
{
  public static final int uniqueID = 421;
  
  public TotalTimeRequest()
  {
    super(421);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TotalTimeRequest)) {
      return false;
    }
    TotalTimeRequest that = (TotalTimeRequest)o;
    return Objects.equals(Integer.valueOf(getUniqueId()), Integer.valueOf(that.getUniqueId()));
  }
}
