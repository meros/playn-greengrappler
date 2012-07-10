package com.meros.playn.core.entities;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.Constants.Direction;
import com.meros.playn.core.Entity;
import com.meros.playn.core.Resource;
import com.meros.playn.core.Sound;
import com.meros.playn.core.float2;

public class BossSaw extends Entity {

	private int myLifeTime = 60*6;
	private int myFrameCounter = 0;
	private Animation mySaw = Resource.getAnimation("data/images/saw.bmp");
	private float2 mySpeed;

	public BossSaw(Direction aDirection)
	{
		if (aDirection == Direction.LEFT)
			mySpeed = new float2(-2.0f, 0);
		else
			mySpeed = new float2(2.0f, 0);

		Sound.playSample("data/sounds/boss_saw");
		setSize(new float2(mySaw.getFrameWidth(), mySaw.getFrameHeight()));
	}

	public void draw( Surface buffer, int offsetX, int offsetY, int layer )
	{
		int x = (int) (getDrawPositionX() + offsetX - getHalfSize().x);
		int y = (int) (getDrawPositionY() + offsetY - getHalfSize().y);

		mySaw.drawFrame(buffer, myFrameCounter / 10, x, y);

		//Entity::draw(buffer, offsetX, offsetY, layer);
	}

	public void update()
	{
		myFrameCounter++;

		if (myFrameCounter > myLifeTime)
			remove();

		setPosition(getPosition().add(mySpeed));


		if (mRoom.getHero().Collides(this))
		{
			mRoom.getHero().kill();
		}
	}

	public void onRespawn()
	{
		remove();
	}

	@Override
	public int getLayer() {
		return 3;
	}

}
