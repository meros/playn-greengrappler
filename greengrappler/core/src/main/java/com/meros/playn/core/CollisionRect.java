package com.meros.playn.core;

public class CollisionRect {
	public FloatPair myBottomRight = new FloatPair();
	public FloatPair myTopLeft = new FloatPair();
	
	public boolean Collides(CollisionRect aOther) {
		if (myBottomRight.getX() <= aOther.myTopLeft.getX())
			return false;

		if (myTopLeft.getX() >= aOther.myBottomRight.getX())
			return false;

		if (myBottomRight.getY() <= aOther.myTopLeft.getY())
			return false;

		if (myTopLeft.getY() >= aOther.myBottomRight.getY())
			return false;

		return true;
	}
}
