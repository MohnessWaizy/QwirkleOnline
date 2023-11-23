package de.upb.swtpra1819interface.messages;

import java.util.Objects;


public class AccessDenied
  extends Error
{
  public static final int uniqueID = 900;
  
  public AccessDenied(String message, int causeId)
  {
    super(900, message, causeId);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AccessDenied)) {
      return false;
    }
    AccessDenied that = (AccessDenied)o;
    return (Objects.equals(Integer.valueOf(getUniqueId()), Integer.valueOf(that.getUniqueId()))) && 
      (Objects.equals(Integer.valueOf(getCauseId()), Integer.valueOf(that.getCauseId())));
  }
}
