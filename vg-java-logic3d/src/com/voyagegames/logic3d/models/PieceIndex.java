package com.voyagegames.logic3d.models;

import java.io.Serializable;

public class PieceIndex implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6685484274676851384L;
	private Index index;
	private Player player;
	
	@SuppressWarnings("unused")
	private PieceIndex() {
		// no-op
	}
	
	public PieceIndex(final Index index, final Player player) {
		this.index = index;
		this.player = player;
	}
	
	public Index index() {
		return this.index;
	}
	
	public Player player() {
		return this.player;
	}

}
