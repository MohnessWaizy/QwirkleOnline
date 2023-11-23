package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Client;
import java.util.Objects;







public class LeavingPlayer
  extends Message
{
  public static final int uniqueID = 406;
  private Client client;
  
  public LeavingPlayer(Client client)
  {
    super(406);
    this.client = client;
  }
  
  public Client getClient() {
    return client;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LeavingPlayer)) {
      return false;
    }
    LeavingPlayer that = (LeavingPlayer)o;
    return Objects.equals(getClient(), that.getClient());
  }
}
