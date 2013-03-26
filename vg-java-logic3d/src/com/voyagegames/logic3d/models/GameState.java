package com.voyagegames.logic3d.models;

import java.util.ArrayList;
import java.util.List;

public class GameState {
	
	public final GameConfig config;
	public final StringLookup strings;
	public final List<PieceIndex> pieces;
	
	private GameCondition mCondition;
	private Player mTurn;
	private Solution mSolution;
	
	public GameState(final GameConfig config, final StringLookup strings) {
		this.config = config;
		this.strings = strings;
		this.pieces = new ArrayList<PieceIndex>();
		this.mTurn = Player.One;
		this.mCondition = GameCondition.InPlay;
		this.mSolution = null;
		
		for (int i = 0; i < config.size; ++i) {
			for (int j = 0; j < config.size; ++j) {
				for (int k = 0; k < config.size; ++k) {
					this.pieces.add(new PieceIndex(new Index(i, j, k), Player.None));
				}
			}
		}
	}
	
	public GameCondition gameCondition() {
		return mCondition;
	}
	
	public Player playerTurn() {
		return mTurn;
	}
	
	public Solution solution() {
		return mSolution;
	}
	
	public void setSolution(final Solution solution) {
		if (mSolution == null) {
			mSolution = solution;
		} else {
			throw new IllegalStateException("Solution has already been set");
		}
	}
	
	public void setPlayerTurn(final Player player) {
		if (player.ordinal() > config.numPlayers) {
			throw new IllegalArgumentException("Game doesn't support " + player.ordinal() + " players");
		}
		
		mTurn = player;
	}
	
	public void setGameCondition(final GameCondition gameCondition) {
		mCondition = gameCondition;
	}

}
