package de.upb.swtpra1819interface.messages;

import java.util.Objects;








public class MoveValid
  extends Message
{
  public static final int uniqueID = 415;
  private boolean validation;
  private String message;
  
  public MoveValid(boolean validation, String message)
  {
    super(415);
    this.validation = validation;
    this.message = message;
  }
  
  public boolean isValidation() {
    return validation;
  }
  
  public String getMessage() {
    return message;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MoveValid)) {
      return false;
    }
    MoveValid moveValid = (MoveValid)o;
    return (isValidation() == moveValid.isValidation()) && 
      (Objects.equals(getMessage(), moveValid.getMessage()));
  }
}
