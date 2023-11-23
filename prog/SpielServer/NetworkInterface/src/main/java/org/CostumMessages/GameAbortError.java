package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;

/**
 * This class signals the gui that a game abort failed
 *
 */
public class GameAbortError extends Message {
	private int gameId;

	public GameAbortError(int gameId) {
		super(3991);
		this.gameId = gameId;
	}

	public int getGameId() {
		return gameId;
	}
}
