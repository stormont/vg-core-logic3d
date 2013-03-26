package com.voyagegames.logic3d.controllers;

import java.util.ArrayList;
import java.util.List;

import com.voyagegames.logic3d.models.Common;
import com.voyagegames.logic3d.models.Index;
import com.voyagegames.logic3d.models.PieceIndex;
import com.voyagegames.logic3d.models.Player;
import com.voyagegames.logic3d.models.Solution;

public class SolutionSearch {
	
	public static Solution checkForSolution(final List<PieceIndex> pieces, final int gameSize) {
		for (final PieceIndex pieceIndex : pieces) {
			if (pieceIndex.player == Player.None) {
				continue;
			}
			
			final List<List<Index>> indices = solutionIndicesForIndex(pieceIndex.index, gameSize);
			
			for (final List<Index> list : indices) {
				if (list.size() < Common.SolutionSize) {
					continue;
				}
				
				boolean solved = true;
				
				for (final Index index : list) {
					final int location = index.toArrayLocation(gameSize);
					
					if (pieces.get(location).player != pieceIndex.player) {
						solved = false;
						break;
					}
				}
				
				if (solved) {
					return new Solution(list, pieceIndex.player);
				}
			}
		}
		
		return null;
	}
	
	private static List<List<Index>> solutionIndicesForIndex(final Index index, final int gameSize) {
		final List<List<Index>> results = new ArrayList<List<Index>>();
		
		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				for (int k = -1; k <= 1; ++k) {
					if (i == 0 && j == 0 && k == 0) {
						continue;
					}
					
					final List<Index> list = new ArrayList<Index>();
					
					for (int c = 0; c < Common.SolutionSize; ++c) {
						final int x = index.x + (i * c);
						final int y = index.y + (j * c);
						final int z = index.z + (k * c);
						
						if (
								x < 0 || x >= gameSize ||
								y < 0 || z >= gameSize ||
								z < 0 || y >= gameSize) {
							continue;
						}
						
						list.add(new Index(x,y,z));
					}
					
					if (list.size() >= Common.SolutionSize) {
						results.add(list);
					}
				}
			}
		}
		
		return results;
	}

}
