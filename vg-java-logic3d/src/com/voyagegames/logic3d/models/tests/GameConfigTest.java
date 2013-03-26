package com.voyagegames.logic3d.models.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.voyagegames.logic3d.models.Common;
import com.voyagegames.logic3d.models.GameConfig;

public class GameConfigTest {

	@Test
	public void testGameConfig_successful() {
		final GameConfig gc = new GameConfig(Common.SolutionSize, Common.MinimumPlayers);
		assertTrue(gc.size == Common.SolutionSize);
		assertTrue(gc.numPlayers == Common.MinimumPlayers);
	}

	@Test
	public void testGameConfig_sizeTooSmall() {
		try {
			new GameConfig(1, Common.MinimumPlayers);
			fail("Should not hit this");
		} catch (final IllegalArgumentException e) {
			// no-op; expected failure
		}
	}

	@Test
	public void testGameConfig_tooFewPlayers() {
		try {
			new GameConfig(Common.SolutionSize, 1);
			fail("Should not hit this");
		} catch (final IllegalArgumentException e) {
			// no-op; expected failure
		}
	}

}
