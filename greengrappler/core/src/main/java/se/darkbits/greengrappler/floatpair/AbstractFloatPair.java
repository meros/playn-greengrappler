package se.darkbits.greengrappler.floatpair;

public abstract class AbstractFloatPair {
	static float EPSILON = 0.0000001f;

	public abstract float getX();

	public abstract float getY();

	public boolean isZero() {
		return length() < EPSILON;
	}

	public float length() {
		return (float) Math.sqrt(getX() * getX() + getY() * getY());
	}

	public float lengthCompare(float aCompareLength) {
		return (getX() * getX() + getY() * getY())
				- (aCompareLength * aCompareLength);
	}

	public float lengthCompare(AbstractFloatPair aOther) {
		return (getX() * getX() + getY() * getY())
				- (aOther.getX() * aOther.getX() * aOther.getY() * aOther
						.getY());
	}

	public float dot(AbstractFloatPair aOther) {
		return getX() * aOther.getX() + getY() * aOther.getY();
	}
}
