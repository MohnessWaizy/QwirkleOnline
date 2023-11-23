package clock;

public class ClockTask implements Runnable {

	/**
	 * The given clock interface with callable ticking method.
	 */
	private ClockInterface clock;
	
	/**
	 * 
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
