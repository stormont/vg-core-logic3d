package com.voyagegames.logic3d;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.voyagegames.logic3d.core.Common.LogLevel;

@SuppressWarnings("serial")
public class PlayServlet extends AbstractLoggingServlet {

	private static final String PATH = "/v1/play";
	private static final String TAG = PlayServlet.class.getName();
	
	public void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws IOException {
		try {
			logLevel = LogLevel.INFO;
			log(TAG, PATH + " POST");

			// TODO
			failureResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, TAG, "Not implemented");
		} catch (final Exception e) {
			serverError(resp, TAG, PATH + " failed", e);
		}
	}
	
}
