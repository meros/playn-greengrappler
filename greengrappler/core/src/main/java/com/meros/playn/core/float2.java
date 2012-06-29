package com.meros.playn.core;

public class float2 {
	public float x = 0;
	public float y = 0;
	
	public Object clone() {
	    float2 copy = new float2();
	    copy.x = x;
	    copy.y = y;
		return copy;
	}

	public float2 multiply(float factor) {
		float2 copy = (float2)clone();
		copy.x *= factor;
		copy.y *= factor;
		return copy;
	}

}
