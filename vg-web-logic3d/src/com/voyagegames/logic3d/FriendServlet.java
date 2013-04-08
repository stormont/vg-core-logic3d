package com.voyagegames.logic3d;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Entity;
import com.voyagegames.logic3d.core.Common;
import com.voyagegames.logic3d.core.Common.LogLevel;

@SuppressWarnings("serial")
public class FriendServlet extends AbstractLoggingServlet {

	private static final String PATH = "/v1/friend";
	private static final String TAG = FriendServlet.class.getName();
	
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
		    
		    if (!name.equalsIgnoreCase(user)) {
				failureResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, TAG, "User " + name + " is not logged in");
				return;
		    }
		    
		    final Entity userEntity = Users.getUser(name);
			final PrintWriter out = resp.getWriter();
			
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
	        out.print(Users.buildJsonFriends(userEntity));
		} catch (final Exception e) {
			serverError(resp, TAG, PATH + " failed", e);
		}
	}
	
	public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws IOException {
		try {
			logLevel = LogLevel.INFO;
			log(TAG, PATH + " POST");
			
			final String name = req.getParameter("name");
			final String friend = req.getParameter("friend");
			
			if (!Common.isValid(name) || !Common.isValid(friend)) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Bad request");
				return;
			}

		    final HttpSession session = req.getSession();
		    final String user = (String)session.getAttribute("user");
		    
		    if (!name.equalsIgnoreCase(user)) {
				failureResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, TAG, "User " + name + " is not logged in");
				return;
		    }
		    
		    final Entity userEntity = Users.getUser(name);
		    
		    if (userEntity == null) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Unknown user " + name);
				return;
		    }

		    final Entity friendEntity = Users.getUser(friend);
		    
		    if (friendEntity == null) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Unknown user " + friend);
				return;
		    }

		    if (!Users.addFriend(userEntity, friendEntity)) {
				failureResponse(
						resp,
						HttpServletResponse.SC_BAD_REQUEST,
						TAG,
						"Failed to add friend " + friend + " to user " + name);
				return;
		    }
		    
			resp.setStatus(HttpServletResponse.SC_OK);
			
    		logLevel = LogLevel.INFO;
			log(TAG, "User " + name + " added friend " + friend);
		} catch (final Exception e) {
			serverError(resp, TAG, PATH + " failed", e);
		}
	}
	
}
