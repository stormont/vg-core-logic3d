package com.voyagegames.logic3d;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.voyagegames.logic3d.core.Common;
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
			
			if (!Common.isValid(name) || !Common.isValid(pass)) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Bad request");
				return;
			}

		    if (Users.getUser(name) != null) {
				failureResponse(resp, HttpServletResponse.SC_BAD_REQUEST, TAG, "Request to register existing user " + name);
				return;
		    }

			final byte[] encrypted = Encryption.encrypt("Logic3D", pass.getBytes());

		    Users.registerUser(name, encrypted);
			resp.setStatus(HttpServletResponse.SC_OK);
			
    		logLevel = LogLevel.INFO;
			log(TAG, "Registered new user " + name);
		} catch (final Exception e) {
			serverError(resp, TAG, PATH + " failed", e);
		}
	}
	
}
