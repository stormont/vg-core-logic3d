package com.voyagegames.logic3d;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.voyagegames.logic3d.core.Common.LogLevel;
import com.voyagegames.logic3d.core.ILogger;

public abstract class AbstractLoggingServlet extends HttpServlet implements ILogger {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6163182523378481834L;
	private static final Logger LOG = Logger.getLogger(AbstractLoggingServlet.class.getName());

	protected LogLevel logLevel;

	@Override
	public void log(final String tag, final String msg) {
		switch (logLevel) {
		case DEBUG:
			LOG.info(tag + " - " + msg);
			break;
		case INFO:
			LOG.info(tag + " - " + msg);
			break;
		case WARNING:
			LOG.warning(tag + " - " + msg);
			break;
		case ERROR:
			LOG.severe(tag + " - " + msg);
			break;
		case CRITICAL:
			LOG.severe(tag + " - " + msg);
			break;
		}
	}

	@Override
	public void log(final String tag, final String msg, final Exception e) {
		switch (logLevel) {
		case DEBUG:
			LOG.info(tag + " - " + msg);
			LOG.info(e.getMessage());
			break;
		case INFO:
			LOG.info(tag + " - " + msg);
			LOG.info(e.getMessage());
			break;
		case WARNING:
			LOG.warning(tag + " - " + msg);
			LOG.warning(e.getMessage());
			break;
		case ERROR:
			LOG.severe(tag + " - " + msg);
			LOG.severe(e.getMessage());
			break;
		case CRITICAL:
			LOG.severe(tag + " - " + msg);
			LOG.severe(e.getMessage());
			break;
		}

		e.printStackTrace();
	}
	
	public void failureResponse(
			final HttpServletResponse resp,
			final int responseCode,
			final String tag,
			final String errorMsg) {
		try {
			final PrintWriter out = resp.getWriter();
			resp.setStatus(responseCode);
	        out.print(errorMsg);
		} catch (final IOException e) {
			logLevel = LogLevel.ERROR;
			log(tag, e.getMessage(), e);
		}
		
		logLevel = LogLevel.WARNING;
		log(tag, errorMsg);
	}
	
	public void serverError(
			final HttpServletResponse resp,
			final String tag,
			final String errorMsg,
			final Exception exc) {
		logLevel = LogLevel.ERROR;
		log(tag, errorMsg, exc);
		
		try {
			final PrintWriter out = resp.getWriter();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        out.print("Server error");
		} catch (final IOException e) {
			logLevel = LogLevel.ERROR;
			log(tag, e.getMessage(), e);
		}
	}
	
}
