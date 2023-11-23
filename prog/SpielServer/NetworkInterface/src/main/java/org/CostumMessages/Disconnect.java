package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;

/**
 * 
 * Send if a client disconnects from the server. Send by: GameManagement
 * Received by: Lobby, Gui, Game
 *
 */
public class Disconnect extends Message {

	private int clientId = -1;

	public Disconnect(int clientId) {
		super(1100);
		this.clientId = clientId;
	}

	public int getClientId() {
		return clientId;
	}
}
