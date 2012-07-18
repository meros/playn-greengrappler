package com.meros.playn.core.entities;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.Constants.Direction;
import com.meros.playn.core.Entity;
import com.meros.playn.core.Resource;
import com.meros.playn.core.Room;
import com.meros.playn.core.ImmutableFloatPair;

public class BossWall extends Entity {

	private Animation myWall = Resource.getAnimation("data/images/wall.bmp", 1); 
	private Direction myDirection;
	private int myFrameCounter = 0;
	private boolean myActive = false;

	public BossWall(Direction aDirection)
	{
		myDirection = aDirection;
		setSize(new ImmutableFloatPair(10, 100));
	}
	
	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer )
	{
		int x = (int) (getDrawPositionX() + offsetX - getHalfSize().getX());
		int y = (int) (getDrawPositionY() + offsetY - getHalfSize().getY());

		myWall.drawFrame(buffer, 0, x, y, myDirection == Direction.LEFT, false);
		//Entity::draw(buffer, offsetX, offsetY, layer);
	}

	@Override
	public void update()
	{
		myFrameCounter++;

		if (mRoom.getHero().Collides(this))
		{
			mRoom.getHero().kill();
		}

		if (!myActive)
			return;

		if (myDirection == Direction.RIGHT && myFrameCounter % 200 == 0)
		{
			BossSaw saw = new BossSaw(myDirection);
			saw.setPosition(new ImmutableFloatPair(getPosition().getX(), getPosition().getY() + getHalfSize().getY() - 10));
			mRoom.addEntity(saw);
		}

		if (myDirection == Direction.RIGHT && (100 + myFrameCounter) % 200 == 0)
		{
			BossSaw saw = new BossSaw(myDirection);
			saw.setPosition(new ImmutableFloatPair(getPosition().getX(), getPosition().getY() + getHalfSize().getY() - 30));
			mRoom.addEntity(saw);
		}

		if (myDirection == Direction.RIGHT && (180 + myFrameCounter) % 200 == 0)
		{
			BossSaw saw = new BossSaw(myDirection);
			saw.setPosition(new ImmutableFloatPair(getPosition().getX(), getPosition().getY()  + getHalfSize().getY() - 50));
			mRoom.addEntity(saw);
		}

		if (myDirection == Direction.LEFT && myFrameCounter % 350 == 0)
		{
			BossSaw saw = new BossSaw(myDirection);
			saw.setPosition(new ImmutableFloatPair(getPosition().getX(), getPosition().getY() + getHalfSize().getY() - 10));
			mRoom.addEntity(saw);
		}

		if (myDirection == Direction.LEFT && (100 + myFrameCounter) % 350 == 0)
		{
			BossSaw saw = new BossSaw(myDirection);
			saw.setPosition(new ImmutableFloatPair(getPosition().getX(), getPosition().getY() + getHalfSize().getY() - 30));
			mRoom.addEntity(saw);
		}

		if (myDirection == Direction.LEFT && (300 + myFrameCounter) % 350 == 0)
		{
			BossSaw saw = new BossSaw(myDirection);
			saw.setPosition(new ImmutableFloatPair(getPosition().getX(), getPosition().getY() + getHalfSize().getY() - 50));
			mRoom.addEntity(saw);
		}

	}


	public void setTilesCollidable( 
		boolean aCollidable )
	{
		int sx = (int) ((getPosition().getX()-getHalfSize().getX())/10);
		int sy = (int) ((getPosition().getY()-getHalfSize().getY())/10);
		int height = (int) ((getSize().getY()) / 10);

		for (int y = sy; y < sy + height; y++)
			mRoom.setCollidable(sx, y, aCollidable);
	}

	@Override
	public void setRoom( Room room )
	{
		super.setRoom(room);
		setTilesCollidable(true);
	}

	@Override
	public void onBossWallActivate()
	{
		myActive = true;
		myFrameCounter = 0;
	}

	@Override
	public void onRespawn()
	{
		myActive = false;
	}

	@Override
	public void onBossWallDeactivate()
	{
		myActive = false;
	}

	@Override
	public int getLayer() {
		return 3;
	}

}
