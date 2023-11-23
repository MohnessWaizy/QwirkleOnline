package model;

import javafx.application.Platform;

public class ClockTask implements Runnable {

	/**
	 * The clock where ticking will be called.
	 */
	private ClockInterface clock;

	/**
	 * The given period clock is ticking.
	 */
	private int period;

	/**
	 * When javafx is true, ticking method will call ticking method in consideration
	 * of ui.
	 */
	private boolean javafx;

	/**
	 * Instantiate object as runnable. Can be started with {@link start()}
	 * 
	 * @param clock  Clock where ticking will be called
	 * @param period The given period clock is ticking in milliseconds
	 * @param javafx True when ticking method is called on ui elements
	 */
	public ClockTask(ClockInterface clock, int period, boolean javafx) {
		super();
		this.period = period;
		this.clock = clock;
		this.javafx = javafx;
	}

	/**
	 * Is called when thread will be started.
	 */
	public void run() {
		if (javafx == true) {
			Platform.runLater(() -> {
				clock.ticking();
			});
		} else {
			clock.ticking();
		}
	}

}
