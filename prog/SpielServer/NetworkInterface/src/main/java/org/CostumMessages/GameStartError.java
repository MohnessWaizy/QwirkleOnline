package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;

/**
 * This class signals the gui that a game start failed
 *
 */
public class GameStartError extends Message {
	private int gameId;

	public GameStartError(int gameId) {
		super(3990);
		this.gameId = gameId;
	}

	public int getGameId() {
		return gameId;
	}

}
