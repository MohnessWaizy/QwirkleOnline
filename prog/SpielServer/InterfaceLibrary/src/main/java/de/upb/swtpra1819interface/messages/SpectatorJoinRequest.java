package de.upb.swtpra1819interface.messages;



public class SpectatorJoinRequest
  extends Message
{
  public static final int uniqueID = 304;
  

  private int gameId;
  


  public SpectatorJoinRequest(int gameId)
  {
    super(304);
    this.gameId = gameId;
  }
  
  public int getGameId() {
    return gameId;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SpectatorJoinRequest)) {
      return false;
    }
    SpectatorJoinRequest that = (SpectatorJoinRequest)o;
    return getGameId() == that.getGameId();
  }
}
