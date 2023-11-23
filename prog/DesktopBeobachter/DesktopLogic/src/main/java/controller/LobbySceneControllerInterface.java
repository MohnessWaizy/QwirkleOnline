package controller;

import java.util.Collection;

import de.upb.swtpra1819interface.models.Game;

public interface LobbySceneControllerInterface {

	/**
	 * @param gameID
	 */
	public void joinGame(int gameID);

	/**
	 * @param games
	 */
	public void updateGames(Collection<Game> games);
}
