package com.meros.playn.core.entities;

import java.util.EnumSet;

import playn.core.Surface;

import com.meros.playn.core.Constants.Direction;
import com.meros.playn.core.Entity;
import com.meros.playn.core.Room;
import com.meros.playn.core.floatpair.ImmutableFloatPair;
import com.meros.playn.core.media.Animation;

public class MovingHookTile extends Entity {

	private Animation mSprite = new Animation("data/images/movinghooktile.bmp",
			2);
	private boolean hasHook = false;
	private ImmutableFloatPair mInitialPosition;
	private ImmutableFloatPair mInitialVelocity;

	public MovingHookTile() {
		setSize(new ImmutableFloatPair(mSprite.getFrameHeight(),
				mSprite.getFrameHeight()));
		myVelocity.set(20.0f, 0.0f);
	}

	@Override
	public void setRoom(Room room) {
		mInitialPosition = new ImmutableFloatPair(getPosition());
		mInitialVelocity = new ImmutableFloatPair(myVelocity);
		super.setRoom(room);
	}

	@Override
	public void onRespawn() {
		setPosition(mInitialPosition);
		myVelocity.set(mInitialVelocity);
		hasHook = false;
	}

	@Override
	public void update() {
		super.update();

		hasHook = (myRoom.getHero().getHookedEntity() == this);

		if (hasHook) {
			myVelocity.set((myVelocity.getX() > 0) ? 40.0f : -40.0f,
					myVelocity.getY());
			myFrameCounter++;
		} else {
			myVelocity.set((myVelocity.getX() > 0) ? 20.0f : -20.0f,
					myVelocity.getY());
		}

		EnumSet<Direction> bumps = moveWithCollision();

		if (bumps.contains(Direction.LEFT) || bumps.contains(Direction.RIGHT)) {
			myVelocity.set(-myVelocity.getX(), myVelocity.getY());
		}
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer) {
		int x = getDrawPositionX() + offsetX - mSprite.getFrameWidth() / 2;
		int y = getDrawPositionY() + offsetY - mSprite.getFrameHeight() / 2;
		mSprite.drawFrame(buffer, myFrameCounter / 12, x, y);
	}

	@Override
	public boolean isHookable() {
		return true;
	}

	@Override
	public int getLayer() {
		return 0;
	}

}
