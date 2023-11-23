package org.GameLogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.GameLogic.DataStructures.CycleList;
import org.junit.Test;

public class CycleListTest {

	@Test
	public void testCycleThrough() {

		ArrayList<String> compare = new ArrayList<String>();
		compare.add("1");
		compare.add("2");
		compare.add("3");

		CycleList<String> cl = new CycleList<String>(10);
		cl.add("1");
		cl.add("2");
		cl.add("3");

		cl.next();// ->2
		cl.next();// ->3
		assertEquals("Cycle shift", "1", cl.next());

		for (int i = 0; i < 10; i++) {
			assertEquals(i + "-th cycle item", compare.get(i % 3), cl.current());
			cl.next();
		}

	}

	@Test
	public void testCycleAddAndRemove() {
		CycleList<String> cl = new CycleList<String>(10);
		cl.add("1");
		cl.add("2");
		cl.add("3");

		// c->1 2 3
		assertEquals("First item", "2", cl.next());
		// 1 c->2 3
		cl.add(1, "4");
		// 1 4 c->2 3
		assertEquals("Move current", "2", cl.current());
		cl.remove("1");
		// 4 c->2 3
		assertEquals("Remove first", "2", cl.current());
		assertEquals("Remove item not there", false, cl.remove("5"));
		// 4 c->2 3
		assertEquals("Test current", "2", cl.current());
		assertEquals("Test current", "3", cl.peekNext());
		assertEquals("Test current", "2", cl.current());
		// 4 c->2
		cl.remove("3");
		assertEquals("Remove 3", "2", cl.current());
		// c->4
		cl.remove("2");
		assertEquals("Remove 2", "4", cl.current());
		// empty
		cl.removeCurrent();
		try {
			cl.current();
		} catch (NoSuchElementException expected) {
			assertTrue("No Elements in List", true);
		}
		try {
			cl.next();
		} catch (NoSuchElementException expected) {
			assertTrue("No Elements in List", true);
		}

		try {
			cl.peekNext();
		} catch (NoSuchElementException expected) {
			assertTrue("No Elements in List", true);
		}

		// after adding to empty list, current need to reseted

		cl.add("1");
		assertEquals("Current points to 1", "1", cl.current());
		assertEquals("Loops 1", "1", cl.next());

	}

	@Test
	public void testCurrentPlayerRemove() {
		CycleList<String> cl = new CycleList<String>(3);
		cl.add("1");
		cl.add("2");
		cl.add("3");

		cl.remove("1");
		assertTrue(cl.current() == "3");
		cl.remove("3");
		assertTrue(cl.current() == "2");
		cl.remove("2");
		assertTrue(cl.current() == null);
		assertTrue(cl.remove(0) == null);
	}

}
