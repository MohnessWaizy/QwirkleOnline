package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;

/**
 * 
 * Signals that the server should be shut down. Send by: GUI, Received by: Lobby
 *
 */
public class Shutdown extends Message {

	public Shutdown() {
		super(3120);
	}

}
