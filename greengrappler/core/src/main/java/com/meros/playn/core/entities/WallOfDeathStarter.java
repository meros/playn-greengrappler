package com.meros.playn.core.entities;

import playn.core.Surface;

import com.meros.playn.core.Entity;
import com.meros.playn.core.float2;

public class WallOfDeathStarter extends Entity {

	private boolean myUsed = false;

	public WallOfDeathStarter()
	{
		setSize(new float2(10, 10 * 100));
	}

	public void onRespawn()
	{
		myUsed = false;
	}

	@Override
	public void draw( Surface buffer, int offsetX, int offsetY, int layer )
	{
		//Entity::draw(buffer, offsetX, offsetY, layer);
	}

	@Override
	public void update()
	{
		if (!myUsed && mRoom.getHero().Collides(this))
		{
			mRoom.broadcastStartWallOfDeath();
			myUsed = true;
		}
	}

	@Override
	public int getLayer() {
		return 1;
	}
}
