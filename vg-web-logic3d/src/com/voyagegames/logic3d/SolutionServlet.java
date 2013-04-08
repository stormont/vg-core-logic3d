package com.voyagegames.logic3d;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.voyagegames.logic3d.controllers.SolutionSearch;
import com.voyagegames.logic3d.core.Common;
import com.voyagegames.logic3d.core.Common.LogLevel;
import com.voyagegames.logic3d.models.GameConfig;
import com.voyagegames.logic3d.models.Index;
import com.voyagegames.logic3d.models.PieceIndex;
import com.voyagegames.logic3d.models.Solution;

@SuppressWarnings("serial")
public class SolutionServlet extends AbstractLoggingServlet {

	private static final String PATH = "/v1/solution";
	private static final String TAG = SolutionServlet.class.getName();
	
	public void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws IOException {
		try {
			logLevel = LogLevel.INFO;
			log(TAG, PATH + " GET");

			final String player = req.getParameter("p");
			final String gameStr = req.getParameter("g");
			
			if (!Common.isValid(player) || !Common.isValid(gameStr)) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Bad request");
				return;
			}
			
			long gameID;
			
			try {
				gameID = Long.parseLong(gameStr);
			} catch (final NumberFormatException e) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Invalid game ID " + gameStr);
				return;
			}

		    final HttpSession session = req.getSession();
		    final String user = (String)session.getAttribute("user");
		    
		    if (!player.equals(user)) {
				failureResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, TAG, "User " + player + " is not logged in");
				return;
		    }
		    
    		logLevel = LogLevel.INFO;
			log(TAG, "User " + player + " requested solution to " + gameID);
		    
		    final List<GameModel> gamesInProgress = Games.getGames(player);
		    
		    GameModel game = null;
		    
		    for (final GameModel e : gamesInProgress) {
		    	if (e.id() == gameID) {
		    		game = e;
		    		break;
		    	}
		    }
		    
		    if (game == null) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Game does not exist");
				return;
		    }
		    
		    if (!game.completed()) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Game is still in progress");
				return;
		    }

		    final GameConfig config = game.config();
			final List<PieceIndex> pieces = game.pieces();
			final Solution solution = SolutionSearch.checkForSolution(pieces, config.size);
		    
		    if (solution == null) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Game solution does not exist");
				return;
		    }
		    
		    final StringBuilder json = new StringBuilder();
		    
		    json.append("{\"winner\":" + solution.player.ordinal());
		    json.append(",\"solution\":[");

		    for (int i = 0; i < solution.indices.size(); ++i) {
		    	final Index index = solution.indices.get(i);
		    	
		    	json.append("{\"x\":" + index.x);
		    	json.append(",\"y\":" + index.y);
		    	json.append(",\"z\":" + index.z);
		    	json.append("}");
		    	
		    	if (i < solution.indices.size() - 1) {
		    		json.append(",");
		    	}
		    }
		    
		    json.append("]}");

			final PrintWriter out = resp.getWriter();
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
	        out.print(json);
		} catch (final Exception e) {
			serverError(resp, TAG, PATH + " failed", e);
		}
	}
	
}
