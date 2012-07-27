package com.meros.playn.core;

public class Collision {

	public static interface AbstractCollidable {
		public abstract float getCollideTop();

		public abstract float getCollideLeft();

		public abstract float getCollideBottom();

		public abstract float getCollideRight();
	}

	public static boolean Collides(AbstractCollidable aFirst,
			AbstractCollidable aSecond) {
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

	public static boolean Collides(AbstractCollidable aFirst,
			float aSecondLeft, float aSecondTop, float aSecondRight,
			float aSecondBottom) {
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
