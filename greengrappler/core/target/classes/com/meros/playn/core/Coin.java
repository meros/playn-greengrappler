package com.meros.playn.core;

import static com.meros.playn.core.Constants.*;
import playn.core.Canvas;

public class Coin extends Entity {
	
	int mLifeTime = 0;
	enum Type { Type_Static, Type_Dynamic }
	Type mType = Type.Type_Static;
	boolean mTemporary = false;
	int mFrame = 0;
	Animation mAminationCoin;
	
	Hero hero = new Hero(); //TODO: should not be initialized like this
	
//		Coin();
	public Coin()
	{
		mAminationCoin = Resource.getAnimation("data/images/coin.bmp", 4);
		setSize(new float2(12,12));
	}
//
//		virtual void setLifeTime(int aLifeTime);
	public void setLifeTime(int aLifeTime)
	{
		mLifeTime = aLifeTime;
		mType = Type.Type_Dynamic;
		mTemporary = aLifeTime != 0;
	}
//
//		static void SpawnDeathCoins( int aNumberOfCoins, float2 aCenterPosition, int aLifeTime, Room* aRoom);
//
//		virtual int getLayer(){return 0;}
//
//		virtual void update();
	public void update() 
	{
		//Hero* hero = mRoom->getHero();

		if (mType == Type.Type_Dynamic)
		{
			if (mTemporary && mLifeTime == 0)
			{
				remove();
			}
			else
			{
				mVelocity.y += 6.0;

				int bumps = moveWithCollision();

				if ((bumps & (Direction_Left | Direction_Right)) != 0) {
					if (Math.abs(mVelocity.x) > 10){
						Sound.playSample("data/sounds/coin");
					}
					mVelocity.x *= -0.5;
				}

				if ((bumps & (Direction_Up | Direction_Down)) != 0) {
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
				Sound.playSample("data/sounds/coin.wav");
				remove();
			}
		}

		mFrame++;
	}
//		virtual void draw(BITMAP *buffer, int offsetX, int offsetY, int layer);
	public void draw(Canvas buffer, int offsetX, int offsetY, int layer)
	{
		float2 pos = getPosition();
		pos = pos.subtract(
				new float2(
						mAminationCoin.getFrameWidth(),
						mAminationCoin.getFrameHeight()).divide(2));
		pos = pos.add(new float2(offsetX, offsetY));

		if (!mTemporary || (mLifeTime/10)%2 == 0 )
		{
			if (mFrame > 180)
			{
				mFrame = 0;
			}

			mAminationCoin.drawFrame(buffer, (mFrame < 5*4)?mFrame/5:0, (int)pos.x, (int)pos.y);
		}
	}
	
//		virtual void onRespawn();
	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 0;
	}
}