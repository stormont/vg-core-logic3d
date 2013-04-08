package com.voyagegames.logic3d.models;

import java.io.Serializable;

public class GameConfig implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7772546748160144629L;
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
