package com.voyagegames.logic3d.models.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.voyagegames.logic3d.models.Index;
import com.voyagegames.logic3d.models.PieceIndex;
import com.voyagegames.logic3d.models.Player;

public class PieceIndexTest {

	@Test
	public void testPieceIndex_successful() {
		final Index index = new Index(1,2,3);
		final PieceIndex pieceIndex = new PieceIndex(index, Player.One);
		assertTrue(pieceIndex.index == index);
		assertTrue(pieceIndex.player == Player.One);
	}

}
