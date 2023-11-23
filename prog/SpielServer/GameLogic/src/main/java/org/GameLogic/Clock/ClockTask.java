package org.GameLogic.Clock;

/**
 * {@link ClockTask} runs as a thread. Therefore, Thread will call {@link run()} method.
 */
public class ClockTask implements Runnable {

	/**
	 * The given clock interface with callable ticking method.
	 */
	private ClockInterface clock;

	/**
	 * Instantiate object as runnable. 
	 * Can be started with {@link start()}
	 * @param clock
	 */
	public ClockTask(ClockInterface clock) {
		super();
		this.clock = clock;
	}

	/**
	 * Is called when thread will be started.
	 */
	public void run() {
		clock.ticking();
	}

}
