package com.meros.playn.core.entities;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.Entity;
import com.meros.playn.core.PlayerSkill;
import com.meros.playn.core.Resource;
import com.meros.playn.core.float2;

public class SpawnPoint extends Entity {
	
	private enum State
	{
		UNCHECKED(0),
		SEMI_CHECKED(1),
		CHECKED(2);
		
		public int value;
		
		State(int aValue)
		{
			value = aValue;
		}
	}
	
	private State myState = State.UNCHECKED;
	private int myFrame = 0;
	private Animation myAnimation = Resource.getAnimation("data/images/checkpoint.bmp", 3);

	
	public void update()
	{
	 	Hero hero = mRoom.getHero();
		
		if (hero.getCollisionRect().Collides(getCollisionRect()))
		{
			if (myState == State.UNCHECKED) {
				PlayerSkill.playerDidSomethingClever(0.7f, 0.5f);
			}

			myState = State.SEMI_CHECKED;
			hero.setLastSpawnPoint(getPosition());
		}

		if (myState == State.SEMI_CHECKED)
		{
			myFrame++;
		}

		if (myFrame > 5)
		{
			myState = State.CHECKED;
		}
	}

	public SpawnPoint()
	{
		setSize(new float2(22,25));
	}

	public void draw(Surface buffer, int offsetX, int offsetY, int layer )
	{
		float2 pos = getPosition()
				.subtract(new float2(myAnimation.getFrameWidth(), myAnimation.getFrameHeight()).divide(2))
				.add(new float2(offsetX, offsetY));

		myAnimation.drawFrame(buffer, myState.value, (int)pos.x, (int)pos.y);
	}
	
	@Override
	public int getLayer() {
		return 3;
	}

}
