package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.Client;

/**
 * 
 * Send if a Game is aborted. Send by GameManagement; Received by Game
 *
 */
public class GameAbort extends Message {

	private Client client;

	public GameAbort(Client client) {
		super(1402);
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

}
