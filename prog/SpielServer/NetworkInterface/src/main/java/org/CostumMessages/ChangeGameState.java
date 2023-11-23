package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.GameState;

/**
 * 
 * Send if a gamestate of a game should be changed. Send by: Gui; Received by:
 * Lobby
 *
 */
public class ChangeGameState extends Message {

	private int gameId = -1;
	private GameState state = GameState.NOT_STARTED;
	private Client client = null;

	public ChangeGameState(int gameId, GameState state) {
		super(3110);
		this.gameId = gameId;
		this.state = state;
	}

	public ChangeGameState(int gameId, GameState state, Client client) {
		super(3110);
		this.gameId = gameId;
		this.state = state;
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

	public int getGameId() {
		return gameId;
	}

	public GameState getState() {
		return state;
	}

}
