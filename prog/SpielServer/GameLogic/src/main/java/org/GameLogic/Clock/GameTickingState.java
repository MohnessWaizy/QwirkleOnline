package org.GameLogic.Clock;

/**
 * The state of gameclock. When clock is in turntime, state is {@link TURNTIME},
 * otherwise clock is in visualiationtime, so state is {link VISUALIZATIONTIME}.
 */
public enum GameTickingState {
	TURNTIME, VISUALIZATIONTIME, ENDED;
}
