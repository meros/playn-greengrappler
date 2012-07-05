package com.meros.playn.core;

import static com.meros.playn.core.Constants.*;

import java.util.EnumSet;

import playn.core.Surface;

public class Coin extends Entity {

	int mLifeTime = 0;
	enum Type { STATIC, DYNAMIC }
	Type mType = Type.STATIC;
	boolean mTemporary = false;


	int mFrame = 0;
	Animation mAnimationCoin;

	//		Coin();
	public Coin()
	{
		mAnimationCoin = Resource.getAnimation("data/images/coin.bmp", 4);
		setSize(new float2(12,12));
	}

	//		virtual void setLifeTime(int aLifeTime);
	public void setLifeTime(int aLifeTime)
	{
		mLifeTime = aLifeTime;
		mType = Type.DYNAMIC;
		mTemporary = aLifeTime != 0;
	}

	//	static void SpawnDeathCoins( int aNumberOfCoins, float2 aCenterPosition, int aLifeTime, Room* aRoom);

	//
	//		virtual void update();
	public void update() 
	{
		Hero hero = mRoom.getHero();

		if (mType == Type.DYNAMIC)
		{
			if (mTemporary && mLifeTime == 0)
			{
				remove();
			}
			else
			{
				mVelocity.y += 6.0;

				EnumSet<Direction> bumps = moveWithCollision();

				if (bumps.contains(Direction.LEFT) || bumps.contains(Direction.RIGHT)) {
					if (Math.abs(mVelocity.x) > 10){
						Sound.playSample("data/sounds/coin");
					}
					mVelocity.x *= -0.5;
				}

				if (bumps.contains(Direction.UP) || bumps.contains(Direction.DOWN)) {
					if (Math.abs(mVelocity.y) > 10){
						Sound.playSample("data/sounds/coin");
					}
					mVelocity.y *= -0.2;
				}

				mLifeTime--;
			}
		}

		if (Collides(hero.getCollisionRect(), getCollisionRect()))
		{
			if (hero.gotCoin())
			{
				PlayerSkill.playerDidSomethingClever(0.3f, 0.05f);
				Sound.playSample("data/sounds/coin");
				remove();
			}
		}

		mFrame++;
	}
	//		virtual void draw(BITMAP *buffer, int offsetX, int offsetY, int layer);
	public void draw(Surface buffer, int offsetX, int offsetY, int layer)
	{
		float2 pos = getPosition();
		pos = pos.subtract(
				new float2(
						mAnimationCoin.getFrameWidth(),
						mAnimationCoin.getFrameHeight()).divide(2));

		pos = pos.add(new float2(offsetX, offsetY));

		if (!mTemporary || (mLifeTime/10)%2 == 0 )
		{
			if (mFrame > 180)
			{
				mFrame = 0;
			}

			mAnimationCoin.drawFrame(buffer, (mFrame < 5*4)?mFrame/5:0, (int)pos.x, (int)pos.y, false, false);
		}
	}

	//		virtual void onRespawn();
	@Override
	public void onRespawn()
	{
		if (mType == Type.DYNAMIC)
			remove();
	}
	
	@Override
	public int getLayer() {
		return 3;
	}

	public static void SpawnDeathCoins(int aNumberOfCoins, float2 aCenterPosition,
			int aLifeTime, Room aRoom) {
		for (int i = 0; i < aNumberOfCoins; i++)
		{

			float2 velocity = UtilMethods.sincos(3.1415 * (i+1)/(aNumberOfCoins+1));
			velocity.x *= 100;
			velocity.y *= -200;

			Coin coin = new Coin();
			coin.setLifeTime(aLifeTime);
			coin.setPosition(aCenterPosition);
			coin.setVelocity(velocity);
			aRoom.addEntity(coin);
			Sound.playSample("data/sounds/coin");
		}
	}
}