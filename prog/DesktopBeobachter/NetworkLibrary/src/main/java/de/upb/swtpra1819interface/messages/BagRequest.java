package de.upb.swtpra1819interface.messages;

import java.util.Objects;


public class BagRequest
  extends Message
{
  public static final int uniqueID = 423;
  
  public BagRequest()
  {
    super(423);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BagRequest)) {
      return false;
    }
    BagRequest that = (BagRequest)o;
    return Objects.equals(Integer.valueOf(getUniqueId()), Integer.valueOf(that.getUniqueId()));
  }
}
