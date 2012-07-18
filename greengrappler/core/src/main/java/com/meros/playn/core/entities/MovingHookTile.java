package com.meros.playn.core.entities;

import java.util.EnumSet;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.Constants.Direction;
import com.meros.playn.core.Entity;
import com.meros.playn.core.Room;
import com.meros.playn.core.float2;

public class MovingHookTile extends Entity {

	private Animation mSprite = new Animation("data/images/movinghooktile.bmp", 2);
	private boolean hasHook = false;
	private float2 mInitialPosition;
	private float2 mInitialVelocity;
	
	public MovingHookTile()
	{
		setSize(new float2(mSprite.getFrameHeight(), mSprite.getFrameHeight()));
		mVelocity = new float2(20.0f, 0.0f);
	}

	@Override
	public void setRoom(Room room)
	{
		mInitialPosition = getPosition();
		mInitialVelocity = mVelocity;
		super.setRoom(room);
	}

	@Override
	public void onRespawn()
	{
		setPosition(mInitialPosition);
		mVelocity = mInitialVelocity;
		hasHook = false;
	}

	@Override
	public void update()
	{
		super.update();

		hasHook = (mRoom.getHero().getHookedEntity() == this);

		if (hasHook) {
			mVelocity = new float2((mVelocity.x > 0) ? 40.0f : -40.0f, mVelocity.y);
			mFrameCounter++;
		} else {
			mVelocity = new float2((mVelocity.x > 0) ? 20.0f : -20.0f, mVelocity.y);
		}

		EnumSet<Direction> bumps = moveWithCollision();

		if (bumps.contains(Direction.LEFT) || bumps.contains(Direction.RIGHT)) {
			mVelocity = new float2(-mVelocity.x, mVelocity.y);
		}
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer)
	{
		int x = getDrawPositionX() + offsetX - mSprite.getFrameWidth() / 2;
		int y = getDrawPositionY() + offsetY - mSprite.getFrameHeight() / 2;
		mSprite.drawFrame(buffer, mFrameCounter / 12, x, y);
	}

	@Override
	public boolean isHookable()
	{
		return true;
	}

	@Override
	public int getLayer() {
		return 0;
	}

}
