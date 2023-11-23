package clock;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.*;

/*
 * TODO : Implement this with OberserverPattern
 *  but not here, create some ClockObserver
 *  could be done with EventHandler:
 *  https://stackoverflow.com/questions/44912/java-delegates #35
 */

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
	protected final int period = 200;

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
	 * 
	 * @return
	 */
	public long getTotalTime() {
		return totalTime;
	}

	/**
	 * 
	 * @return
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
	 * Cycle in milliseconds.
	 * 
	 * @param cycle milliseconds
	 */
	public Clock(long cycle) {
		this.cycle = cycle;
		this.state = ClockState.STOPPED;
		this.time = 0;
	}

	/**
	 * Ticking will be called by ticking thread.
	 */
	public void ticking() {
		if(state == ClockState.STARTED) {
			time += period;
			totalTime += period;
			if (time >= cycle) {
				time = 0;
				timeElapsed();
			}
		}
	}
	
	/**
	 * Ticking thread will be started and clock will tick.
	 */
	public void startTicking() {
		final Runnable tick = new ClockTask(this);
		state = ClockState.STARTED;
		tickHandle = scheduler.scheduleAtFixedRate(tick, 0, period, MILLISECONDS);
	}

	/**
	 * Returns time left until clock repeats at zero.
	 * Time left is calculated by cycle-time.
	 * 
	 * @return
	 */
	public long getTimeLeft() {
		return cycle-time;
	}
	
	/**
	 * Clock will stop and thread will be canceled.
	 */
	public void destroy() {
		state = ClockState.STOPPED;
		try {
			tickHandle.cancel(true);
		} catch(NullPointerException ex) {
			// there was no tickHandle to destroy 
		}
		scheduler.shutdown();
		time = 0;
	}
	
	/**
	 * Pause ticking.
	 */
	public void pauseTicking() {
		state = ClockState.PAUSED;
	}
	
	/**
	 * Resume ticking.
	 */
	public void resumeTicking() {
		state = ClockState.STARTED;
	}

	/**
	 * Returns true if clock is state started, otherwise returns false.
	 *  
	 * @return
	 */
	public boolean isStarted() {
		if(state == ClockState.STARTED) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Clock will be paused.
	 * 
	 * @return
	 */
	public boolean isPaused() {
		if(state == ClockState.PAUSED) {
			return true;
		} else {
			return false;
		}
	}
}