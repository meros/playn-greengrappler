package com.meros.playn.core;

public class FloatPair implements AbstractFloatPair {
	private float x = 0.0f;
	private float y = 0.0f;
	
	public FloatPair(AbstractFloatPair aPair)
	{
		x = aPair.getX();
		y = aPair.getY();
	}

	public FloatPair(int aX, int aY) {
		x = aX;
		y = aY;
	}

	public FloatPair add(AbstractFloatPair aOther) {
		x += aOther.getX();
		y += aOther.getY();
		return this;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	public FloatPair multiply(float aFactor) {
		x *= aFactor;
		y *= aFactor;
		return this;
	}

	public FloatPair divide(float aDivisor) {
		x /= aDivisor;
		y /= aDivisor;
		return this;
	}
}
