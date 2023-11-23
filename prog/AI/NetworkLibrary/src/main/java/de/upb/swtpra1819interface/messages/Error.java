package de.upb.swtpra1819interface.messages;




public abstract class Error
  extends Message
{
  private String message;
  


  private int causeId;
  


  public Error(int uniqueId, String message, int causeId)
  {
    super(uniqueId);
    this.message = message;
    this.causeId = causeId;
  }
  
  public String getMessage() {
    return message;
  }
  
  public int getCauseId() {
    return causeId;
  }
}
