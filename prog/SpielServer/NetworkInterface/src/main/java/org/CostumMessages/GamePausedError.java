package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;

/**
 * This class signals the gui that a game pause failed
 *
 */
public class GamePausedError extends Message {
	private int gameId;

	public GamePausedError(int gameId) {
		super(3992);
		this.gameId = gameId;
	}

	public int getGameId() {
		return gameId;
	}
}
