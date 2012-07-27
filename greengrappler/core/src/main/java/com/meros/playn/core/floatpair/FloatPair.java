package com.meros.playn.core.floatpair;

public class FloatPair extends AbstractFloatPair {
	private float x = 0.0f;
	private float y = 0.0f;

	public FloatPair(AbstractFloatPair aPair) {
		x = aPair.getX();
		y = aPair.getY();
	}

	public FloatPair(int aX, int aY) {
		x = aX;
		y = aY;
	}

	public FloatPair() {
		x = 0;
		y = 0;
	}

	public FloatPair add(AbstractFloatPair aOther) {
		return add(aOther.getX(), aOther.getY());
	}

	public FloatPair add(float aX, float aY) {
		x += aX;
		y += aY;
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

	public FloatPair set(AbstractFloatPair aOther) {
		return set(aOther.getX(), aOther.getY());
	}

	public FloatPair set(float aX, float aY) {
		x = aX;
		y = aY;

		return this;
	}

	public FloatPair subtract(AbstractFloatPair aOther) {
		return subtract(aOther.getX(), aOther.getY());
	}

	public FloatPair subtract(float aX, float aY) {
		x -= aX;
		y -= aY;
		return this;
	}

	public FloatPair normalize() {
		return this.divide(this.length());
	}
}
