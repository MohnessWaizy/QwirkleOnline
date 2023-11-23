package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Game;
import java.util.Collection;
import java.util.Objects;







public class GameListResponse
  extends Message
{
  public static final int uniqueID = 301;
  private Collection<Game> games;
  
  public GameListResponse(Collection<Game> games)
  {
    super(301);
    this.games = games;
  }
  
  public Collection<Game> getGames() {
    return games;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof GameListResponse)) {
      return false;
    }
    GameListResponse that = (GameListResponse)o;
    return Objects.equals(getGames(), that.getGames());
  }
}
