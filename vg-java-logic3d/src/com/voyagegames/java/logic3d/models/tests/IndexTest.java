package com.voyagegames.java.logic3d.models.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.voyagegames.java.logic3d.models.Index;

public class IndexTest {

	@Test
	public void testIndex_successful() {
		final Index index = new Index(1,2,3);
		assertTrue(index.x == 1);
		assertTrue(index.y == 2);
		assertTrue(index.z == 3);
	}

	@Test
	public void testIndex_invalidIndex() {
		try {
			new Index(-1,2,3);
			fail("Shouldn't hit this");
		} catch (final IllegalArgumentException e) {
			// no-op; expected
		}
		
		try {
			new Index(1,-1,3);
			fail("Shouldn't hit this");
		} catch (final IllegalArgumentException e) {
			// no-op; expected
		}
		
		try {
			new Index(1,2,-1);
			fail("Shouldn't hit this");
		} catch (final IllegalArgumentException e) {
			// no-op; expected
		}
	}

	@Test
	public void testArrayLocation_successful() {
		final int size = 4;
		final Index index = new Index(1,2,3);
		final int location = index.toArrayLocation(size);
		
		assertTrue(location == (1 * size * size) + (2 * size) + 3);
	}

}
