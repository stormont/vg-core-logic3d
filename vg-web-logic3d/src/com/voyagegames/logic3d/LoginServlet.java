package com.voyagegames.logic3d;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.voyagegames.logic3d.core.Common;
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
			
			if (!Common.isValid(name) || !Common.isValid(pass)) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Bad request");
				return;
			}
			
			final byte[] encrypted = Encryption.encrypt("Logic3D", pass.getBytes());

		    if (!Users.loginUser(name, encrypted)) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "User login failed");
				return;
		    }

		    final HttpSession session = req.getSession();
		    session.setAttribute("user", name);
		    session.setAttribute("context", new Date());
			resp.setStatus(HttpServletResponse.SC_OK);
			
    		logLevel = LogLevel.INFO;
			log(TAG, "User " + name + " logged in");
		} catch (final Exception e) {
			serverError(resp, TAG, PATH + " failed", e);
		}
	}
	
}
