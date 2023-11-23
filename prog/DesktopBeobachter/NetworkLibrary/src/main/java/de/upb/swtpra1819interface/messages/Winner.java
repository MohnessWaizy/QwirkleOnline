package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Client;
import java.util.Map;
import java.util.Objects;










public class Winner
  extends Message
{
  public static final int uniqueID = 407;
  private Client client;
  private int score;
  private Map<Client, Integer> leaderboard;
  
  public Winner(Client client, int score, Map<Client, Integer> leaderboard)
  {
    super(407);
    this.client = client;
    this.score = score;
    this.leaderboard = leaderboard;
  }
  
  public Client getClient() {
    return client;
  }
  
  public int getScore() {
    return score;
  }
  
  public Map<Client, Integer> getLeaderboard() {
    return leaderboard;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Winner)) {
      return false;
    }
    Winner winner = (Winner)o;
    return (getScore() == winner.getScore()) && 
      (Objects.equals(getClient(), winner.getClient())) && 
      (Objects.equals(getLeaderboard(), winner.getLeaderboard()));
  }
}
