package com.meros.playn.core;

public class float2   {
	static float EPSILON = 0.0000001f;
	public final float x;
	public final float y;

	public float2() {
		x = 0;
		y = 0;
	}

	public float2(float aX, float aY) {
		x = aX;
		y = aY;
	}
	
	public float2 add(float2 other) {
		return new float2(x+other.x, y+other.y);
	}

	public float2 divide(float divider) {
		return new float2(x/divider, y/divider);
	}

	public float dot(float2 aOther) {
		return x * aOther.x + y * aOther.y;
	}

	public boolean isZero() {
		return length() < EPSILON;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float lengthCompare(float aCompareLength) {
		return (x * x + y * y) - (aCompareLength * aCompareLength);
	}

	public float lengthCompare(float2 aOther) {
		return (x * x + y * y) - (aOther.x * aOther.x * aOther.y * aOther.y);
	}

	public float2 multiply(float factor) {
		return new float2(x*factor, y*factor);
	}

	public float2 normalize() {
		return this.divide(this.length());
	}

	public float2 subtract(float2 other) {
		return new float2(x-other.x, y-other.y);
	}
}
