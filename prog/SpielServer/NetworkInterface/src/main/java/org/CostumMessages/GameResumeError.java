package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;

public class GameResumeError extends Message {
	private int gameId;

	public GameResumeError(int gameId) {
		super(3993);
		this.gameId = gameId;
	}

	public int getGameId() {
		return gameId;
	}
}
