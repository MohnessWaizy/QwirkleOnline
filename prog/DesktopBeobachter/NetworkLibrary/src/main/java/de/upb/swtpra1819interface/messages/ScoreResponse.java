package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Client;
import java.util.Map;
import java.util.Objects;







public class ScoreResponse
  extends Message
{
  public static final int uniqueID = 418;
  private Map<Client, Integer> scores;
  
  public ScoreResponse(Map<Client, Integer> scores)
  {
    super(418);
    this.scores = scores;
  }
  
  public Map<Client, Integer> getScores() {
    return scores;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ScoreResponse)) {
      return false;
    }
    ScoreResponse that = (ScoreResponse)o;
    return Objects.equals(getScores(), that.getScores());
  }
}
