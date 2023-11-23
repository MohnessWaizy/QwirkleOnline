package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.GameState;
import de.upb.swtpra1819interface.models.Tile;
import de.upb.swtpra1819interface.models.TileOnPosition;
import java.util.Collection;
import java.util.Objects;












public class GameDataResponse
  extends Message
{
  public static final int uniqueID = 499;
  private Collection<TileOnPosition> board;
  private Client currentClient;
  private Collection<Tile> ownTiles;
  private GameState gameState;
  
  public GameDataResponse(Collection<TileOnPosition> board, Client currentClient, Collection<Tile> ownTiles, GameState gameState)
  {
    super(499);
    this.board = board;
    this.currentClient = currentClient;
    this.ownTiles = ownTiles;
    this.gameState = gameState;
  }
  
  public Collection<TileOnPosition> getBoard() {
    return board;
  }
  
  public Client getCurrentClient() {
    return currentClient;
  }
  
  public Collection<Tile> getOwnTiles() {
    return ownTiles;
  }
  
  public GameState getGameState() {
    return gameState;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof GameDataResponse)) {
      return false;
    }
    GameDataResponse that = (GameDataResponse)o;
    return (getGameState() == that.getGameState()) && 
      (Objects.equals(getBoard(), that.getBoard())) && 
      (Objects.equals(getCurrentClient(), that.getCurrentClient())) && 
      (Objects.equals(getOwnTiles(), that.getOwnTiles()));
  }
}
