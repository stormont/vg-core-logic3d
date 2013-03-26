package com.voyagegames.logic3d.models.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.voyagegames.logic3d.models.Index;
import com.voyagegames.logic3d.models.Player;
import com.voyagegames.logic3d.models.Solution;

public class SolutionTest {

	@Test
	public void testSolution_successful() {
		final List<Index> indices = new ArrayList<Index>();
		indices.add(new Index(1,2,3));
		indices.add(new Index(2,2,3));
		indices.add(new Index(3,2,3));
		indices.add(new Index(4,2,3));
		
		final Solution solution = new Solution(indices, Player.One);
		
		assertTrue(solution.indices == indices);
		assertTrue(solution.player == Player.One);
	}

	@Test
	public void testSolution_solutionTooSmall() {
		try {
			final List<Index> indices = new ArrayList<Index>();
			new Solution(indices, Player.One);
			fail("Shouldn't hit this");
		} catch (final IllegalArgumentException e) {
			// no-op; expected
		}
	}

}
