package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Game;

/**
 * 
 * Class signals GUI that a client joined a game. Send by:Lobby; Received by Gui
 * 
 */
public class ClientJoinGame extends Message {
	private Game game;
	private Client client;

	public ClientJoinGame(Game game, Client client) {
		super(2120);
		this.game = game;
		this.client = client;
	}

	public Game getGame() {
		return game;
	}

	public Client getClient() {
		return client;
	}
}
