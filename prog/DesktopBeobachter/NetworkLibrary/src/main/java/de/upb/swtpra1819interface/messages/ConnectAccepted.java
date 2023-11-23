package de.upb.swtpra1819interface.messages;



public class ConnectAccepted
  extends Message
{
  public static final int uniqueID = 101;
  

  private int clientId;
  


  public ConnectAccepted(int clientId)
  {
    super(101);
    this.clientId = clientId;
  }
  
  public int getClientId() {
    return clientId;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ConnectAccepted)) {
      return false;
    }
    ConnectAccepted that = (ConnectAccepted)o;
    return getClientId() == that.getClientId();
  }
}
