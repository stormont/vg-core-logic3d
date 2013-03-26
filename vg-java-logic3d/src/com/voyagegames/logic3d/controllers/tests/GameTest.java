package com.voyagegames.logic3d.controllers.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.voyagegames.logic3d.controllers.Game;
import com.voyagegames.logic3d.models.ActionResult;
import com.voyagegames.logic3d.models.Common;
import com.voyagegames.logic3d.models.GameCondition;
import com.voyagegames.logic3d.models.GameConfig;
import com.voyagegames.logic3d.models.GameState;
import com.voyagegames.logic3d.models.Index;
import com.voyagegames.logic3d.models.PieceIndex;
import com.voyagegames.logic3d.models.Player;
import com.voyagegames.logic3d.models.StringLookup;

public class GameTest {
	
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
	public void testSetPiece_successful() {
		final Game game = new Game(gameState);
		final PieceIndex pieceIndex = new PieceIndex(new Index(1,2,3), Player.One);
		final int location = pieceIndex.index.toArrayLocation(config.size);

		assertTrue(gameState.pieces.get(location).player == Player.None);
		game.setPiece(pieceIndex);
		assertTrue(gameState.pieces.get(location).player == Player.One);
	}

	@Test
	public void testSetPiece_gameAlreadyCompleted() {
		final Game game = new Game(gameState);

		assertTrue(game.gameState.gameCondition() == GameCondition.InPlay);
		game.setPiece(new PieceIndex(new Index(0,0,0), Player.One));
		assertTrue(game.gameState.gameCondition() == GameCondition.InPlay);
		game.setPiece(new PieceIndex(new Index(0,1,0), Player.Two));
		assertTrue(game.gameState.gameCondition() == GameCondition.InPlay);
		game.setPiece(new PieceIndex(new Index(1,0,0), Player.One));
		assertTrue(game.gameState.gameCondition() == GameCondition.InPlay);
		game.setPiece(new PieceIndex(new Index(1,1,0), Player.Two));
		assertTrue(game.gameState.gameCondition() == GameCondition.InPlay);
		game.setPiece(new PieceIndex(new Index(2,0,0), Player.One));
		assertTrue(game.gameState.gameCondition() == GameCondition.InPlay);
		game.setPiece(new PieceIndex(new Index(2,1,0), Player.Two));
		assertTrue(game.gameState.gameCondition() == GameCondition.InPlay);
		game.setPiece(new PieceIndex(new Index(3,0,0), Player.One));
		assertTrue(game.gameState.gameCondition() == GameCondition.Completed);
		
		try {
			game.setPiece(new PieceIndex(new Index(3,1,0), Player.Two));
			fail("Shouldn't hit this");
		} catch (final IllegalStateException e) {
			// no-op; expected
			assertTrue(game.gameState.gameCondition() == GameCondition.Completed);
		}
	}

	@Test
	public void testSetPiece_wrongPlayerTurn() {
		final Game game = new Game(gameState);
		final PieceIndex pieceIndex = new PieceIndex(new Index(1,2,3), Player.Two);
		
		try {
			game.setPiece(pieceIndex);
			fail("Shouldn't hit this");
		} catch (final IllegalArgumentException e) {
			// no-op; expected
		}
	}

	@Test
	public void testSetPiece_pieceAlreadyPlacedAtLocation() {
		final Game game = new Game(gameState);
		final PieceIndex pieceIndex1 = new PieceIndex(new Index(1,2,3), Player.One);
		final PieceIndex pieceIndex2 = new PieceIndex(new Index(1,2,3), Player.Two);

		game.setPiece(pieceIndex1);
		
		try {
			game.setPiece(pieceIndex2);
			fail("Shouldn't hit this");
		} catch (final IllegalArgumentException e) {
			// no-op; expected
		}
	}

}
