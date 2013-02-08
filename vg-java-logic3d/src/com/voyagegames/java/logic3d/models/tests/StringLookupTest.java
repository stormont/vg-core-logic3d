package com.voyagegames.java.logic3d.models.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.voyagegames.java.logic3d.models.ActionResult;
import com.voyagegames.java.logic3d.models.StringLookup;

public class StringLookupTest {
	
	private Map<String, String> map;

	@Before
	public void setUp() throws Exception {
		map = new HashMap<String, String>();

		final ActionResult[] results = ActionResult.values();
		
		for (final ActionResult r : results) {
			map.put(r.toString(), r.toString() + "_value");
		}
	}

	@Test
	public void testStringLookup_successful() {
		final StringLookup lookup = new StringLookup("en", map);
		assertTrue(lookup.language.contentEquals("en"));
	}

	@Test
	public void testStringLookup_missingString() {
		try {
			final Map<String, String> m = new HashMap<String, String>();
			new StringLookup("en", m);
			fail("Shouldn't hit this");
		} catch (final IllegalArgumentException e) {
			// no-op; expected
		}

		final ActionResult[] results = ActionResult.values();
		
		for (final ActionResult r : results) {
			try {
				final Map<String, String> m = new HashMap<String, String>();
				
				for (final ActionResult r2 : results) {
					if (r == r2) {
						continue;
					}
					
					m.put(r2.toString(), r2.toString() + "_value");
				}
				
				new StringLookup("en", m);
				fail("Shouldn't hit this");
			} catch (final IllegalArgumentException e) {
				// no-op; expected
			}
		}
	}

	@Test
	public void testLookup_successful() {
		final ActionResult[] results = ActionResult.values();
		final StringLookup lookup = new StringLookup("en", map);
		
		for (final ActionResult r : results) {
			assertTrue(lookup.lookup(r).contentEquals(r.toString() + "_value"));
		}
	}

}
