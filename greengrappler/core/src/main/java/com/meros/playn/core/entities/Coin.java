package com.meros.playn.core.entities;

import java.util.EnumSet;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.Constants.Direction;
import com.meros.playn.core.Entity;
import com.meros.playn.core.PlayerSkill;
import com.meros.playn.core.Resource;
import com.meros.playn.core.Room;
import com.meros.playn.core.Sound;
import com.meros.playn.core.UtilMethods;
import com.meros.playn.core.float2;

public class Coin extends Entity {

	enum Type {
		DYNAMIC, STATIC
	}

	Animation myAnimationCoin;
	int myFrame = 0;

	int myLifeTime = 0;
	boolean myTemporary = false;

	Type myType = Type.STATIC;

	public static void SpawnDeathCoins(int aNumberOfCoins,
			float2 aCenterPosition, int aLifeTime, Room aRoom) {
		for (int i = 0; i < aNumberOfCoins; i++) {

			float2 velocity = UtilMethods.sincos(3.1415 * (i + 1)
					/ (aNumberOfCoins + 1));
			velocity = new float2(velocity.x * 100, velocity.y * -200);

			Coin coin = new Coin();
			coin.setLifeTime(aLifeTime);
			coin.setPosition(aCenterPosition);
			coin.setVelocity(velocity);
			aRoom.addEntity(coin);
			Sound.playSample("data/sounds/coin");
		}
	}

	public Coin() {
		myAnimationCoin = Resource.getAnimation("data/images/coin.bmp", 4);
		setSize(new float2(12, 12));
	}

	@Override
	public void draw(Surface aBuffer, int aOffsetX, int aOffsetY, int aLayer) {
		float2 pos = getPosition();
		pos = pos.subtract(new float2(myAnimationCoin.getFrameWidth(),
				myAnimationCoin.getFrameHeight()).divide(2));

		pos = pos.add(new float2(aOffsetX, aOffsetY));

		if (!myTemporary || (myLifeTime / 10) % 2 == 0) {
			if (myFrame > 180) {
				myFrame = 0;
			}

			myAnimationCoin.drawFrame(aBuffer, (myFrame < 5 * 4) ? myFrame / 5 : 0,
					(int) pos.x, (int) pos.y, false, false);
		}
	}

	@Override
	public int getLayer() {
		return 3;
	}

	// virtual void onRespawn();
	@Override
	public void onRespawn() {
		if (myType == Type.DYNAMIC)
			remove();
	}

	// virtual void setLifeTime(int aLifeTime);
	public void setLifeTime(int aLifeTime) {
		myLifeTime = aLifeTime;
		myType = Type.DYNAMIC;
		myTemporary = aLifeTime != 0;
	}

	//
	// virtual void update();
	@Override
	public void update() {
		Hero hero = mRoom.getHero();

		if (myType == Type.DYNAMIC) {
			if (myTemporary && myLifeTime == 0) {
				remove();
			} else {
				mVelocity = mVelocity.add(new float2(0.0f, 6.0f));

				EnumSet<Direction> bumps = moveWithCollision();

				if (bumps.contains(Direction.LEFT)
						|| bumps.contains(Direction.RIGHT)) {
					if (Math.abs(mVelocity.x) > 10) {
						Sound.playSample("data/sounds/coin");
					}

					mVelocity = new float2(mVelocity.x * -0.5f, mVelocity.y);
				}

				if (bumps.contains(Direction.UP)
						|| bumps.contains(Direction.DOWN)) {
					if (Math.abs(mVelocity.y) > 10) {
						Sound.playSample("data/sounds/coin");
					}

					mVelocity = new float2(mVelocity.x, mVelocity.y*-0.2f);
				}

				myLifeTime--;
			}
		}

		if (hero.getCollisionRect().Collides(getCollisionRect())) {
			if (hero.gotCoin()) {
				PlayerSkill.playerDidSomethingClever(0.3f, 0.05f);
				Sound.playSample("data/sounds/coin");
				remove();
			}
		}

		myFrame++;
	}
}