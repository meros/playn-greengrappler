package se.darkbits.greengrappler.floatpair;

public class ImmutableFloatPair extends AbstractFloatPair {
	private final float x;
	private final float y;

	public ImmutableFloatPair() {
		x = 0;
		y = 0;
	}

	public ImmutableFloatPair(AbstractFloatPair aOther) {
		x = aOther.getX();
		y = aOther.getY();
	}

	public ImmutableFloatPair(float aX, float aY) {
		x = aX;
		y = aY;
	}

	public ImmutableFloatPair add(AbstractFloatPair other) {
		return new ImmutableFloatPair(x + other.getX(), y + other.getY());
	}

	public ImmutableFloatPair divide(float divider) {
		return new ImmutableFloatPair(x / divider, y / divider);
	}

	public ImmutableFloatPair multiply(float factor) {
		return new ImmutableFloatPair(x * factor, y * factor);
	}

	public ImmutableFloatPair normalize() {
		return this.divide(this.length());
	}

	public ImmutableFloatPair subtract(AbstractFloatPair other) {
		return new ImmutableFloatPair(x - other.getX(), y - other.getY());
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
