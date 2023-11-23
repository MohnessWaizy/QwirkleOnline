package model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.*;

public class Clock implements ClockInterface {

	/**
	 * Time cylce in milliseconds. It's like an interval.
	 */
	protected long cycle;

	/**
	 * The actual time of the clock in milliseconds.
	 */
	protected long time;

	/**
	 * Total time the clock is ticking.
	 */
	protected long totalTime;

	/**
	 * When javafx is true, ticking method will call ticking method in consideration
	 * of ui.
	 */
	protected boolean javafx;

	/**
	 * Timer that will be used in this class.
	 */
	protected final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	/**
	 * The handle of ticking thread.
	 */
	protected ScheduledFuture<?> tickHandle;

	/**
	 * The period the clock is ticking.
	 */
	private int period = 1000;

	/**
	 * State of clock.
	 */
	protected ClockState state;

	/**
	 * Return cycle time in milliseconds.
	 * 
	 * @return milliseconds
	 */
	public long getCycle() {
		return cycle;
	}

	/**
	 * Return delta time in milliseconds.
	 * 
	 * @return milliseconds
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Returns total time the clock ticked.
	 * 
	 * @return milliseconds
	 */
	public long getTotalTime() {
		return totalTime;
	}

	/**
	 * Returns actual clockstate.
	 * 
	 * @return {@link ClockState}
	 */
	public ClockState getState() {
		return state;
	}

	/**
	 * Have to be overwritten by inerhited classes.
	 */
	public void timeElapsed() {

	}

	/**
	 * Instantiates clock.
	 * 
	 * @param cycle  The cycle the clock will call timeElapsed()
	 * @param javafx If clock is used for javafx events this should be true
	 */
	public Clock(long cycle, boolean javafx) {
		this.cycle = cycle;
		this.state = ClockState.STOPPED;
		this.time = 0;
		this.javafx = javafx;
	}

	/**
	 * Is called every given period of time.
	 */
	public void ticking() {
		if (state == ClockState.STARTED) {
			time += period;
			totalTime += period;
			if (time >= cycle) {
				time = 0;
				timeElapsed();
			}
		}
	}

	/**
	 * Starts clock ticking if state is STOPPED.
	 */
	public void startTicking() {
		if (state == ClockState.STOPPED) {
			final Runnable tick = new ClockTask(this, period, javafx);
			state = ClockState.STARTED;
			tickHandle = scheduler.scheduleAtFixedRate(tick, 1000, period, MILLISECONDS);
		}
	}

	/**
	 * Returns time left until clock repeats at zero. Time left is calculated by
	 * cycle-time.
	 * 
	 * @return milliseconds
	 */
	public long getTimeLeft() {
		return cycle - time;
	}

	/**
	 * Clock will stop and thread will be canceled.
	 */
	public void destroy() {
		state = ClockState.STOPPED;
		tickHandle.cancel(true);
		scheduler.shutdown();
	}

	/**
	 * Stops ticking.
	 */
	public void stopTicking() {
		state = ClockState.PAUSED;
	}

	/**
	 * Resumes ticking.
	 */
	public void resumeTicking() {
		state = ClockState.STARTED;
	}

}