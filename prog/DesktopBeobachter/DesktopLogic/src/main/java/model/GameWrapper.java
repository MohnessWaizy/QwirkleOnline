package model;


import org.NetworkInterface.NetClient;

import de.upb.swtpra1819interface.models.Client;
import de.upb.swtpra1819interface.models.Game;

/**
 * wrapper class that is sent from the lobby to the field
 *
 */
public class GameWrapper {
	private Client client;
	private NetClient netClient;
	private Game game;
	


	/**
	 * constructor
	 * 
	 * @param client
	 * @param netClient
	 * @param game
	 */
	public GameWrapper(Client client, NetClient netClient, Game game) {
		this.client = client;
		this.netClient = netClient;
		this.game = game;
	}

	public Game getGame() {
		return this.game;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	public NetClient getNetClient() {
		return netClient;
	}

	public void setNetClient(NetClient netClient) {
		this.netClient = netClient;
	}
}
