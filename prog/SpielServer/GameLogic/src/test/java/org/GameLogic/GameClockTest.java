package org.GameLogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.GameLogic.Clock.ClockState;
import org.GameLogic.Clock.GameClock;
import org.junit.Test;

public class GameClockTest {

	public void threadSleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			assertTrue("Thread could not be paused!", true);
		}
	}

	@Test
	public void initialization() {
		ClockableTestClass ctc = new ClockableTestClass();
		long turnTime = 2000;
		long visualizationTime = 2000;
		GameClock clock = new GameClock(ctc, turnTime, visualizationTime);

		assertTrue("ClockState not properly set", clock.getState() == ClockState.STOPPED);
		assertTrue("GameClockState not properly set", clock.isInTurnTime() == true);

		clock.destroy();
	}

	@Test
	public void ticking() {
		ClockableTestClass ctc = new ClockableTestClass();
		long turnTime = 2000;
		long visualizationTime = 2000;
		GameClock clock = new GameClock(ctc, turnTime, visualizationTime);

		clock.startTicking();

		threadSleep(1200);

		clock.pauseTicking();

		assertTrue("Clock is not ticking", clock.getTotalTime() >= 1000);

		clock.destroy();
	}

	@Test
	public void stopAndStart() {
		ClockableTestClass ctc = new ClockableTestClass();
		long turnTime = 2000;
		long visualizationTime = 2000;
		GameClock clock = new GameClock(ctc, turnTime, visualizationTime);

		clock.startTicking();

		threadSleep(1200);

		clock.pauseTicking();
		long totalTime = clock.getTotalTime();
		long time = clock.getTime();

		threadSleep(520);

		assertEquals("Clock is not stopped", totalTime, clock.getTotalTime());
		assertEquals("Clock is not stopped", time, clock.getTime());

		clock.startTicking();

		threadSleep(520);

		clock.pauseTicking();

		assertTrue("Clock not resumed properly", totalTime != clock.getTotalTime());
		assertTrue("Clock not resumed properly", time != clock.getTime());

		clock.destroy();
	}

	@Test
	public void destroy() {
		ClockableTestClass ctc = new ClockableTestClass();
		long turnTime = 2000;
		long visualizationTime = 2000;
		GameClock clock = new GameClock(ctc, turnTime, visualizationTime);

		clock.startTicking();

		threadSleep(1200);

		clock.destroy();

		threadSleep(1000);

	}

	@Test
	public void testMilliseconds() {
		// will test clock milliseconds at rate 500
		ClockableTestClass ctc = new ClockableTestClass();
		long turnTime = 200;
		long visualizationTime = 200;
		GameClock clock = new GameClock(ctc, turnTime, visualizationTime);

		clock.startTicking();

		threadSleep(5000);

		clock.destroy();
	}

	@Test
	public void cycle() {
		ClockableTestClass ctc = new ClockableTestClass();
		long turnTime = 1000;
		long visualizationTime = 1000;
		GameClock clock = new GameClock(ctc, turnTime, visualizationTime);

		clock.startTicking();

		threadSleep(500);

		assertTrue("Clock not in started state", clock.getState() == ClockState.STARTED);
		assertTrue("Clock should be in turntime", clock.isInTurnTime() == true);

		clock.pauseTicking();

		assertTrue("Clock should be paused", clock.isPaused() == true);

		clock.startTicking();

		assertTrue("Clock not in started state", clock.getState() == ClockState.STARTED);

		threadSleep(510);

		assertTrue("Clock not in visualization time", clock.isInTurnTime() == false);

		threadSleep(510);

		assertTrue("Clock not in turn time", clock.isInTurnTime() == true);

		clock.destroy();
	}

	@Test
	public void turnTimeOver() {
		ClockableTestClass ctc = new ClockableTestClass();
		long turnTime = 800;
		long visualizationTime = 1000;
		GameClock clock = new GameClock(ctc, turnTime, visualizationTime);

		clock.startTicking();

		threadSleep(500);

		clock.turnTimeOver();

		threadSleep(200);

		clock.pauseTicking();

		assertEquals("Clock not in visualization time", false, clock.isInTurnTime());

		clock.startTicking();

		long time = clock.getTime();
		clock.turnTimeOver();

		clock.pauseTicking();

		assertEquals("Clock time should not be changed", time, clock.getTime());
		assertEquals("Clock not in visualization time", false, clock.isInTurnTime());

		clock.destroy();
	}

	@Test
	public void getTurnTime() {
		ClockableTestClass ctc = new ClockableTestClass();
		long turnTime = 600;
		long visualizationTime = 600;
		GameClock clock = new GameClock(ctc, turnTime, visualizationTime);

		clock.startTicking();

		threadSleep(200);

		clock.turnTimeOver();

		threadSleep(200);

		clock.pauseTicking();

		threadSleep(500);

		assertTrue("Clock total time wrong", clock.getTotalTime() <= 600);
		assertTrue("Clock left time wrong", clock.getTimeLeft() >= 200);

		clock.destroy();
	}

}
