package de.upb.swtpra1819interface.messages;



public class GameJoinRequest
  extends Message
{
  public static final int uniqueID = 302;
  

  private int gameId;
  


  public GameJoinRequest(int gameId)
  {
    super(302);
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
    if (!(o instanceof GameJoinRequest)) {
      return false;
    }
    GameJoinRequest that = (GameJoinRequest)o;
    return getGameId() == that.getGameId();
  }
}
