package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;

/**
 * 
 * Send if a Game is started. Send by GameManagement; Received by Game
 *
 */
public class GameStart extends Message {
	public GameStart() {
		super(1400);
	}

}
