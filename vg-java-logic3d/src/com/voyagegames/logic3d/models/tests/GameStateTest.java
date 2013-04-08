package com.voyagegames.logic3d.models.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.voyagegames.logic3d.models.ActionResult;
import com.voyagegames.logic3d.models.Common;
import com.voyagegames.logic3d.models.GameCondition;
import com.voyagegames.logic3d.models.GameConfig;
import com.voyagegames.logic3d.models.GameState;
import com.voyagegames.logic3d.models.Index;
import com.voyagegames.logic3d.models.PieceIndex;
import com.voyagegames.logic3d.models.Player;
import com.voyagegames.logic3d.models.Solution;
import com.voyagegames.logic3d.models.StringLookup;

public class GameStateTest {
	
	private StringLookup strings;
	private GameConfig config;

	@Before
	public void setUp() throws Exception {
		final Map<String, String> map = new HashMap<String, String>();

		final ActionResult[] results = ActionResult.values();
		
		for (final ActionResult r : results) {
			map.put(r.toString(), r.toString() + "_value");
		}
		
		strings = new StringLookup("en", map);
		config = new GameConfig(Common.SolutionSize, Common.MinimumPlayers);
	}

	@Test
	public void testGame_successful() {
		final GameState game = new GameState(config, strings);
		assertTrue(game.config == config);
		assertTrue(game.strings == strings);
		assertTrue(game.pieces.size() == config.size * config.size * config.size);
		assertTrue(game.solution() == null);
		
		for (int i = 0; i < config.size; ++i) {
			for (int j = 0; j < config.size; ++j) {
				for (int k = 0; k < config.size; ++k) {
					final PieceIndex pieceIndex = game.pieces.get((i * config.size * config.size) + (j * config.size) + k);
					assertTrue(pieceIndex.index().x == i);
					assertTrue(pieceIndex.index().y == j);
					assertTrue(pieceIndex.index().z == k);
					assertTrue(pieceIndex.player() == Player.None);
				}
			}
		}
	}

	@Test
	public void testGameCondition() {
		final GameState game = new GameState(config, strings);
		assertTrue(game.gameCondition() == GameCondition.InPlay);
	}

	@Test
	public void testPlayerTurn() {
		final GameState game = new GameState(config, strings);
		assertTrue(game.playerTurn() == Player.One);
	}

	@Test
	public void testSolution() {
		final GameState game = new GameState(config, strings);
		assertTrue(game.solution() == null);
	}

	@Test
	public void testSetSolution_successful() {
		final List<Index> indices = new ArrayList<Index>();
		indices.add(new Index(1,2,3));
		indices.add(new Index(2,2,3));
		indices.add(new Index(3,2,3));
		indices.add(new Index(4,2,3));
		
		final Solution solution = new Solution(indices, Player.One);
		final GameState game = new GameState(config, strings);
		
		game.setSolution(solution);
		assertTrue(game.solution() == solution);
	}

	@Test
	public void testSetSolution_solutionAlreadySet() {
		final List<Index> indices = new ArrayList<Index>();
		indices.add(new Index(1,2,3));
		indices.add(new Index(2,2,3));
		indices.add(new Index(3,2,3));
		indices.add(new Index(4,2,3));
		
		final Solution solution = new Solution(indices, Player.One);
		final GameState game = new GameState(config, strings);
		
		game.setSolution(solution);
		
		try {
			game.setSolution(solution);
			fail("Shouldn't hit this");
		} catch (final IllegalStateException e) {
			// no-op; expected
		}
	}

	@Test
	public void testSetPlayerTurn() {
		final GameState game = new GameState(config, strings);
		game.setPlayerTurn(Player.One);
		assertTrue(game.playerTurn() == Player.One);
	}

	@Test
	public void testSetGameCondition() {
		final GameState game = new GameState(config, strings);
		assertTrue(game.gameCondition() == GameCondition.InPlay);
		game.setGameCondition(GameCondition.Completed);
		assertTrue(game.gameCondition() == GameCondition.Completed);
	}

}
