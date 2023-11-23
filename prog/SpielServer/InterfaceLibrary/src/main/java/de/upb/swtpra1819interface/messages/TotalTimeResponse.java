package de.upb.swtpra1819interface.messages;



public class TotalTimeResponse
  extends Message
{
  public static final int uniqueID = 422;
  

  private long time;
  


  public TotalTimeResponse(long time)
  {
    super(422);
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
    if (!(o instanceof TotalTimeResponse)) {
      return false;
    }
    TotalTimeResponse that = (TotalTimeResponse)o;
    return getTime() == that.getTime();
  }
}
