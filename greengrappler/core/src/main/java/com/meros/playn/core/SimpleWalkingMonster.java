package com.meros.playn.core;

import playn.core.Canvas;

import com.meros.playn.core.Constants.Direction;

public class SimpleWalkingMonster extends Entity {

	float WALKING_SPEED = 14.0f;
	float WALK_TO_IDLE_CHANCE = 0.01f;
	float IDLE_TO_WALK_CHANCE = 0.05f;

	enum State
	{
		WALKING,
		IDLING
	}

	State myState = State.IDLING;

	enum Facing
	{
		RIGHT,
		LEFT
	}

	Facing myFacing = Facing.LEFT;

	int myFrame = 0;
	
	Animation myAnimation = Resource.getAnimation("data/images/robot.bmp", 4);	

	public SimpleWalkingMonster()
	{
		setSize(new float2(20,20));
	}

	@Override
	public void update()
	{
		switch(myState)
		{
		case WALKING:
		{
			mVelocity.y += 6.0f;
			mVelocity.x = 20.0f * ((myFacing==Facing.LEFT)?-1:1);

			setVelocity(mVelocity);
			int bumps = moveWithCollision();

			if ((bumps & (Direction.Up.value | Direction.Down.value)) != 0) 
			{
				mVelocity.y = 0;
			}

			if ((bumps & (Direction.Left.value)) != 0)
			{
				myFacing = Facing.RIGHT;
			}

			if ((bumps & (Direction.Right.value)) != 0)
			{
				myFacing = Facing.LEFT;
			}


			int offsetX = (int) ((myFacing==Facing.RIGHT)?-getHalfSize().x-2:getHalfSize().x+2);
			int offsetY = (int) (getHalfSize().y+2);

			float2 position = getPosition();

			int x = (int) ((position.x+offsetX) / (float)mRoom.getTileWidth());
			int y = (int) ((position.y+offsetY) / (float)mRoom.getTileHeight());

			if (!mRoom.isCollidable(x, y))
			{
				if (myFacing == Facing.LEFT)
				{
					myFacing = Facing.RIGHT;
				}
				else
				{
					myFacing = Facing.LEFT;
				}
			}

			if (Math.random() < WALK_TO_IDLE_CHANCE)
			{
				myState = State.IDLING;
			}
		}

		break;
		case IDLING:

			if (Math.random() < IDLE_TO_WALK_CHANCE)
			{
				if (Math.random() > 0.5)
				{
					myFacing = Facing.LEFT;
				}
				else
				{
					myFacing = Facing.RIGHT;
				}
				myState = State.WALKING;
			}

			break;
		}

		Hero hero = mRoom.getHero();
		if (Collides(hero.getCollisionRect(), getCollisionRect()))
		{
			hero.kill();
		}

		myFrame++;
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 3;
	}
	
	@Override
	public void draw(Canvas buffer, int offsetX, int offsetY, int layer)
	{
		float2 pos = getPosition().subtract(
				new float2(
						myAnimation.getFrameWidth(), 
						myAnimation.getFrameHeight()).divide(2)).add(
								new float2(offsetX, offsetY));

		myAnimation.drawFrame(buffer, myFrame/15, (int)pos.x, (int)pos.y, myFacing == Facing.RIGHT, false);
	}

	@Override
	public boolean isDamagable()
	{
		return true;
	}
	
	@Override
	public void onDamage()
	{
		PlayerSkill.playerDidSomethingClever(0.5f, 0.1f);
		die();
	}

	public void die()
	{
		Sound.playSample("data/sounds/damage");

		ParticleSystem ps = new ParticleSystem(Resource.getAnimation("data/images/debris.bmp", 4), 10, 30, 10, 1, 50, 5, new float2(0.0f, -30.0f), 2.0f);
		ps.setPosition(getPosition(), 5.0f, false);
		mRoom.addEntity(ps);
		remove();
	}
}
