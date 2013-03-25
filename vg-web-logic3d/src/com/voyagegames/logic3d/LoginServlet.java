package com.voyagegames.logic3d;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.voyagegames.logic3d.core.Common.LogLevel;

@SuppressWarnings("serial")
public class LoginServlet extends AbstractLoggingServlet {

	private static final String PATH = "/v1/login";
	private static final String TAG = LoginServlet.class.getName();
	
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

		    if (!Users.loginUser(name, pass.getBytes())) {
				final String error = "User login failed";
				final PrintWriter out = resp.getWriter();
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		        out.print(error);
	    		logLevel = LogLevel.WARNING;
				log(TAG, error);
				return;
		    }

		    final HttpSession session = req.getSession();
		    session.setAttribute("user", name);
		    session.setAttribute("context", new Date());
			resp.setStatus(HttpServletResponse.SC_OK);
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
