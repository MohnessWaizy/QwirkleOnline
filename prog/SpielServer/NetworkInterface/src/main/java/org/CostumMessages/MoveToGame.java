package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.ClientType;

/**
 *
 * Signals that a client should be moved to a game. Send by: Gui; Received by:
 * Lobby
 *
 */
public class MoveToGame extends Message {
	private int clientId;
	private int gameId;
	private ClientType clientType;

	public MoveToGame(int clientId, ClientType clientType, int gameId) {
		super(3130);
		this.clientId = clientId;
		this.clientType = clientType;
		this.gameId = gameId;
	}

	public int getClientId() {
		return clientId;
	}

	public int getGameId() {
		return gameId;
	}

	public ClientType getClientType() {
		return clientType;
	}

}
