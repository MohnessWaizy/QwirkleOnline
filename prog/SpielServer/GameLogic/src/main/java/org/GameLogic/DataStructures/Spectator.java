package org.GameLogic.DataStructures;

import de.upb.swtpra1819interface.models.Client;

/**
 * A class for a spectator
 */
public class Spectator extends Participant {

	public Spectator(Client client) {
		super(client);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Spectator other = (Spectator) obj;
		return other.client.equals(this.client);
	}

}
