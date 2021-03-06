package com.voyagegames.android.logic3d.opengles;

import android.content.Context;
import android.opengl.GLES20;

import com.voyagegames.core.android.opengles.buffers.ColoredNormalTriangleBuffer;
import com.voyagegames.core.android.opengles.buffers.CubeBuffer;

public class GreyGamePiece extends GamePiece {
    
    private final float[] VERTICES_DATA_FRONT_FACE = {
			-0.5f, 0.5f, 0.5f,    0.0f, 0.0f, 1.0f,      1.0f, 1.0f, 1.0f, 0.5f,		
			-0.5f, -0.5f, 0.5f,   0.0f, 0.0f, 1.0f,	     1.0f, 1.0f, 1.0f, 0.5f,
			 0.5f, 0.5f, 0.5f,    0.0f, 0.0f, 1.0f,	     1.0f, 1.0f, 1.0f, 0.5f,
			 0.5f, -0.5f, 0.5f,   0.0f, 0.0f, 1.0f,	     1.0f, 1.0f, 1.0f, 0.5f,
    };

    private final float[] VERTICES_DATA_RIGHT_FACE = { 
			0.5f, 0.5f, 0.5f,     1.0f, 0.0f, 0.0f,      1.0f, 1.0f, 1.0f, 0.5f,
			0.5f, -0.5f, 0.5f,    1.0f, 0.0f, 0.0f,	     1.0f, 1.0f, 1.0f, 0.5f,
			0.5f, 0.5f, -0.5f,    1.0f, 0.0f, 0.0f,	     1.0f, 1.0f, 1.0f, 0.5f,
			0.5f, -0.5f, -0.5f,   1.0f, 0.0f, 0.0f,	     1.0f, 1.0f, 1.0f, 0.5f,
    };

    private final float[] VERTICES_DATA_BACK_FACE = {
			0.5f, 0.5f, -0.5f,	  0.0f, 0.0f, -1.0f,	 1.0f, 1.0f, 1.0f, 0.5f,
			0.5f, -0.5f, -0.5f,   0.0f, 0.0f, -1.0f,     1.0f, 1.0f, 1.0f, 0.5f,
			-0.5f, 0.5f, -0.5f,   0.0f, 0.0f, -1.0f,     1.0f, 1.0f, 1.0f, 0.5f,
			-0.5f, -0.5f, -0.5f,  0.0f, 0.0f, -1.0f,     1.0f, 1.0f, 1.0f, 0.5f,
    };

    private final float[] VERTICES_DATA_LEFT_FACE = { 
			-0.5f, 0.5f, -0.5f,	  -1.0f, 0.0f, 0.0f,	 1.0f, 1.0f, 1.0f, 0.5f,
			-0.5f, -0.5f, -0.5f,  -1.0f, 0.0f, 0.0f,     1.0f, 1.0f, 1.0f, 0.5f,
			-0.5f, 0.5f, 0.5f,    -1.0f, 0.0f, 0.0f,     1.0f, 1.0f, 1.0f, 0.5f,
			-0.5f, -0.5f, 0.5f,   -1.0f, 0.0f, 0.0f,     1.0f, 1.0f, 1.0f, 0.5f,
    };

    private final float[] VERTICES_DATA_TOP_FACE = { 
			-0.5f, 0.5f, -0.5f,	  0.0f, 1.0f, 0.0f,	     1.0f, 1.0f, 1.0f, 0.5f,
			-0.5f, 0.5f, 0.5f,    0.0f, 1.0f, 0.0f,	     1.0f, 1.0f, 1.0f, 0.5f,
			0.5f, 0.5f, -0.5f,    0.0f, 1.0f, 0.0f,	     1.0f, 1.0f, 1.0f, 0.5f,
			0.5f, 0.5f, 0.5f,     0.0f, 1.0f, 0.0f,	     1.0f, 1.0f, 1.0f, 0.5f,
    };

    private final float[] VERTICES_DATA_BOTTOM_FACE = { 
			0.5f, -0.5f, -0.5f,	  0.0f, -1.0f, 0.0f,	 1.0f, 1.0f, 1.0f, 0.5f,
			0.5f, -0.5f, 0.5f,    0.0f, -1.0f, 0.0f,     1.0f, 1.0f, 1.0f, 0.5f,
			-0.5f, -0.5f, -0.5f,  0.0f, -1.0f, 0.0f,     1.0f, 1.0f, 1.0f, 0.5f,
			-0.5f, -0.5f, 0.5f,   0.0f, -1.0f, 0.0f,     1.0f, 1.0f, 1.0f, 0.5f,
    };

    public GreyGamePiece(final Context context) {
    	super(context);
    	mObject = new CubeBuffer(
        		new ColoredNormalTriangleBuffer(VERTICES_DATA_FRONT_FACE, GLES20.GL_TRIANGLE_STRIP),
        		new ColoredNormalTriangleBuffer(VERTICES_DATA_RIGHT_FACE, GLES20.GL_TRIANGLE_STRIP),
        		new ColoredNormalTriangleBuffer(VERTICES_DATA_BACK_FACE, GLES20.GL_TRIANGLE_STRIP),
        		new ColoredNormalTriangleBuffer(VERTICES_DATA_LEFT_FACE, GLES20.GL_TRIANGLE_STRIP),
        		new ColoredNormalTriangleBuffer(VERTICES_DATA_TOP_FACE, GLES20.GL_TRIANGLE_STRIP),
        		new ColoredNormalTriangleBuffer(VERTICES_DATA_BOTTOM_FACE, GLES20.GL_TRIANGLE_STRIP));
    }
    
}
