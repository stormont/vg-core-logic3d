package com.voyagegames.java.logic3d.controllers.tests;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.voyagegames.java.logic3d.controllers.Game;
import com.voyagegames.java.logic3d.controllers.SolutionSearch;
import com.voyagegames.java.logic3d.models.ActionResult;
import com.voyagegames.java.logic3d.models.Common;
import com.voyagegames.java.logic3d.models.GameConfig;
import com.voyagegames.java.logic3d.models.GameState;
import com.voyagegames.java.logic3d.models.Index;
import com.voyagegames.java.logic3d.models.PieceIndex;
import com.voyagegames.java.logic3d.models.Player;
import com.voyagegames.java.logic3d.models.StringLookup;

public class SolutionSearchTest {
	
	private StringLookup strings;
	private GameConfig config;
	private GameState gameState;

	@Before
	public void setUp() throws Exception {
		final Map<String, String> map = new HashMap<String, String>();

		final ActionResult[] results = ActionResult.values();
		
		for (final ActionResult r : results) {
			map.put(r.toString(), r.toString() + "_value");
		}
		
		strings = new StringLookup("en", map);
		config = new GameConfig(Common.SolutionSize, Common.MinimumPlayers);
		gameState = new GameState(config, strings);
	}

	@Test
	public void testCheckForSolution() {
		final Game game = new Game(gameState);

		assertTrue(SolutionSearch.checkForSolution(gameState.pieces, config.size) == null);
		game.setPiece(new PieceIndex(new Index(0,0,0), Player.One));
		assertTrue(SolutionSearch.checkForSolution(gameState.pieces, config.size) == null);
		game.setPiece(new PieceIndex(new Index(0,1,0), Player.Two));
		assertTrue(SolutionSearch.checkForSolution(gameState.pieces, config.size) == null);
		game.setPiece(new PieceIndex(new Index(1,0,0), Player.One));
		assertTrue(SolutionSearch.checkForSolution(gameState.pieces, config.size) == null);
		game.setPiece(new PieceIndex(new Index(1,1,0), Player.Two));
		assertTrue(SolutionSearch.checkForSolution(gameState.pieces, config.size) == null);
		game.setPiece(new PieceIndex(new Index(2,0,0), Player.One));
		assertTrue(SolutionSearch.checkForSolution(gameState.pieces, config.size) == null);
		game.setPiece(new PieceIndex(new Index(2,1,0), Player.Two));
		assertTrue(SolutionSearch.checkForSolution(gameState.pieces, config.size) == null);
		game.setPiece(new PieceIndex(new Index(3,0,0), Player.One));
		assertTrue(SolutionSearch.checkForSolution(gameState.pieces, config.size) != null);
	}

}
