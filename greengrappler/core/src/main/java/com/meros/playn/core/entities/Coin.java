package com.meros.playn.core.entities;

import java.util.EnumSet;

import playn.core.Surface;

import com.meros.playn.core.AbstractFloatPair;
import com.meros.playn.core.Animation;
import com.meros.playn.core.Constants.Direction;
import com.meros.playn.core.Entity;
import com.meros.playn.core.ImmutableFloatPair;
import com.meros.playn.core.PlayerSkill;
import com.meros.playn.core.Resource;
import com.meros.playn.core.Room;
import com.meros.playn.core.Sound;
import com.meros.playn.core.UtilMethods;

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
			AbstractFloatPair aCenterPosition, int aLifeTime, Room aRoom) {
		for (int i = 0; i < aNumberOfCoins; i++) {

			ImmutableFloatPair velocity = UtilMethods.sincos(3.1415 * (i + 1)
					/ (aNumberOfCoins + 1));
			velocity = new ImmutableFloatPair(velocity.getX() * 100, velocity.getY() * -200);

			Coin coin = new Coin();
			coin.setLifeTime(aLifeTime);
			coin.setPosition(aCenterPosition);
			coin.setVelocity(velocity);
			aRoom.addEntity(coin);
			//TODO: this causes a stupid lag when lots of coins! Not really needed sound imo! Sound.playSample("data/sounds/coin");
		}
	}

	public Coin() {
		myAnimationCoin = Resource.getAnimation("data/images/coin.bmp", 4);
		setSize(new ImmutableFloatPair(12, 12));
	}

	@Override
	public void draw(Surface aBuffer, int aOffsetX, int aOffsetY, int aLayer) {
		float x = getPosition().getX();
		float y = getPosition().getY();
		x -= myAnimationCoin.getFrameWidth() / 2;
		y -= myAnimationCoin.getFrameHeight() / 2;

		x += aOffsetX;
		y += aOffsetY;

		if (!myTemporary || (myLifeTime / 10) % 2 == 0) {
			if (myFrame > 180) {
				myFrame = 0;
			}

			myAnimationCoin.drawFrame(aBuffer, (myFrame < 5 * 4) ? myFrame / 5 : 0,
					(int) x, (int) y, false, false);
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
				mVelocity = mVelocity.add(new ImmutableFloatPair(0.0f, 6.0f));

				EnumSet<Direction> bumps = moveWithCollision();

				if (bumps.contains(Direction.LEFT)
						|| bumps.contains(Direction.RIGHT)) {
					if (Math.abs(mVelocity.getX()) > 10) {
						Sound.playSample("data/sounds/coin");
					}

					mVelocity.set(mVelocity.getX() * -0.5f, mVelocity.getY());
				}

				if (bumps.contains(Direction.UP)
						|| bumps.contains(Direction.DOWN)) {
					if (Math.abs(mVelocity.getY()) > 10) {
						Sound.playSample("data/sounds/coin");
					}

					mVelocity.set(mVelocity.getX(), mVelocity.getY()*-0.2f);
				}

				myLifeTime--;
			}
		}

		if (hero.Collides(this)) {
			if (hero.gotCoin()) {
				PlayerSkill.playerDidSomethingClever(0.3f, 0.05f);
				Sound.playSample("data/sounds/coin");
				remove();
			}
		}

		myFrame++;
	}
}