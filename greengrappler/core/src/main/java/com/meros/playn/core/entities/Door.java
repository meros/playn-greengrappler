package com.meros.playn.core.entities;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.Entity;
import com.meros.playn.core.Resource;
import com.meros.playn.core.float2;

public class Door extends Entity {

	private int myId;
	private boolean myOpening = false;
	private boolean myClosing = false;
	private int myDoorHeight = 40;
	private int myFrameCounter = 0;
	
	private Animation myDoor = Resource.getAnimation("data/images/door.bmp", 1);
	
	public Door(int aId)
	{
		myId = aId;
		setSize(new float2(10, 40));
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer )
	{
		int x = (int) (getDrawPositionX() + offsetX - getSize().x / 2);
		int y = (int) (getDrawPositionY() + offsetY - getSize().y / 2);

		buffer.drawImage(myDoor.getFrame(0), (float)x, (float)y, (float)10, (float)myDoorHeight, 0, 0 , 10, myDoorHeight);
	}

	@Override
	public void update()
	{

		myFrameCounter++;

		if (myOpening && myDoorHeight != 4)
		{
			myDoorHeight-=2;
		}
		else if (myOpening)
		{
			myFrameCounter = 0;
		}

		if (myClosing && myFrameCounter % 5 == 0 &&  myDoorHeight != 40)
		{
			myDoorHeight++;
		}

		int tileX = (int)(getPosition().x - getSize().x / 2) / 10;
		int tileY = (int)(getPosition().y - getSize().y / 2) / 10;

		for (int i = 0; i < 4; i++)
		{
			if (i > myDoorHeight / 10)
			{
				mRoom.setCollidable(tileX, tileY + i, false);
			}
			else
			{
				mRoom.setCollidable(tileX, tileY + i, true);
			}
		}
	}

	public void onButtonDown( int aId )
	{
		if (myId != aId)
			return;

		myFrameCounter = 0;
		myOpening = true;
		myClosing = false;
	}

	public void onButtonUp( int aId )
	{
		if (myId != aId)
			return;

		myClosing = true;
		myOpening = false;
		myFrameCounter = 0;
	}

	public void onRespawn()
	{
		myOpening = false;
		myClosing = false;
		myDoorHeight = 40;
	}


	@Override
	public int getLayer() {
		return 0;
	}
}
