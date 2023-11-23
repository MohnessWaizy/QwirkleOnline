package model;

import java.util.Collection;

import de.upb.swtpra1819interface.models.Game;

/**
 * class to hold current games
 *
 */
public class Lobby {
	private static Collection<Game> currentGames;

	/**
	 * @return current games
	 */
	public static Collection<Game> getCurrentGames() {
		return currentGames;
	}

	/**
	 * @param games Updates current games
	 */
	public static void setCurrentGames(Collection<Game> games) {
		currentGames = games;
	}
}
