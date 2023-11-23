package de.upb.swtpra1819interface.messages;

import java.util.Objects;


public class TurnTimeLeftRequest
  extends Message
{
  public static final int uniqueID = 419;
  
  public TurnTimeLeftRequest()
  {
    super(419);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TurnTimeLeftRequest)) {
      return false;
    }
    TurnTimeLeftRequest that = (TurnTimeLeftRequest)o;
    return Objects.equals(Integer.valueOf(getUniqueId()), Integer.valueOf(that.getUniqueId()));
  }
}
