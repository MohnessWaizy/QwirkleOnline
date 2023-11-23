package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;

/**
 * 
 * Send if a Game is paused. Send by GameManagement; Received by Game
 *
 */
public class GamePaused extends Message {

	public GamePaused() {
		super(1403);
	}

}
