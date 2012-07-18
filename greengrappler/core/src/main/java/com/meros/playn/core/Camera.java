package com.meros.playn.core;

import java.util.ArrayList;
import java.util.List;

import com.meros.playn.core.entities.Hero;

public class Camera {

	private class Rect
	{
		public int x;
		public int y;
		public int w;
		public int h;
	}

	List<Rect> cameraRects = new ArrayList<Rect>();

	ImmutableFloatPair myOffset = new ImmutableFloatPair();
	ImmutableFloatPair myShakeOffset = new ImmutableFloatPair();

	float myShakeAmount = 0.0f;
	int myShakeTime = 0;

	private final ImmutableFloatPair myTopLeft;
	private final ImmutableFloatPair myBottomRight;

	public Camera(ImmutableFloatPair aTopLeft, ImmutableFloatPair aBottomRight) {
		myTopLeft = aTopLeft;
		myBottomRight = aBottomRight;
	}

	public void addShake(float aAmount, int aShakeTime) {
		myShakeAmount = aAmount;
		myShakeTime = aShakeTime;
	}

	public void centerToHero(Hero aHero) {
		ImmutableFloatPair heropos = new ImmutableFloatPair(-aHero.getDrawPositionX() + 320 / 2,
				-aHero.getDrawPositionY() + (2 * 240) / 3);
		myOffset = heropos;
	}

	public ImmutableFloatPair getOffset() {
		return new ImmutableFloatPair((int) (myOffset.getX() + myShakeOffset.getX() + 0.5),
				(int) (myOffset.getY() + myShakeOffset.getY() + 0.5));
	}

	public void update(Hero aHero) {
		boolean foundRect = false;
		ImmutableFloatPair desiredOffset = new ImmutableFloatPair();

		ImmutableFloatPair heroRealPos = aHero.getPosition();
		for(Rect rect : cameraRects)
		{
			if (rect.x < heroRealPos.getX() && rect.y <heroRealPos.getY() &&
					(rect.x + rect.w) > heroRealPos.getX() && (rect.y+rect.h) > heroRealPos.getY())
			{
				desiredOffset = new ImmutableFloatPair(-rect.x, -rect.y + 10);
				foundRect = true;
			}
		}

		if (!foundRect) {
			ImmutableFloatPair heropos = new ImmutableFloatPair(-aHero.getDrawPositionX() + 320 / 2,
					-aHero.getDrawPositionY() + (2 * 240) / 3);

			desiredOffset = heropos;
		}

		myOffset = myOffset.add((desiredOffset.subtract(myOffset))
				.multiply((float) 0.1));

		myShakeTime--;

		if (myShakeTime > 0) {
			myShakeOffset = new ImmutableFloatPair(
					(float) (myShakeAmount * (Math.random() - 0.5f)),
					(float) (myShakeAmount * (Math.random() - 0.5f)));
		}
		if (myShakeTime < 0) {
			myShakeOffset = new ImmutableFloatPair();
		}

		if (myOffset.getX() + myTopLeft.getX() > 0) {
			myOffset = new ImmutableFloatPair(-myTopLeft.getX(), myOffset.getY());
		}

		if (myOffset.getY() + myTopLeft.getY() > 10) {
			myOffset = new ImmutableFloatPair(myOffset.getX(), 10 - myTopLeft.getY());
		}

		if (myOffset.getX() + myBottomRight.getX() < 320) {
			myOffset = new ImmutableFloatPair(320 - myBottomRight.getX(), myOffset.getY());
		}

		if (myOffset.getY() + myBottomRight.getY() < 240) {
			myOffset = new ImmutableFloatPair(myOffset.getX(), 240 - myBottomRight.getY());
		}

		if (GlobalOptions.avoidHeroAtThumbs())
		{
			int thumbSafeAreaSize = 130;

			if (heroRealPos.getX() + myOffset.getX() < thumbSafeAreaSize)
			{
				myOffset = new ImmutableFloatPair(thumbSafeAreaSize-heroRealPos.getX(), myOffset.getY());
			}

			if (heroRealPos.getX() + myOffset.getX() > 320-thumbSafeAreaSize)
			{
				myOffset = new ImmutableFloatPair((320-thumbSafeAreaSize)-heroRealPos.getX(), myOffset.getY());
			}
		}
	}

	public void onRespawn() {
		myShakeTime = 0;
		myShakeAmount = 0.0f;
	}

	public void addRect(int x, int y, int w, int h) {
		Rect rect = new Rect();
		rect.x = x;
		rect.y = y;
		rect.w = w;
		rect.h = h;

		cameraRects.add(rect);
	}
}
