package de.upb.swtpra1819interface.messages;

import java.util.Objects;


public class ParsingError
  extends Error
{
  public static final int uniqueID = 910;
  
  public ParsingError(String message, int causeId)
  {
    super(910, message, causeId);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ParsingError)) {
      return false;
    }
    ParsingError that = (ParsingError)o;
    return (Objects.equals(Integer.valueOf(getUniqueId()), Integer.valueOf(that.getUniqueId()))) && 
      (Objects.equals(Integer.valueOf(getCauseId()), Integer.valueOf(that.getCauseId())));
  }
}
