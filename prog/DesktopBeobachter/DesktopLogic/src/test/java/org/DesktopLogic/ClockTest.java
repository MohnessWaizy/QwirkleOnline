package org.DesktopLogic;

import model.Clock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.Math;

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
		Clock clock = new Clock(cycle, false);
		
		clock.startTicking();
		
		try {
			Thread.sleep(8100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		clock.stopTicking();
		
		int time = Math.toIntExact(clock.getTime());
		
		assertEquals("Clock time is wrong", 2000, Math.toIntExact(clock.getTime()));
		assertEquals("Left clock time is wrong", 1000, Math.toIntExact(clock.getTimeLeft()));
		
		threadSleep(2);
		
		clock.destroy();
		
		assertEquals("Clock is ticking but clock is already stopped", time, Math.toIntExact(clock.getTime()));
		assertEquals("Clock total time is wrong", 8000, Math.toIntExact(clock.getTotalTime()));
	}
	
	@Test
	public void stopAndStart() {
		int cycle = 3000;
		Clock clock = new Clock(cycle, false);
		
		clock.startTicking();
		
		try {
			Thread.sleep(2100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int time = Math.toIntExact(clock.getTime());
		clock.stopTicking();
		
		threadSleep(3);
		
		assertEquals("Clock did not stop properly", time, clock.getTime());
	}
	
	@Test
	public void cycle() {
		int cycle = 3000;
		Clock clock = new Clock(cycle, false);
		
		clock.startTicking();
		
		try {
			Thread.sleep(8100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int time = Math.toIntExact(clock.getTime());
		int totaltime = Math.toIntExact(clock.getTotalTime());
		
		clock.destroy();
		
		assertEquals("Clock did not properly cycle, total time wrong", 8000, totaltime);
		assertEquals("Clock did not properly cycle, time wrong", 2000, time);
	}
	
	@Test
	public void destroy() {
		int cycle = 1000;
		Clock clock = new Clock(cycle, false);
		
		clock.startTicking();
		
		threadSleep(2);
		
		int time = Math.toIntExact(clock.getTime());
		int totaltime = Math.toIntExact(clock.getTotalTime());
		clock.destroy();
		
		threadSleep(2);
		
		assertEquals("Clock is not properly destroyed, total time wrong", totaltime, Math.toIntExact(clock.getTotalTime()));
		assertEquals("Clock is not properly destroyed, time wrong", time, Math.toIntExact(clock.getTime()));
	}

}
