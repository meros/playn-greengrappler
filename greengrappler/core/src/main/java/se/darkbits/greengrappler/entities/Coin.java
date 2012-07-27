package se.darkbits.greengrappler.entities;

import java.util.EnumSet;

import playn.core.Surface;
import se.darkbits.greengrappler.Entity;
import se.darkbits.greengrappler.PlayerSkill;
import se.darkbits.greengrappler.Resource;
import se.darkbits.greengrappler.Room;
import se.darkbits.greengrappler.UtilMethods;
import se.darkbits.greengrappler.Constants.Direction;
import se.darkbits.greengrappler.floatpair.AbstractFloatPair;
import se.darkbits.greengrappler.floatpair.ImmutableFloatPair;
import se.darkbits.greengrappler.media.Animation;
import se.darkbits.greengrappler.media.Sound;


public class Coin extends Entity {

	public static final int COIN_SIZE = 12;

	enum Type {
		DYNAMIC, STATIC
	}

	Animation myAnimationCoin;
	int myFrame = 0;

	int myLifeTime = 0;
	boolean myTemporary = false;

	Type myType = Type.STATIC;
	private boolean mCollides = false;

	public static void SpawnDeathCoins(int aNumberOfCoins,
			AbstractFloatPair aCenterPosition, int aLifeTime, Room aRoom) {
		for (int i = 0; i < aNumberOfCoins; i++) {

			ImmutableFloatPair velocity = UtilMethods.sincos(3.1415 * (i + 1)
					/ (aNumberOfCoins + 1));
			velocity = new ImmutableFloatPair(velocity.getX() * 100,
					velocity.getY() * -200);

			Coin coin = new Coin();
			coin.setLifeTime(aLifeTime);
			coin.setPosition(aCenterPosition);
			coin.setVelocity(velocity);
			aRoom.addEntity(coin);
		}
	}

	public Coin() {
		myAnimationCoin = Resource.getAnimation("data/images/coin.bmp", 4);
		setSize(new ImmutableFloatPair(COIN_SIZE, COIN_SIZE));
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

			myAnimationCoin.drawFrame(aBuffer, (myFrame < 5 * 4) ? myFrame / 5
					: 0, (int) x, (int) y, false, false);
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
		if (myType == Type.DYNAMIC) {
			if (myRoom.getHero().Collides(this)) {
				setCollides();
			}

			if (myTemporary && myLifeTime == 0) {
				remove();
			} else {
				myVelocity.add(0.0f, 6.0f);

				EnumSet<Direction> bumps = moveWithCollision();

				if (bumps.contains(Direction.LEFT)
						|| bumps.contains(Direction.RIGHT)) {
					if (Math.abs(myVelocity.getX()) > 10) {
						Sound.playSample("data/sounds/coin");
					}

					myVelocity.set(myVelocity.getX() * -0.5f, myVelocity.getY());
				}

				if (bumps.contains(Direction.UP)
						|| bumps.contains(Direction.DOWN)) {
					if (Math.abs(myVelocity.getY()) > 10) {
						Sound.playSample("data/sounds/coin");
					}

					myVelocity.set(myVelocity.getX(), myVelocity.getY() * -0.2f);
				}

				myLifeTime--;
			}
		}

		if (mCollides) {
			if (myRoom.getHero().gotCoin()) {
				PlayerSkill.playerDidSomethingClever(0.3f, 0.05f);
				Sound.playSample("data/sounds/coin");
				remove();
			}
		}

		myFrame++;
	}

	public void setCollides() {
		mCollides = true;
	}

	public boolean isDynamic() {
		return myType == Type.DYNAMIC;
	}
}