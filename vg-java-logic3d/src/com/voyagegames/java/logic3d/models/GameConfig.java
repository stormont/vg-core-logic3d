package com.voyagegames.java.logic3d.models;

public class GameConfig {
	
	public final int size;
	public final int numPlayers;
	
	public GameConfig(final int size, final int numPlayers) {
		this.size = size;
		this.numPlayers = numPlayers;
		
		if (size < Common.SolutionSize){
			throw new IllegalArgumentException("Game size (" + size + ") is below the minimum (" + Common.SolutionSize + ")");
		}
		
		if (numPlayers < Common.MinimumPlayers) {
			throw new IllegalArgumentException("Number of players (" + numPlayers + ") is below the minimum (" + Common.MinimumPlayers + ")");
		}
	}

}
