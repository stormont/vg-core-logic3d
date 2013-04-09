package com.voyagegames.android.logic3d.opengles;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

import com.voyagegames.core.android.opengles.interfaces.IScene;
import com.voyagegames.core.android.opengles.interfaces.IShaderSet;
import com.voyagegames.core.android.opengles.modules.Frustum;
import com.voyagegames.core.android.opengles.modules.Light;
import com.voyagegames.core.android.opengles.modules.LookAt;
import com.voyagegames.core.android.opengles.modules.OrbitCamera;
import com.voyagegames.core.android.opengles.modules.Vector3D;

public class GameRenderer implements Renderer, ITouchObserver {
	
	private static final float TOUCH_SCALE_FACTOR = 0.1f;
	private static final float SPACING = 2.0f;
	private static final int GAME_SIZE = 4;

    private final IScene mScene;
    private final Light mLight;
    private final IShaderSet mGamePieceShaderSet;
    private final List<GamePiece> mGamePieces;
    private final List<GamePiece> mPlaceholderGamePieces;
    private final Context mContext;
    
    private OrbitCamera mCamera;
	private float mLastTouchX;
	private float mLastTouchY;

    public GameRenderer(final Context context) {
        mScene = new GameScene();
    	mLight = new Light();
    	mGamePieceShaderSet = new GamePieceShaderSet();
    	mGamePieces = new ArrayList<GamePiece>();
    	mPlaceholderGamePieces = new ArrayList<GamePiece>();
    	mContext = context;
    	
    	initEntities();
    }
    
    private void initEntities() {
        final float offset = SPACING * (GAME_SIZE / 2) - (SPACING / 2.0f);
    	
    	GamePiece gamePiece = new GreenGamePiece(mContext);
    	gamePiece.setPosition(new Vector3D(-offset, -offset, -offset));
    	gamePiece.setPositionIndex(new Position(0, 0, 0));
    	mGamePieces.add(gamePiece);
    	
    	gamePiece = new RedGamePiece(mContext);
    	gamePiece.setPosition(new Vector3D(SPACING - offset, -offset, -offset));
    	gamePiece.setPositionIndex(new Position(1, 0, 0));
    	mGamePieces.add(gamePiece);
    	
    	for (int i = 0; i < GAME_SIZE; ++i) {
    		for (int j = 0; j < GAME_SIZE; ++j) {
    			for (int k = 0; k < GAME_SIZE; ++k) {
    		    	gamePiece = new GreyGamePiece(mContext);
    		    	gamePiece.setPosition(new Vector3D(SPACING * i - offset, SPACING * j - offset, SPACING * k - offset));
    		    	gamePiece.setPositionIndex(new Position(i, j, k));
    		    	mPlaceholderGamePieces.add(gamePiece);
    			}
    		}
    	}
    	
    	for (final GamePiece gp : mGamePieces) {
    		final Position gpPosition = gp.positionIndex();
    		
    		GamePiece match = null;
    		
    		for (final GamePiece pgp : mPlaceholderGamePieces) {
    			final Position pgpPosition = pgp.positionIndex();
    			
    			if (
    					gpPosition.x == pgpPosition.x &&
    					gpPosition.y == pgpPosition.y &&
    					gpPosition.z == pgpPosition.z) {
    				match = pgp;
    				break;
    			}
    		}
    		
    		if (match != null) {
    			mPlaceholderGamePieces.remove(match);
    		}
    	}
    }
    
	@Override
    public void onDrawFrame(final GL10 glUnused) {
    	mScene.clear();
        
        final Vector3D eye = mCamera.lookAt().eye;
        final float[] lightModelMatrix = mLight.modelMatrix();

        Matrix.translateM(lightModelMatrix, 0, eye.x, eye.y, eye.z);

        final float[] lightPosInEyeSpace =
        		mLight.positionInEyeSpace(mScene.camera().viewMatrix(), mLight.positionInWorldSpace(lightModelMatrix));

        // Draw the game pieces
        mGamePieceShaderSet.prepareRender();
        
		GLES20.glDisable(GLES20.GL_BLEND);

        for (final GamePiece gamePiece : mGamePieces) {
        	gamePiece.onDrawFrame(mScene.camera(), lightPosInEyeSpace);
        }
        
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        for (final GamePiece gamePiece : mPlaceholderGamePieces) {
        	gamePiece.onDrawFrame(mScene.camera(), lightPosInEyeSpace);
        }
	}

	@Override
    public void onSurfaceChanged(final GL10 glUnused, final int width, final int height) {
        mScene.updateViewport(width, height);
        
        for (final GamePiece gamePiece : mGamePieces) {
        	gamePiece.onSurfaceChanged((GamePieceShaderSet)mGamePieceShaderSet);
        }
        
        for (final GamePiece gamePiece : mPlaceholderGamePieces) {
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
