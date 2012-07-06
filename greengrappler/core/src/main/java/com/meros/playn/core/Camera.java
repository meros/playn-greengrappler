package com.meros.playn.core;

import com.meros.playn.core.entities.Hero;

public class Camera {

	float2 myOffset = new float2();
	float2 myShakeOffset = new float2();

	float myShakeAmount = 0.0f;
	int myShakeTime = 0;

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

	public void update(Hero aHero, float2 aTopLeft, float2 aBottomRight) {
		boolean foundRect = false;
		float2 desiredOffset = new float2();

		// float2 heroRealPos = aHero.getPosition();
		// TODO: for (int i = 0; i < myRects.size(); i++)
		// {
		// float2 topleft = myRects.get(i).first;
		// float2 bottomright = myRects.get(i).second;
		//
		// if (topleft.x < heroRealPos.x && topleft.y <heroRealPos.y &&
		// bottomright.x > heroRealPos.x && bottomright.y > heroRealPos.y)
		// {
		// desiredOffset = -topleft;
		// desiredOffset.y += 10;
		// foundRect = true;
		// }
		// }

		if (!foundRect) {
			float2 heropos = new float2(-aHero.getDrawPositionX() + 320 / 2,
					-aHero.getDrawPositionY() + (2 * 240) / 3);

			desiredOffset = heropos;
		}

		myOffset = myOffset.add((desiredOffset.subtract(myOffset))
				.multiply((float) 0.1));

		myShakeTime--;

		if (myShakeTime > 0) {
			myShakeOffset.x = (float) (myShakeAmount * (Math.random() - 0.5f));
			myShakeOffset.y = (float) (myShakeAmount * (Math.random() - 0.5f));
		}
		if (myShakeTime < 0) {
			myShakeOffset = new float2();
		}

		if (myOffset.x + aTopLeft.x > 0) {
			myOffset.x = -aTopLeft.x;
		}

		if (myOffset.y + aTopLeft.y > 10) {
			myOffset.y = 10 - aTopLeft.y;
		}

		if (myOffset.x + aBottomRight.x < 320) {
			myOffset.x = 320 - aBottomRight.x;
		}

		if (myOffset.y + aBottomRight.y < 240) {
			myOffset.y = 240 - aBottomRight.y;
		}
	}

	public void onRespawn() {
		myShakeTime = 0;
		myShakeAmount = 0.0f;
	}
}
