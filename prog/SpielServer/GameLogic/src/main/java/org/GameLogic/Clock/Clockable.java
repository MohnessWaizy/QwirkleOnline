package org.GameLogic.Clock;

/**
 * The clockable object is implemented in {@link Game}.
 * Every time {@link GameClock} turnTime or visualizationTime is elapsed, these
 * two method will be called.
 */
public interface Clockable {

	/**
	 * Is called when visualization time elapsed.
	 */
	public void visualisationTimeElapsed();

	/**
	 * Is called when turn time elapsed.
	 */
	public void turnTimeElapsed();

}
