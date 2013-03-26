package com.voyagegames.logic3d;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Entity;
import com.voyagegames.logic3d.controllers.Game;
import com.voyagegames.logic3d.core.Common;
import com.voyagegames.logic3d.core.Common.LogLevel;
import com.voyagegames.logic3d.models.GameConfig;
import com.voyagegames.logic3d.models.GameState;
import com.voyagegames.logic3d.models.StringLookup;

@SuppressWarnings("serial")
public class StartServlet extends AbstractLoggingServlet {

	private static final String PATH = "/v1/start";
	private static final String TAG = StartServlet.class.getName();
	private static final int NUM_PLAYERS = 2;
	private static final int MAX_SOLUTION_SIZE = com.voyagegames.logic3d.models.Common.SolutionSize * 2;
	private static final String LANGUAGE = "en";
	
	public void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws IOException {
		try {
			logLevel = LogLevel.INFO;
			log(TAG, PATH + " POST");

			final String player1 = req.getParameter("p1");
			final String player2 = req.getParameter("p2");
			final String sizeStr = req.getParameter("s");
			
			if (!Common.isValid(player1) || !Common.isValid(player2) || !Common.isValid(sizeStr)) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Bad request");
				return;
			}
			
			int size;
			
			try {
				size = Integer.parseInt(sizeStr);
			} catch (final NumberFormatException e) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Invalid size " + sizeStr);
				return;
			}
			
			if (size < com.voyagegames.logic3d.models.Common.SolutionSize || size > MAX_SOLUTION_SIZE) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Invalid size " + sizeStr);
				return;
			}

		    final HttpSession session = req.getSession();
		    final String user = (String)session.getAttribute("user");
		    
		    if (!player1.equals(user)) {
				failureResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, TAG, "User " + player1 + " is not logged in");
				return;
		    }
		    
		    final List<Entity> gamesInProgress = Games.getGamesInProgress(player1, player2);
		    
		    if (gamesInProgress.size() > 0) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Game is already in progress");
				return;
		    }
			
		    final String language = LANGUAGE;
		    final StringLookup strings = Common.getStringLookup(language);
		    
		    if (strings == null) {
				failureResponse(
						resp,
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						TAG,
						"An unknown language was specified: " + language);
				return;
		    }
		    
		    final GameConfig config = new GameConfig(size, NUM_PLAYERS);
		    final GameState gameState = new GameState(config, strings);
		    final Game game = new Game(gameState);
		    
		    String playerTurn = null;
		    
		    switch (game.gameState.playerTurn()) {
		    case One:
		    	playerTurn = player1;
		    	break;
		    case Two:
		    	playerTurn = player2;
		    	break;
	    	default:
				failureResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, TAG, "An invalid player turn was set");
	    		return;
		    }
		    
		    final Entity result = Games.startGame(player1, player2, config, playerTurn);
		    
		    if (result == null) {
				failureResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, TAG, "Failed to start game");
				return;
		    }
		    
			// TODO Return game ID and state of play
			failureResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, TAG, "Not implemented");
		} catch (final Exception e) {
			serverError(resp, TAG, PATH + " failed", e);
		}
	}
	
}
