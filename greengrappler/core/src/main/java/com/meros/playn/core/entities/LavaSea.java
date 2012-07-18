package com.meros.playn.core.entities;

import java.util.Random;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.CollisionRect;
import com.meros.playn.core.Entity;
import com.meros.playn.core.Resource;
import com.meros.playn.core.ImmutableFloatPair;

public class LavaSea extends Entity {

	private int mFrame = 0;
	private boolean mSafeMode = false;
	private int mSafeLevel = 0;
	
	Animation mTopAnimation = Resource.getAnimation("data/images/lavatop.bmp",2);
	Animation mFillAnimation = Resource.getAnimation("data/images/lavafill.bmp",2);

	
	@Override
	public void update()
	{
		Hero hero = mRoom.getHero();

		if (hero.getCollisionRect().Collides(getCollisionRect()))
		{
			hero.kill();
		}

		mFrame++;

		if (mSafeMode)
		{
			mSafeLevel ++;
			if (mSafeLevel > getPosition().y + 10)
			{
				mSafeLevel = (int) (getPosition().y + 10);
			}
		}

		if (Math.random() < 0.3)
		{
			ParticleSystem particleSystem = new ParticleSystem(
				Resource.getAnimation("data/images/particles.bmp"),
				2,
				30,
				10,
				1,
				50,
				10,
				new ImmutableFloatPair(0, -30),
				5.0f);
			
			Random random = new Random();

			int min = (int) -mRoom.getCamera().getOffset().x;
			int max = (int) (320-mRoom.getCamera().getOffset().x);
			int rVal = random.nextInt(max-min)+min;
			
			ImmutableFloatPair pos = new ImmutableFloatPair(rVal, getCurrentY());
			particleSystem.setPosition(pos, 5, false);

			mRoom.addEntity(particleSystem);
		}
	}
	
	@Override
	public void draw( Surface buffer, int offsetX, int offsetY, int layer )
	{
		int screenY = offsetY+getCurrentY();

		for (int x = -10 + offsetX%10; x <320; x+= 10)
		{
			mTopAnimation.drawFrame(buffer, mFrame/30, x, screenY);
		}

		for (int x = -10 + offsetX%10; x <320; x+= 10)
		{
			for (int y = screenY + 10; y < 240; y += 10)
			{
				mFillAnimation.drawFrame(buffer, mFrame/30, x, y);
			}
		}
	}


	@Override
	public CollisionRect getCollisionRect()
	{
		CollisionRect rect = new CollisionRect();
		rect.myTopLeft = new ImmutableFloatPair(0, getCurrentY());
		rect.myBottomRight = new ImmutableFloatPair(10000000, 10000000);

		return rect;
	}

	private int getCurrentY()
	{
		if (mSafeMode)
		{
			return mSafeLevel;
		}

		float sinwave = (float) ((Math.sin(mFrame/100.0f)+1)/2);
		sinwave = sinwave*sinwave*sinwave ;
		return (int) (getPosition().y - sinwave*80 + 2);
	}

	@Override
	public void onLevelComplete()
	{
		mSafeLevel = getCurrentY();
		mSafeMode = true;
	}
	
	@Override
	public int getLayer() {
		return 4;
	}

}
