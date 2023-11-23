package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Configuration;
import java.util.Collection;
import java.util.Objects;








public class StartGame
  extends Message
{
  public static final int uniqueID = 400;
  private Configuration config;
  private Collection<Client> clients;
  
  public StartGame(Configuration config, Collection<Client> clients)
  {
    super(400);
    this.config = config;
    this.clients = clients;
  }
  
  public Configuration getConfig() {
    return config;
  }
  
  public Collection<Client> getClients() {
    return clients;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof StartGame)) {
      return false;
    }
    StartGame startGame = (StartGame)o;
    return (Objects.equals(getConfig(), startGame.getConfig())) && 
      (Objects.equals(getClients(), startGame.getClients()));
  }
}
