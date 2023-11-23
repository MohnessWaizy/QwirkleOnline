package org.GameLogic.DataStructures;

import de.upb.swtpra1819interface.models.Client;

/**
 * Abstract class for creating a Player or Spectator
 */
public abstract class Participant {
	/**
	 * Every participant has a uniquely associated client instance
	 */
	protected Client client;

	public Participant(Client client) {
		this.client = client;
	}

	/*
	 * Getters and setters
	 */

	public int getId() {
		return client.getClientId();
	}

	public String getName() {
		return client.getClientName();
	}

	public Client getClient() {
		return client;
	}

	@Override
	public String toString() {
		return "Participant [id=" + getId() + ", name=" + getName() + "]";
	}

}
