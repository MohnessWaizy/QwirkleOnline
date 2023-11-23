package de.upb.swtpra1819interface.messages;



public class TurnTimeLeftResponse
  extends Message
{
  public static final int uniqueID = 420;
  

  private long time;
  


  public TurnTimeLeftResponse(long time)
  {
    super(420);
    this.time = time;
  }
  
  public long getTime() {
    return time;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TurnTimeLeftResponse)) {
      return false;
    }
    TurnTimeLeftResponse that = (TurnTimeLeftResponse)o;
    return getTime() == that.getTime();
  }
}
