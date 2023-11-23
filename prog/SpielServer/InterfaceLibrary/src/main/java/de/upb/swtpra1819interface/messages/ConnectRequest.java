package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.ClientType;
import java.util.Objects;








public class ConnectRequest
  extends Message
{
  public static final int uniqueID = 100;
  private String clientName;
  private ClientType clientType;
  
  public ConnectRequest(String clientName, ClientType clientType)
  {
    super(100);
    this.clientName = clientName;
    this.clientType = clientType;
  }
  
  public String getClientName() {
    return clientName;
  }
  
  public ClientType getClientType() {
    return clientType;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ConnectRequest)) {
      return false;
    }
    ConnectRequest that = (ConnectRequest)o;
    return (Objects.equals(getClientName(), that.getClientName())) && 
      (getClientType() == that.getClientType());
  }
}
