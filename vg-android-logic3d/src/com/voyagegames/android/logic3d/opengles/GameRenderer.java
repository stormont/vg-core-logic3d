package com.voyagegames.android.logic3d.opengles;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.voyagegames.core.android.opengles.interfaces.IScene;
import com.voyagegames.core.android.opengles.interfaces.IShaderSet;
import com.voyagegames.core.android.opengles.modules.Frustum;
import com.voyagegames.core.android.opengles.modules.Light;
import com.voyagegames.core.android.opengles.modules.LookAt;
import com.voyagegames.core.android.opengles.modules.OrbitCamera;
import com.voyagegames.core.android.opengles.modules.Vector3D;

public class GameRenderer implements Renderer, ITouchObserver {
	
	private static final float TOUCH_SCALE_FACTOR = 0.1f;

    private final IScene mScene;
    private final Light mLight;
    private final IShaderSet mGamePieceShaderSet;
    private final List<GamePiece> mGamePieces;
    private final Context mContext;
    
    private OrbitCamera mCamera;
	private float mLastTouchX;
	private float mLastTouchY;

    public GameRenderer(final Context context) {
        mScene = new GameScene();
    	mLight = new Light();
    	mGamePieceShaderSet = new GamePieceShaderSet();
    	mGamePieces = new ArrayList<GamePiece>();
    	mContext = context;
    	
    	initEntities();
    }
    
    private void initEntities() {
    	mLight.setPosition(new Vector3D(0.0f, 0.0f, -5.0f));
    	mLight.setRotationAxis(new Vector3D(0.0f, 1.0f, 0.0f));
    	
    	GamePiece gamePiece = new GreenGamePiece(mContext);
    	gamePiece.setPosition(new Vector3D(0.0f, 0.0f, 0.0f));
    	mGamePieces.add(gamePiece);
    	
    	gamePiece = new RedGamePiece(mContext);
    	gamePiece.setPosition(new Vector3D(0.0f, 5.0f, 0.0f));
    	mGamePieces.add(gamePiece);
    }
    
	@Override
    public void onDrawFrame(final GL10 glUnused) {
    	mScene.clear();

		// Do a complete rotation every 5 seconds.
    	final int time = (int) (SystemClock.uptimeMillis() % 5000L);
		float angleInDegrees = (360.0f / 5000.0f) * time;
		float sinAngle = (90.0f / 5.0f / 5000.0f) * time;
        
        // Calculate position of the light. Rotate and then push into the distance.
		mLight.setRotationAngle(angleInDegrees);
    	mLight.setRotationAxis(new Vector3D((float) Math.sin(sinAngle), 1.0f, 0.0f));
        
        final float[] lightModelMatrix = mLight.modelMatrix();

        Matrix.translateM(lightModelMatrix, 0, 0.0f, 0.0f, 2.0f);

        final float[] lightPosInEyeSpace = mLight.positionInEyeSpace(mScene.camera().viewMatrix(), mLight.positionInWorldSpace(lightModelMatrix));

        // Draw the game pieces
        mGamePieceShaderSet.prepareRender();

        for (final GamePiece gamePiece : mGamePieces) {
        	gamePiece.onDrawFrame(mScene.camera(), lightPosInEyeSpace);
        }
	}

	@Override
    public void onSurfaceChanged(final GL10 glUnused, final int width, final int height) {
        mScene.updateViewport(width, height);
        
        for (final GamePiece gamePiece : mGamePieces) {
        	gamePiece.onSurfaceChanged((GamePieceShaderSet)mGamePieceShaderSet);
        }
	}

	@Override
    public void onSurfaceCreated(final GL10 glUnused, final EGLConfig config) {
    	mGamePieceShaderSet.create();

        final LookAt lookAt = new LookAt(new Vector3D(0, 0, 10.0f), new Vector3D(0, 0, 0.0f), new Vector3D(0, 1.0f, 0));
        final Frustum frustum = new Frustum(1.0f, -1.0f, 1.0f, 100.0f);
        
        mCamera = new OrbitCamera(lookAt, frustum);
        mScene.setCamera(mCamera);
	}

	@Override
	public void onTouchBegin(final float x, final float y) {
		mLastTouchX = x;
		mLastTouchY = y;
	}

	@Override
	public void onTouchMoved(final float x, final float y) {
        final float dx = mLastTouchX - x;
        final float dy = mLastTouchY - y;
        
        mCamera.addTheta(dx * TOUCH_SCALE_FACTOR);
        mCamera.addPhi(dy * TOUCH_SCALE_FACTOR);
        
		mLastTouchX = x;
		mLastTouchY = y;
	}

	@Override
	public void onTouchEnd(final float x, final float y) {
		// no-op
	}

}
