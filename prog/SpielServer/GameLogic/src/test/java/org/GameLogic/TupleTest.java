package org.GameLogic;

import org.GameLogic.DataStructures.Tuple;
import org.junit.Test;

import junit.framework.TestCase;

public class TupleTest extends TestCase {

	@Test
	public void testFirstAndSecondValue() {

		String a = "Test";
		Integer b = 12;

		Tuple<String, Integer> tuple = new Tuple<String, Integer>(a, b);

		assertEquals(tuple.getFirst(), a);
		assertEquals(tuple.getSecond(), b);

	}

}
