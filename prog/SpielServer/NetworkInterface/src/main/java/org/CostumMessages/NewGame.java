package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.Configuration;
import de.upb.swtpra1819interface.models.Game;
import de.upb.swtpra1819interface.models.GameState;

/**
 * 
 * Signals a new game should be created. Send by: 1.Instance: GUI, 2.Instance:
 * Lobby; Received by: 1.Instance: Lobby, 2.Instance: GUI
 *
 */
public class NewGame extends Message {

	private int gameId = -1;
	private String gameName;
	private boolean isTournament;
	private Configuration config;
	private Integer tournamentId = null;

	public NewGame(String gameName, boolean isTournament, Configuration config) {
		super(2130);
		this.gameName = gameName;
		this.isTournament = isTournament;
		this.config = config;
	}

	public Integer getTournamentId() {
		return tournamentId;
	}

	public void setTournamentId(Integer tournamentId) {
		this.tournamentId = tournamentId;
	}

	public Game getGame() {
		return new Game(gameId, gameName, GameState.NOT_STARTED, isTournament, null, config);
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public boolean isTournament() {
		return isTournament;
	}

	public Configuration getConfig() {
		return config;
	}

}
