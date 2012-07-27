package com.meros.playn.core;

import java.util.ArrayList;
import java.util.List;

import com.meros.playn.core.entities.Hero;
import com.meros.playn.core.floatpair.FloatPair;
import com.meros.playn.core.floatpair.ImmutableFloatPair;

public class Camera {

	private class Rect {
		public int myX;
		public int myY;
		public int myW;
		public int myH;
	}

	List<Rect> myCameraRects = new ArrayList<Rect>();

	FloatPair myOffset = new FloatPair();
	FloatPair myShakeOffset = new FloatPair();

	float myShakeAmount = 0.0f;
	int myShakeTime = 0;

	private final ImmutableFloatPair myTopLeft;
	private final ImmutableFloatPair myBottomRight;

	private FloatPair myUpdateDesiredOffset = new FloatPair();

	public Camera(ImmutableFloatPair aTopLeft, ImmutableFloatPair aBottomRight) {
		myTopLeft = aTopLeft;
		myBottomRight = aBottomRight;
	}

	public void addShake(float aAmount, int aShakeTime) {
		myShakeAmount = aAmount;
		myShakeTime = aShakeTime;

		// Shake time is in frames - convert to ms
		GlobalOptions.Vibrate((int) (myShakeTime / 60.0f * 1000.0f),
				GlobalOptions.VibrationType.PULSATING);
	}

	public void centerToHero(Hero aHero) {
		myOffset.set(-aHero.getDrawPositionX() + 320 / 2,
				-aHero.getDrawPositionY() + (2 * 240) / 3);
	}

	public int getOffsetX() {
		return (int) (myOffset.getX() + myShakeOffset.getX() + 0.5);
	}

	public int getOffsetY() {
		return (int) (myOffset.getY() + myShakeOffset.getY() + 0.5);
	}

	public void update(Hero aHero) {
		boolean foundRect = false;
		myUpdateDesiredOffset.set(0, 0);

		FloatPair heroRealPos = aHero.getPosition();
		for (int i = 0; i < myCameraRects.size(); i++) {
			Rect rect = myCameraRects.get(i);

			if (rect.myX < heroRealPos.getX() && rect.myY < heroRealPos.getY()
					&& (rect.myX + rect.myW) > heroRealPos.getX()
					&& (rect.myY + rect.myH) > heroRealPos.getY()) {
				myUpdateDesiredOffset.set(-rect.myX, -rect.myY + 10);
				foundRect = true;
			}
		}

		if (!foundRect) {
			myUpdateDesiredOffset.set(-aHero.getDrawPositionX() + 320 / 2,
					-aHero.getDrawPositionY() + (2 * 240) / 3);
		}

		myUpdateDesiredOffset.subtract(myOffset).multiply(0.1f);
		myOffset.add(myUpdateDesiredOffset);

		myShakeTime--;

		if (myShakeTime > 0) {
			myShakeOffset.set((float) (myShakeAmount * (Math.random() - 0.5f)),
					(float) (myShakeAmount * (Math.random() - 0.5f)));
		}
		if (myShakeTime < 0) {
			myShakeOffset.set(0, 0);
		}

		if (myOffset.getX() + myTopLeft.getX() > 0) {
			myOffset.set(-myTopLeft.getX(), myOffset.getY());
		}

		if (myOffset.getY() + myTopLeft.getY() > 10) {
			myOffset.set(myOffset.getX(), 10 - myTopLeft.getY());
		}

		if (myOffset.getX() + myBottomRight.getX() < 320) {
			myOffset.set(320 - myBottomRight.getX(), myOffset.getY());
		}

		if (myOffset.getY() + myBottomRight.getY() < 240) {
			myOffset.set(myOffset.getX(), 240 - myBottomRight.getY());
		}

		if (GlobalOptions.avoidHeroAtThumbs()) {
			int thumbSafeAreaSize = 130;

			if (heroRealPos.getX() + myOffset.getX() < thumbSafeAreaSize) {
				myOffset.set(thumbSafeAreaSize - heroRealPos.getX(),
						myOffset.getY());
			}

			if (heroRealPos.getX() + myOffset.getX() > 320 - thumbSafeAreaSize) {
				myOffset.set((320 - thumbSafeAreaSize) - heroRealPos.getX(),
						myOffset.getY());
			}
		}
	}

	public void onRespawn() {
		myShakeTime = 0;
		myShakeAmount = 0.0f;
	}

	public void addRect(int x, int y, int w, int h) {
		Rect rect = new Rect();
		rect.myX = x;
		rect.myY = y;
		rect.myW = w;
		rect.myH = h;

		myCameraRects.add(rect);
	}
}
