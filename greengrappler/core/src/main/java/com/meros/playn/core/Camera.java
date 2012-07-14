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

	float2 myOffset = new float2();
	float2 myShakeOffset = new float2();

	float myShakeAmount = 0.0f;
	int myShakeTime = 0;

	private final float2 myTopLeft;
	private final float2 myBottomRight;

	public Camera(float2 aTopLeft, float2 aBottomRight) {
		myTopLeft = aTopLeft;
		myBottomRight = aBottomRight;
	}

	public void addShake(float aAmount, int aShakeTime) {
		myShakeAmount = aAmount;
		myShakeTime = aShakeTime;
	}

	public void centerToHero(Hero aHero) {
		float2 heropos = new float2(-aHero.getDrawPositionX() + 320 / 2,
				-aHero.getDrawPositionY() + (2 * 240) / 3);
		myOffset = heropos;
	}

	public float2 getOffset() {
		return new float2((int) (myOffset.x + myShakeOffset.x + 0.5),
				(int) (myOffset.y + myShakeOffset.y + 0.5));
	}

	public void update(Hero aHero) {
		boolean foundRect = false;
		float2 desiredOffset = new float2();

		float2 heroRealPos = aHero.getPosition();
		for(Rect rect : cameraRects)
		{
			if (rect.x < heroRealPos.x && rect.y <heroRealPos.y &&
					(rect.x + rect.w) > heroRealPos.x && (rect.y+rect.h) > heroRealPos.y)
			{
				desiredOffset = new float2(-rect.x, -rect.y + 10);
				foundRect = true;
			}
		}

		if (!foundRect) {
			float2 heropos = new float2(-aHero.getDrawPositionX() + 320 / 2,
					-aHero.getDrawPositionY() + (2 * 240) / 3);

			desiredOffset = heropos;
		}

		myOffset = myOffset.add((desiredOffset.subtract(myOffset))
				.multiply((float) 0.1));

		myShakeTime--;

		if (myShakeTime > 0) {
			myShakeOffset = new float2(
					(float) (myShakeAmount * (Math.random() - 0.5f)),
					(float) (myShakeAmount * (Math.random() - 0.5f)));
		}
		if (myShakeTime < 0) {
			myShakeOffset = new float2();
		}

		if (myOffset.x + myTopLeft.x > 0) {
			myOffset = new float2(-myTopLeft.x, myOffset.y);
		}

		if (myOffset.y + myTopLeft.y > 10) {
			myOffset = new float2(myOffset.x, 10 - myTopLeft.y);
		}

		if (myOffset.x + myBottomRight.x < 320) {
			myOffset = new float2(320 - myBottomRight.x, myOffset.y);
		}

		if (myOffset.y + myBottomRight.y < 240) {
			myOffset = new float2(myOffset.x, 240 - myBottomRight.y);
		}

		if (GlobalOptions.avoidHeroAtThumbs())
		{
			int thumbSafeAreaSize = 130;

			if (heroRealPos.x + myOffset.x < thumbSafeAreaSize)
			{
				myOffset = new float2(thumbSafeAreaSize-heroRealPos.x, myOffset.y);
			}

			if (heroRealPos.x + myOffset.x > 320-thumbSafeAreaSize)
			{
				myOffset = new float2((320-thumbSafeAreaSize)-heroRealPos.x, myOffset.y);
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
