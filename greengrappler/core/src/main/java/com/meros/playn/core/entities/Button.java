package com.meros.playn.core.entities;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.Entity;
import com.meros.playn.core.Resource;
import com.meros.playn.core.Sound;
import com.meros.playn.core.float2;

public class Button extends Entity {

	private boolean mTriggered = false;
	private int myTime = 60*5;
	private int myCounter = myTime + 1;
	private int myId;
	private Animation myButton = Resource.getAnimation("data/images/button.bmp");

	public Button(int aId)
	{
		myId = aId;
		setSize(new float2(10, 10));
	}

	@Override
	public void update()
	{
		myCounter++;

		if (myCounter == myTime)
		{
			Sound.playSample("data/sounds/timeout");
			mRoom.broadcastButtonUp(myId);
			return;
		}

		if (myCounter < myTime)
		{	
			if (myCounter % 60 == 0)
				Sound.playSample("data/sounds/time");
			return;
		}

		if (mRoom.getHero().Collides(this))
		{
			if (!mTriggered)
			{
				mTriggered = true;
				myCounter = 0;
				Sound.playSample("data/sounds/time");
				mRoom.broadcastButtonDown(myId);
			}
		}
		else
		{
			mTriggered = false;
		}
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer )
	{
		int x = getDrawPositionX() + offsetX;
		int y = getDrawPositionY() + offsetY;

		int frame = 1;
		if (myCounter > myTime)
		{
			frame = 0;
		}
		myButton.drawFrame(buffer, frame, x - (int)getSize().x / 2, y - (int)getSize().y / 2);

		//Entity::draw(buffer, offsetX, offsetY, layer);
	}

	@Override
	public void onRespawn()
	{
		mTriggered = false;
		myCounter = myTime + 1;
	}

	@Override
	public int getLayer() {
		return 0;
	}

}
