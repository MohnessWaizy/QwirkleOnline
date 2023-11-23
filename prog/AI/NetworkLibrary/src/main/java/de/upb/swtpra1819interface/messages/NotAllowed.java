package de.upb.swtpra1819interface.messages;

import java.util.Objects;


public class NotAllowed
  extends Error
{
  public static final int uniqueID = 920;
  
  public NotAllowed(String message, int causeId)
  {
    super(920, message, causeId);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NotAllowed)) {
      return false;
    }
    NotAllowed that = (NotAllowed)o;
    return (Objects.equals(Integer.valueOf(getUniqueId()), Integer.valueOf(that.getUniqueId()))) && 
      (Objects.equals(Integer.valueOf(getCauseId()), Integer.valueOf(that.getCauseId())));
  }
}
