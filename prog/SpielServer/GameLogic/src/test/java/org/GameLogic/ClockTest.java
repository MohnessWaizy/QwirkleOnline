package org.GameLogic;

import static org.junit.Assert.*;

import org.GameLogic.Clock.Clock;
import org.junit.Test;

public class ClockTest {

	public void threadSleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			assertTrue("Thread could not be paused!", true);
		}
	}

	@Test
	public void ticking() {
		int cycle = 3000;
		Clock clock = new Clock(cycle);

		clock.startTicking();

		threadSleep(2);

		clock.pauseTicking();

		long time = clock.getTime();
		long totalTime = clock.getTotalTime();
		assertEquals("Clock time is wrong", 2000, clock.getTime());
		assertEquals("Left clock time is wrong", 1000, clock.getTimeLeft());

		threadSleep(2);

		assertEquals("Clock is ticking but clock is already stopped", time, clock.getTime());
		assertEquals("Clock total time is wrong", totalTime, clock.getTotalTime());

		clock.destroy();
	}

	@Test
	public void startAndStop() {
		int cycle = 3000;
		Clock clock = new Clock(cycle);

		clock.startTicking();

		threadSleep(2);

		assertEquals("Clock not in ticking state", true, clock.isStarted());
		assertEquals("Clock not in ticking state", false, clock.isPaused());

		clock.pauseTicking();

		assertEquals("Clock not in ticking state", true, clock.isPaused());
		assertEquals("Clock not in ticking state", false, clock.isStarted());

		long time = clock.getTime();

		threadSleep(3);

		assertEquals("Clock did not stop properly", time, clock.getTime());

		clock.destroy();
	}

	@Test
	public void testMilliseconds() {
		int cycle = 100;
		Clock clock = new Clock(cycle);

		clock.startTicking();

		threadSleep(5);

		clock.pauseTicking();
		long time = clock.getTime();

		threadSleep(1);

		assertEquals("Clock did not stop properly", time, clock.getTime());

		clock.destroy();
	}

	@Test
	public void pauseAndResume() {
		int cycle = 200;
		Clock clock = new Clock(cycle);

		clock.startTicking();

		threadSleep(1);

		clock.pauseTicking();
		assertEquals("Clock not in ticking state", true, clock.isPaused());

		threadSleep(1);

		clock.resumeTicking();

		threadSleep(1);
		assertEquals("Clock not in ticking state", false, clock.isPaused());

		clock.destroy();

	}

	@Test
	public void destroy() {
		int cycle = 200;
		Clock clock = new Clock(cycle);

		// no exception should be thrown
		clock.destroy();
	}

}
