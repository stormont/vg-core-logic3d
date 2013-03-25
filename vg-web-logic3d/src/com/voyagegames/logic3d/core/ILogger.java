package com.voyagegames.logic3d.core;

public interface ILogger {
	
	public void log(String tag, String msg);
	public void log(String tag, String msg, Exception e);

}
