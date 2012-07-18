package com.meros.playn.core.entities;

import java.util.EnumSet;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.Constants.Direction;
import com.meros.playn.core.Entity;
import com.meros.playn.core.Room;
import com.meros.playn.core.ImmutableFloatPair;

public class MovingHookTile extends Entity {

	private Animation mSprite = new Animation("data/images/movinghooktile.bmp", 2);
	private boolean hasHook = false;
	private ImmutableFloatPair mInitialPosition;
	private ImmutableFloatPair mInitialVelocity;
	
	public MovingHookTile()
	{
		setSize(new ImmutableFloatPair(mSprite.getFrameHeight(), mSprite.getFrameHeight()));
		mVelocity.set(20.0f, 0.0f);
	}

	@Override
	public void setRoom(Room room)
	{
		mInitialPosition = new ImmutableFloatPair(getPosition());
		mInitialVelocity = new ImmutableFloatPair(mVelocity);
		super.setRoom(room);
	}

	@Override
	public void onRespawn()
	{
		setPosition(mInitialPosition);
		mVelocity.set(mInitialVelocity);
		hasHook = false;
	}

	@Override
	public void update()
	{
		super.update();

		hasHook = (mRoom.getHero().getHookedEntity() == this);

		if (hasHook) {
			mVelocity.set((mVelocity.getX() > 0) ? 40.0f : -40.0f, mVelocity.getY());
			mFrameCounter++;
		} else {
			mVelocity.set((mVelocity.getX() > 0) ? 20.0f : -20.0f, mVelocity.getY());
		}

		EnumSet<Direction> bumps = moveWithCollision();

		if (bumps.contains(Direction.LEFT) || bumps.contains(Direction.RIGHT)) {
			mVelocity.set(-mVelocity.getX(), mVelocity.getY());
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
