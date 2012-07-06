package com.meros.playn.core;

public class CollisionRect {
	public float2 myBottomRight = new float2();
	public float2 myTopLeft = new float2();
	
	public boolean Collides(CollisionRect aOther) {
		if (myBottomRight.x <= aOther.myTopLeft.x)
			return false;

		if (myTopLeft.x >= aOther.myBottomRight.x)
			return false;

		if (myBottomRight.y <= aOther.myTopLeft.y)
			return false;

		if (myTopLeft.y >= aOther.myBottomRight.y)
			return false;

		return true;
	}
}
