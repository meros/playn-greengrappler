package com.meros.playn.core;

public class CollisionMethods {

	public static boolean Collides(Collidable aFirst, Collidable aSecond) {
		if (aFirst.getCollideRight() < aSecond.getCollideLeft())
			return false;
		if (aFirst.getCollideLeft() > aSecond.getCollideRight())
			return false;
		if (aFirst.getCollideBottom() < aSecond.getCollideTop())
			return false;
		if (aFirst.getCollideTop() > aSecond.getCollideBottom())
			return false;
		
		return true;
	}
	
	public static boolean Collides(Collidable aFirst, float aSecondLeft, float aSecondTop, float aSecondRight, float aSecondBottom) {
		if (aFirst.getCollideRight() < aSecondLeft)
			return false;
		if (aFirst.getCollideLeft() > aSecondRight)
			return false;
		if (aFirst.getCollideBottom() < aSecondTop)
			return false;
		if (aFirst.getCollideTop() > aSecondBottom)
			return false;
		
		return true;
	}

}
