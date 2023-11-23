package org.CostumMessages;

import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.messages.Winner;
import de.upb.swtpra1819interface.models.Game;

/**
 * 
 * Signals that a game was finished. Send by Game; Received by
 * Gui,GameManagement
 *
 */
public class FinishGame extends Message {
	private Winner winner;
	private Game game;
	private int gameId;

	public FinishGame(Winner winner, Game game) {
		super(2110);
		this.winner = winner;
		this.game = game;
		this.gameId = game.getGameId();
	}

	public FinishGame(Winner winner, int gameId) {
		super(2110);
		this.winner = winner;
		this.gameId = gameId;

	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Winner getWinner() {
		return winner;
	}

	public Game getGame() {
		return game;
	}

	public int getGameId() {
		return gameId;
	}
}
