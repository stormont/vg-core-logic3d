package com.voyagegames.logic3d.models;

import java.io.Serializable;

public class Index implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2892122643903202692L;
	public final int x;
	public final int y;
	public final int z;
	
	public Index(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		if (x < 0 || y < 0 || z < 0) {
			throw new IllegalArgumentException("Indices must be 0 or greater (" + x + "," + y + "," + z + ")");
		}
	}
	
	public int toArrayLocation(final int dimensionSize) {
		return (x * dimensionSize * dimensionSize + y * dimensionSize + z);
	}

}
