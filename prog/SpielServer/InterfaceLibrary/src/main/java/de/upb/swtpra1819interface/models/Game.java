package de.upb.swtpra1819interface.models;

import java.util.Collection;

public class Game {
	
	private int gameId;
	private String gameName;
	private GameState gameState;
	private boolean isTournament;
	private Collection<Client> players;
	private Configuration config;

	public Game(int gameId, String gameName, GameState gameState, boolean isTournament, Collection<Client> players,
			Configuration config) {
		this.gameId = gameId;
		this.gameName = gameName;
		this.gameState = gameState;
		this.isTournament = isTournament;
		this.players = players;
		this.config = config;
	}

	public int getGameId() {
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

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Game)) {
			return false;
		}
		Game game = (Game) o;
		return (getGameId() == game.getGameId());
	}

	public void setPlayers(Collection<Client> players) {
		this.players = players;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;

	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setTournament(boolean isTournament) {
		this.isTournament = isTournament;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}
}
