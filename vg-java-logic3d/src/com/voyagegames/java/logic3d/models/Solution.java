package com.voyagegames.java.logic3d.models;

import java.util.List;

public class Solution {
	
	public final List<Index> indices;
	public final Player player;
	
	public Solution(final List<Index> indices, final Player player) {
		this.indices = indices;
		this.player = player;
		
		if (indices.size() < Common.SolutionSize){
			throw new IllegalArgumentException("Solution size (" + indices.size() + ") is below the minimum (" + Common.SolutionSize + ")");
		}
	}

}
