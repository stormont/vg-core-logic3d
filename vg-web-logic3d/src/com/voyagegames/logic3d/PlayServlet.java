package com.voyagegames.logic3d;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.voyagegames.logic3d.controllers.Game;
import com.voyagegames.logic3d.core.Common;
import com.voyagegames.logic3d.core.Common.LogLevel;
import com.voyagegames.logic3d.models.GameCondition;
import com.voyagegames.logic3d.models.GameConfig;
import com.voyagegames.logic3d.models.GameState;
import com.voyagegames.logic3d.models.Index;
import com.voyagegames.logic3d.models.PieceIndex;
import com.voyagegames.logic3d.models.Player;
import com.voyagegames.logic3d.models.StringLookup;

@SuppressWarnings("serial")
public class PlayServlet extends AbstractLoggingServlet {

	private static final String PATH = "/v1/play";
	private static final String TAG = PlayServlet.class.getName();
	
	public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws IOException {
		try {
			logLevel = LogLevel.INFO;
			log(TAG, PATH + " POST");

			final String player = req.getParameter("p");
			final String gameStr = req.getParameter("g");
			final String xStr = req.getParameter("x");
			final String yStr = req.getParameter("y");
			final String zStr = req.getParameter("z");
			
			if (
					!Common.isValid(player) ||
					!Common.isValid(gameStr) ||
					!Common.isValid(xStr) ||
					!Common.isValid(yStr) ||
					!Common.isValid(zStr)) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Bad request");
				return;
			}
			
			int x;
			int y;
			int z;
			long gameID;
			
			try {
				x = Integer.parseInt(xStr);
				y = Integer.parseInt(yStr);
				z = Integer.parseInt(zStr);
				gameID = Long.parseLong(gameStr);
			} catch (final NumberFormatException e) {
				failureResponse(
						resp,
						HttpServletResponse.SC_BAD_REQUEST,
						TAG,
						"Invalid index: " + xStr + ", " + yStr + ", " + zStr);
				return;
			}

		    final HttpSession session = req.getSession();
		    final String user = (String)session.getAttribute("user");
		    
		    if (!player.equalsIgnoreCase(user)) {
				failureResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, TAG, "User " + player + " is not logged in");
				return;
		    }

    		logLevel = LogLevel.INFO;
			log(TAG, "User " + player + " requested play for game " + gameID);
		    
		    final List<GameModel> gamesInProgress = Games.getGamesInProgress(player);
		    
		    GameModel game = null;
		    
		    for (final GameModel e : gamesInProgress) {
		    	if (e.id() == gameID) {
		    		game = e;
		    		break;
		    	}
		    }
		    
		    if (game == null) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Game does not exist or has been completed");
				return;
		    }
		    
		    final GameConfig config = game.config();
			
			if (
					x < 0 || x >= config.size ||
					y < 0 || y >= config.size ||
					z < 0 || z >= config.size) {
				failureResponse(
						resp,
						HttpServletResponse.SC_BAD_REQUEST,
						TAG,
						"Invalid game index: " + xStr + ", " + yStr + ", " + zStr);
				return;
			}
			
			if (!game.turn().equalsIgnoreCase(player)) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Not player's turn: " + player);
				return;
			}
			
			final List<PieceIndex> pieces = game.pieces();
			
			for (final PieceIndex pi : pieces) {
				if (
						pi.index().x == x &&
						pi.index().y == y &&
						pi.index().z == z) {
					failureResponse(
							resp,
							HttpServletResponse.SC_BAD_REQUEST,
							TAG,
							"Index already occupied: " + xStr + ", " + yStr + ", " + zStr);
					return;
				}
			}
			
		    final String language = "en";
		    final StringLookup strings = Common.getStringLookup(language);
		    
		    if (strings == null) {
				failureResponse(
						resp,
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						TAG,
						"An unknown language was specified: " + language);
				return;
		    }
		    
		    final GameState gameState = new GameState(config, strings);
		    final Game gameSnapshot = new Game(gameState);

			for (final PieceIndex pi : pieces) {
				gameState.setPlayerTurn(pi.player());
				gameSnapshot.setPiece(pi);
			}
			
			PieceIndex newPiece = null;
			
			if (game.player1().equalsIgnoreCase(game.turn())) {
				newPiece = new PieceIndex(new Index(x, y, z), Player.One);
			} else {
				newPiece = new PieceIndex(new Index(x, y, z), Player.Two);
			}
			
			gameState.setPlayerTurn(newPiece.player());
			gameSnapshot.setPiece(newPiece);
			Games.addPiece(game, newPiece);
			
			if (gameSnapshot.gameState.gameCondition() == GameCondition.Completed) {
				Games.completeGame(game);
			}

			final PrintWriter out = resp.getWriter();
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
	        out.print(Games.buildJsonSummary(game));
		} catch (final Exception e) {
			serverError(resp, TAG, PATH + " failed", e);
		}
	}
	
}
