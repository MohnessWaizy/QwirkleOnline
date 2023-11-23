package org.GameLogic.Clock;

/**
 * {@link ClockInterface} has to be implemented by {@link Clock} classes.
 */
public interface ClockInterface {

	/**
	 * Is called when clock period is over. It represents a tick of clock.
	 */
	public void ticking();

}
