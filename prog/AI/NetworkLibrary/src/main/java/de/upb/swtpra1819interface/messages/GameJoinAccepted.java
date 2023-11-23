package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Game;
import java.util.Objects;







public class GameJoinAccepted
  extends Message
{
  public static final int uniqueID = 303;
  private Game game;
  
  public GameJoinAccepted(Game game)
  {
    super(303);
    this.game = game;
  }
  
  public Game getGame() {
    return game;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof GameJoinAccepted)) {
      return false;
    }
    GameJoinAccepted that = (GameJoinAccepted)o;
    return Objects.equals(game, game);
  }
}
