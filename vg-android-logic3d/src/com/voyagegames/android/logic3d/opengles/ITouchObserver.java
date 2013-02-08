package com.voyagegames.android.logic3d.opengles;

public interface ITouchObserver {
	
	public void onTouchBegin(float x, float y);
	public void onTouchMoved(float x, float y);
	public void onTouchEnd(float x, float y);

}
