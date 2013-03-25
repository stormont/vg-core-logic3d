package com.voyagegames.logic3d;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.voyagegames.logic3d.core.Common.LogLevel;

@SuppressWarnings("serial")
public class RegisterServlet extends AbstractLoggingServlet {

	private static final String PATH = "/v1/register";
	private static final String TAG = RegisterServlet.class.getName();
	
	public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws IOException {
		try {
			logLevel = LogLevel.INFO;
			log(TAG, PATH + " POST");
			
			final String name = req.getParameter("name");
			final String pass = req.getParameter("pass");
			
			if (name == null || pass == null) {
				final String error = "Bad request";
				final PrintWriter out = resp.getWriter();
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		        out.print(error);
	    		logLevel = LogLevel.WARNING;
				log(TAG, error);
				return;
			}

		    if (Users.isUserRegistered(name)) {
				final String error = "Request to register existing user";
				final PrintWriter out = resp.getWriter();
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		        out.print(error);
	    		logLevel = LogLevel.WARNING;
				log(TAG, error);
				return;
		    }

		    Users.registerUser(name, pass.getBytes());
			resp.setStatus(HttpServletResponse.SC_OK);
    		logLevel = LogLevel.WARNING;
			log(TAG, "Registered new user " + name);
		} catch (final Exception e) {
    		logLevel = LogLevel.ERROR;
    		log(TAG, PATH + " failed", e);
			final PrintWriter out = resp.getWriter();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        out.print("Server error");
    		logLevel = LogLevel.ERROR;
		}
	}
	
}
