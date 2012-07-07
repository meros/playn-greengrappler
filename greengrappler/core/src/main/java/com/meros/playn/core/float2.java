package com.meros.playn.core;

public class float2  implements Cloneable {
	static float EPSILON = 0.0000001f;
	public float x = 0;
	public float y = 0;

	public float2() {
	}

	public float2(float aX, float aY) {
		x = aX;
		y = aY;
	}
	
	private float2 getClone()
	{
		try {
			return (float2) clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public float2 add(float2 other) {
		float2 copy = getClone();
		copy.x += other.x;
		copy.y += other.y;
		return copy;
	}

	public float2 divide(float divider) {
		float2 copy = getClone();
		copy.x /= divider;
		copy.y /= divider;
		return copy;
	}

	public float dot(float2 aOther) {
		// TODO Auto-generated method stub
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
		float2 copy = getClone();
		copy.x *= factor;
		copy.y *= factor;
		return copy;
	}

	public float2 normalize() {
		return this.divide(this.length());
	}

	public float2 subtract(float2 other) {
		float2 copy = getClone();
		copy.x -= other.x;
		copy.y -= other.y;
		return copy;
	}

	public void set(float2 aOther) {
		x = aOther.x;
		y = aOther.y;
	}
}
