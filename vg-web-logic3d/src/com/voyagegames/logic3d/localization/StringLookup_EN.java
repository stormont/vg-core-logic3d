package com.voyagegames.logic3d.localization;

import java.util.HashMap;
import java.util.Map;

import com.voyagegames.logic3d.models.ActionResult;
import com.voyagegames.logic3d.models.StringLookup;

public class StringLookup_EN extends StringLookup {

	public static final String LANGUAGE = "en";
	
	private static Map<String, String> getMap() {
		final Map<String, String> map = new HashMap<String, String>();

		map.put(ActionResult.Success.toString(), "Successful action");
		map.put(ActionResult.GameSizeBelowMinimum.toString(), "The size of the game is below the minimum");
		map.put(ActionResult.GamePlayersBelowMinimum.toString(), "Number of players is below the minimum");
		map.put(ActionResult.InvalidPieceIndex.toString(), "An invalid piece index was specified");
		map.put(ActionResult.InvalidPlayerSpecified.toString(), "An invalid player was specified");
		map.put(ActionResult.NotPlayerTurn.toString(), "It is not this player's turn");
		map.put(ActionResult.GameAlreadyCompleted.toString(), "The game has already been completed");
		
		return map;
	}
	
	public StringLookup_EN() {
		super(LANGUAGE, getMap());
	}

}
