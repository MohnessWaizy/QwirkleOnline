package org.GameLogic.Clock;

/**
 * {@link GameClock} implements two countdowns, turnTime and visualizationTime.
 */
public class GameClock extends Clock implements ClockInterface {

	/**
	 * Clockable object to call method {@link Clockable#visualisationTimeElapsed()}
	 * and {@link Clockable#turnTimeElapsed()}.
	 */
	private Clockable clockable;

	/**
	 * The extended clock state. To represent correct game state we need more states
	 * than just started, stopped and paused. We need it to represent if clock time
	 * is in {@link #turnTime} or in {@link #visualizationTime}.
	 */
	private GameTickingState clockState;

	/**
	 * Given turn time of game.
	 */
	private long turnTime;

	/**
	 * Given visualization time of game.
	 */
	private long visualizationTime;

	/**
	 * The total time clock ticked.
	 */
	private long totalTime;

	/**
	 * The real whole turn time spectator or player see.
	 */
	private long timeOfTurn;

	/**
	 * Be sure you do not input {@link #totalTime} or {@link #visualizationTime}
	 * less than the fixed period time. If you do, {@link #totalTime} and
	 * {@link #visualizationTime} will be set to fixed period time (usually around
	 * 200ms).
	 * 
	 * @param clockable
	 * @param turnTime
	 *            milliseconds
	 * @param visualizationTime
	 *            milliseconds
	 */
	public GameClock(Clockable clockable, long turnTime, long visualizationTime) {
		super((turnTime + visualizationTime));
		timeOfTurn = 0;

		if (turnTime < period) {
			this.turnTime = period;
			this.cycle = this.turnTime + this.visualizationTime;
		} else {
			this.turnTime = turnTime;
		}

		if (visualizationTime < period) {
			this.visualizationTime = period;
			this.cycle = this.turnTime + this.visualizationTime;
		} else {
			this.visualizationTime = visualizationTime;
		}

		this.clockable = clockable;
		this.clockState = GameTickingState.TURNTIME;
	}

	/**
	 * Is called when cycle time elapsed.
	 */
	public void timeElapsed() {
		timeOfTurn = 0;
		clockable.visualisationTimeElapsed();
		clockState = GameTickingState.TURNTIME;
	}

	/**
	 * Is called when clock ticked.
	 */
	public void ticking() {
		if (state == ClockState.STARTED) {
			super.ticking();

			timeOfTurn += period;

			totalTime += period;
			if (time >= turnTime && clockState == GameTickingState.TURNTIME) {
				clockState = GameTickingState.VISUALIZATIONTIME;
				clockable.turnTimeElapsed();
			} else if (time <= turnTime && clockState == GameTickingState.VISUALIZATIONTIME) {
				clockState = GameTickingState.TURNTIME;
				clockable.visualisationTimeElapsed();
			}
		}
	}

	/**
	 * Clock will change state to visualization time.
	 */
	public void turnTimeOver() {
		if (clockState == GameTickingState.TURNTIME) {
			clockState = GameTickingState.VISUALIZATIONTIME;
			time = turnTime;
		}
	}

	/**
	 * Get clock ticking period.
	 * 
	 * @return milliseconds
	 */
	public int getClockPeriod() {
		return period;
	}

	/**
	 * Returns total time clock ticked.
	 * 
	 * @return milliseconds
	 */
	public long getTotalTime() {
		return totalTime;
	}

	/**
	 * Returns actual time of turn.
	 * 
	 * @return milliseconds
	 */
	public long getTime() {
		return timeOfTurn;
	}

	/**
	 * Will destroy clock thread and will permit the clock from ticking.
	 */
	public void destroy() {
		super.destroy();
		clockState = GameTickingState.ENDED;
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
	 * Returns left time of actual turn.
	 * 
	 * @return milliseconds
	 */
	public long getTimeLeft() {
		return (turnTime - timeOfTurn);
	}

	/**
	 * Returns true if time is in turn time. Otherwise returns false.
	 * 
	 * @return boolean
	 */
	public boolean isInTurnTime() {
		if (clockState == GameTickingState.TURNTIME) {
			return true;
		} else {
			return false;
		}
	}

}
