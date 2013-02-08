package com.voyagegames.java.logic3d.controllers;

import com.voyagegames.java.logic3d.models.GameCondition;
import com.voyagegames.java.logic3d.models.GameState;
import com.voyagegames.java.logic3d.models.PieceIndex;
import com.voyagegames.java.logic3d.models.Player;
import com.voyagegames.java.logic3d.models.Solution;

public class Game {
	
	public final GameState gameState;
	
	public Game(final GameState gameState) {
		this.gameState = gameState;
	}
	
	public void setPiece(final PieceIndex pieceIndex) {
		if (gameState.gameCondition() == GameCondition.Completed) {
			throw new IllegalStateException("Game has already completed");
		}
		
		if (pieceIndex.player != gameState.playerTurn()) {
			throw new IllegalArgumentException("It is Player " + gameState.playerTurn().toString() + "'s turn");
		}
		
		final int location = pieceIndex.index.toArrayLocation(gameState.config.size);
		
		if (gameState.pieces.get(location).player != Player.None) {
			throw new IllegalArgumentException("Piece has already been placed at this index (" +
					pieceIndex.index.x + "," +
					pieceIndex.index.y + "," +
					pieceIndex.index.z + ")");
		}
		
		gameState.pieces.set(location, pieceIndex);
		
		final int playerIndex = pieceIndex.player.ordinal();
		
		if (playerIndex == gameState.config.numPlayers) {
			gameState.setPlayerTurn(Player.One);
		} else {
			gameState.setPlayerTurn(Player.values()[playerIndex + 1]);
		}
		
		final Solution solution = SolutionSearch.checkForSolution(gameState.pieces, gameState.config.size);
		
		if (solution != null) {
			gameState.setSolution(solution);
			gameState.setGameCondition(GameCondition.Completed);
		}
	}

}
