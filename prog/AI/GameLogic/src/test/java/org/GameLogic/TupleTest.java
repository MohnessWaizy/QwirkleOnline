package org.GameLogic;

import org.GameLogic.Board.Tuple;
import org.junit.Test;

import junit.framework.TestCase;

public class TupleTest extends TestCase {

	@Test
	public void testFirstAndSecondValue() {

		String a = "Test";
		Integer b = 12;
		
		String c = "Test2";
		Integer d = 123;

		Tuple<String, Integer> tuple = new Tuple<String, Integer>(a, b);

		assertEquals(tuple.getFirst(), a);
		assertEquals(tuple.getSecond(), b);
		
		tuple.setFirst(c);
		tuple.setSecond(d);
		
		assertEquals(tuple.getFirst(), c);
		assertEquals(tuple.getSecond(), d);

	}

}