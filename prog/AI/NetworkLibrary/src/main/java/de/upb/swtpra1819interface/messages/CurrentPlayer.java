package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Client;
import java.util.Objects;







public class CurrentPlayer
  extends Message
{
  public static final int uniqueID = 409;
  private Client client;
  
  public CurrentPlayer(Client client)
  {
    super(409);
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
    if (!(o instanceof CurrentPlayer)) {
      return false;
    }
    CurrentPlayer that = (CurrentPlayer)o;
    return Objects.equals(getClient(), that.getClient());
  }
}
