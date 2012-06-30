package com.meros.playn.core;

public class float2 {
	public float x = 0;
	public float y = 0;
	float EPSILON = 0.0000001f;
	
	public float2(float aX, float aY) {
		x = aX;
		y = aY;
	}

	public float2() {
	}

	public Object clone() {
	    float2 copy = new float2();
	    copy.x = x;
	    copy.y = y;
		return copy;
	}
	
	public boolean isZero()
	{
		return length() < EPSILON;
	}
	
	public float2 normalize()
	{
		return this.divide(this.length());
	}

	public float2 multiply(float factor) {
		float2 copy = (float2)clone();
		copy.x *= factor;
		copy.y *= factor;
		return copy;
	}

	public float2 subtract(float2 other) {
		float2 copy = (float2)clone();
		copy.x -= other.x;
		copy.y -= other.y;
		return copy;
	}
	
	public float2 add(float2 other) {
		float2 copy = (float2)clone();
		copy.x += other.x;
		copy.y += other.y;
		return copy;
	}

	public float2 divide(float divider) {
		float2 copy = (float2)clone();
		copy.x /= divider;
		copy.y /= divider;
		return copy;
	}

	public float length() {
		return (float) Math.sqrt(x*x+y*y);
	}

	public float lengthCompare(float aCompareLength) {
		return (x*x+y*y) - (aCompareLength*aCompareLength);
	}
}
