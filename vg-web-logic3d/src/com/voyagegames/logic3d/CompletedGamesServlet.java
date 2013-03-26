package com.voyagegames.logic3d;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Entity;
import com.voyagegames.logic3d.core.Common;
import com.voyagegames.logic3d.core.Common.LogLevel;

@SuppressWarnings("serial")
public class CompletedGamesServlet extends AbstractLoggingServlet {

	private static final String PATH = "/v1/completed";
	private static final String TAG = CompletedGamesServlet.class.getName();
	
	public void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws IOException {
		try {
			logLevel = LogLevel.INFO;
			log(TAG, PATH + " GET");
			
			final String name = req.getParameter("name");
			
			if (!Common.isValid(name)) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Bad request");
				return;
			}

		    final HttpSession session = req.getSession();
		    final String user = (String)session.getAttribute("user");
		    
		    if (!name.equals(user)) {
				failureResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, TAG, "User " + name + " is not logged in");
				return;
		    }
			
    		logLevel = LogLevel.INFO;
			log(TAG, "User " + name + " requested completed games");
		    
		    final List<Entity> games = Games.getCompletedGames(name);
		    final StringBuilder json = new StringBuilder();
		    final int numGames = games.size();
		    
		    json.append("{\"completedgames\":[");
		    
		    for (int i = 0; i < numGames; ++i) {
		    	json.append(Games.buildJsonSummary(games.get(i)));
		    	
		    	if (i < (numGames - 1)) {
		    		json.append(",");
		    	}
		    }
		    
		    json.append("]}");

			final PrintWriter out = resp.getWriter();
			
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
	        out.print(json.toString());
		} catch (final Exception e) {
			serverError(resp, TAG, PATH + " failed", e);
		}
	}
	
}
