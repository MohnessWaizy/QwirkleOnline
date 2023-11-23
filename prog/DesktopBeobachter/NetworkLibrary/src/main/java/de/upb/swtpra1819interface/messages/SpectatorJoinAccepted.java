package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Game;
import java.util.Objects;







public class SpectatorJoinAccepted
  extends Message
{
  public static final int uniqueID = 305;
  private Game game;
  
  public SpectatorJoinAccepted(Game game)
  {
    super(305);
    this.game = game;
  }
  
  public Game getGameId() {
    return game;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SpectatorJoinAccepted)) {
      return false;
    }
    SpectatorJoinAccepted that = (SpectatorJoinAccepted)o;
    return Objects.equals(game, game);
  }
}
