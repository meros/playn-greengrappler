package com.meros.playn.core;

public class ImmutableFloatPair implements AbstractFloatPair   {
	static float EPSILON = 0.0000001f;
	private final float x;
	private final float y;

	public ImmutableFloatPair() {
		x = 0;
		y = 0;
	}

	public ImmutableFloatPair(float aX, float aY) {
		x = aX;
		y = aY;
	}
	
	public ImmutableFloatPair add(ImmutableFloatPair other) {
		return new ImmutableFloatPair(x+other.x, y+other.y);
	}

	public ImmutableFloatPair divide(float divider) {
		return new ImmutableFloatPair(x/divider, y/divider);
	}

	public float dot(ImmutableFloatPair aOther) {
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

	public float lengthCompare(ImmutableFloatPair aOther) {
		return (x * x + y * y) - (aOther.x * aOther.x * aOther.y * aOther.y);
	}

	public ImmutableFloatPair multiply(float factor) {
		return new ImmutableFloatPair(x*factor, y*factor);
	}

	public ImmutableFloatPair normalize() {
		return this.divide(this.length());
	}

	public ImmutableFloatPair subtract(ImmutableFloatPair other) {
		return new ImmutableFloatPair(x-other.x, y-other.y);
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}
}
