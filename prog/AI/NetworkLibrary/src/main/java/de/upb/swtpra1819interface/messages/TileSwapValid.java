package de.upb.swtpra1819interface.messages;



public class TileSwapValid
  extends Message
{
  public static final int uniqueID = 412;
  

  private boolean validation;
  


  public TileSwapValid(boolean validation)
  {
    super(412);
    this.validation = validation;
  }
  
  public boolean isValidation() {
    return validation;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TileSwapValid)) {
      return false;
    }
    TileSwapValid that = (TileSwapValid)o;
    return isValidation() == that.isValidation();
  }
}
