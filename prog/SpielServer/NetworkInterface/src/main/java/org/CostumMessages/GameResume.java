package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;

/**
 * 
 * Send if a Game is resumed. Send by GameManagement; Received by Game
 *
 */
public class GameResume extends Message {

	public GameResume() {
		super(1404);
	}

}
