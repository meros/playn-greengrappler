package com.meros.playn.core;

import java.util.EnumSet;

import playn.core.Canvas;

import com.meros.playn.core.Constants.Direction;

public class ReactorCore extends Entity {

	Animation mAnimation = Resource.getAnimation("data/images/core.bmp", 3); 
	int mFrame = 0;

	@Override
	public void update()
	{
		Hero hero = mRoom.getHero();

		mVelocity.y += 6.0;
		mVelocity.x = 0;

		EnumSet<Direction> bumps = moveWithCollision();

		if (bumps.contains(Direction.UP) || bumps.contains(Direction.DOWN)) {
			mVelocity.y *= -0.8;
		}

		if (Collides(hero.getCollisionRect(), getCollisionRect()))
		{
			if (hero.gotCore())
			{
				PlayerSkill.playerDidSomethingClever(1.0f, 0.75f);
				mRoom.setCompleted();
				remove();
			}
		}

		mFrame++;
	}
	
	
	public ReactorCore()
	{
		setSize(new float2(30,30));
	}

	@Override
	public void draw( Canvas buffer, int offsetX, int offsetY, int layer )
	{
		//Entity::draw(buffer, offsetX, offsetY, layer);
		float2 pos = getPosition();
		pos = pos.subtract(new float2(mAnimation.getFrameWidth(), mAnimation.getFrameHeight()).divide(2));
		pos = pos.add(new float2(offsetX, offsetY));

		mAnimation.drawFrame(buffer, mFrame/5, (int)pos.x, (int)pos.y);
	}
	
	@Override
	public int getLayer() {
		return 1;
	}

}
