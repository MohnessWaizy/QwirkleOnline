package de.upb.swtpra1819interface.models;

import java.util.Collection;
import java.util.Objects;
















public class Game
{
  private int gameId;
  private String gameName;
  private GameState gameState;
  private boolean isTournament;
  private Collection<Client> players;
  private Configuration config;
  
  public Game(int gameId, String gameName, GameState gameState, boolean isTournament, Collection<Client> players, Configuration config)
  {
    this.gameId = gameId;
    this.gameName = gameName;
    this.gameState = gameState;
    this.isTournament = isTournament;
    this.players = players;
    this.config = config;
  }
  
  public int getGameId()
  {
    return gameId;
  }
  
  public String getGameName() {
    return gameName;
  }
  
  public GameState getGameState() {
    return gameState;
  }
  
  public boolean isTournament() {
    return isTournament;
  }
  
  public Collection<Client> getPlayers() {
    return players;
  }
  
  public Configuration getConfig() {
    return config;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Game)) {
      return false;
    }
    Game game = (Game)o;
    return (getGameId() == game.getGameId()) && 
      (isTournament() == game.isTournament()) && 
      (Objects.equals(getGameName(), game.getGameName())) && 
      (getGameState() == game.getGameState()) && 
      (Objects.equals(getPlayers(), game.getPlayers())) && 
      (Objects.equals(getConfig(), game.getConfig()));
  }
}
