package com.voyagegames.android.logic3d.opengles;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.voyagegames.core.android.opengles.buffers.CubeBuffer;
import com.voyagegames.core.android.opengles.interfaces.ICamera;
import com.voyagegames.core.android.opengles.modules.Vector3D;

public abstract class GamePiece {

    protected CubeBuffer mObject;
    
    private int[] mVertexBufferObjects;
    private GamePieceShaderSet mShaderSet;
    private Vector3D mPosition;
    private Position mPositionIndex;

    public GamePiece(final Context context) {
    	mPosition = new Vector3D(0f, 0f, 0f);
    	mPositionIndex = new Position(0, 0, 0);
    }
    
    public void setPosition(final Vector3D position) {
    	mPosition = position;
    }
    
    public void setPositionIndex(final Position position) {
    	mPositionIndex = position;
    }
    
    public Position positionIndex() {
    	return mPositionIndex;
    }
    
    public void onSurfaceChanged(final GamePieceShaderSet shaderSet) {
    	mShaderSet = shaderSet;
        mVertexBufferObjects = new int[6];

        GLES20.glGenBuffers(6, mVertexBufferObjects, 0);
    	mObject.setHandles(mShaderSet.handles());
        mObject.convertToVertexBufferObject(mVertexBufferObjects);
    }
    
    public void onDrawFrame(final ICamera camera, final float[] lightPosInEyeSpace) {
    	final float[] modelMatrix = new float[16];
    	
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, mPosition.x, mPosition.y, mPosition.z);
        mShaderSet.render(camera, mObject, modelMatrix, lightPosInEyeSpace);
    }
    
}
