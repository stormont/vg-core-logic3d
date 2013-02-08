package com.voyagegames.android.logic3d;

import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;

import com.voyagegames.android.logic3d.opengles.GameRenderer;
import com.voyagegames.core.android.opengles.AbstractGLActivity;

public class GameActivity extends AbstractGLActivity {
	
	private GameRenderer mRenderer;

	@Override
	public int openGLESVersion() {
		return 2;
	}

	@Override
	public Renderer renderer() {
		if (mRenderer == null) {
			mRenderer = new GameRenderer(this);
		}
		
		return mRenderer;
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mRenderer.onTouchBegin(event.getX(), event.getY());
			break;
		
		case MotionEvent.ACTION_MOVE:
			mRenderer.onTouchMoved(event.getX(), event.getY());
			break;
		
		case MotionEvent.ACTION_UP:
			mRenderer.onTouchEnd(event.getX(), event.getY());
			break;
		}
		
		return true;
	}

}
