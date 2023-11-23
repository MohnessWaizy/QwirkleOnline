package org.CostumMessages;

import de.upb.swtpra1819interface.messages.ConnectRequest;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.ClientType;

/**
 * 
 * Send if a client joins the Structure Send by: GameManagement; Received by:
 * Game, Lobby, Gui
 *
 */
public class ClientConnect extends Message {
	private ClientType type;
	private int clientId;
	private String username;

	public ClientConnect(String username, int clientId, ClientType type) {
		super(1000);
		this.type = type;
		this.clientId = clientId;
		this.username = username;
	}

	public ClientConnect(int clientId, ConnectRequest req) {
		super(1000);
		this.type = req.getClientType();
		this.clientId = clientId;
		this.username = req.getClientName();
	}

	public ClientConnect(Client client) {
		super(1000);
		this.type = client.getClientType();
		this.clientId = client.getClientId();
		this.username = client.getClientName();
	}

	public ClientType getType() {
		return type;
	}

	public int getClientId() {
		return clientId;
	}

	public String getUsername() {
		return username;
	}

	public Client getClient() {
		return new Client(clientId, username, type);
	}
}
