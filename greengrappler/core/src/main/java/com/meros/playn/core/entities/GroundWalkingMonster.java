package com.meros.playn.core.entities;

import java.util.EnumSet;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.Constants.Direction;
import com.meros.playn.core.Entity;
import com.meros.playn.core.PlayerSkill;
import com.meros.playn.core.Resource;
import com.meros.playn.core.Sound;
import com.meros.playn.core.UtilMethods;
import com.meros.playn.core.float2;

public class GroundWalkingMonster extends Entity {

	private enum Facing {
		LEFT_UP, RIGHT_DOWN
	}

	public enum Type {
		FLOOR, LEFT_WALL, RIGHT_WALL, ROOF
	}

	private final float MAX_WALKING_SPEED = 70.0f;

	private final float MIN_WALKING_SPEED = 40.0f;

	Animation myAnimation = Resource.getAnimation("data/images/saw.bmp", 2);
	private Facing myFacing = Facing.RIGHT_DOWN;

	int myFrame = 0;

	private Type myType;

	public GroundWalkingMonster(Type aType) {
		myType = aType;
		setSize(new float2(10, 10));
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer) {
		float2 pos = getPosition();
		pos = pos.subtract(new float2(myAnimation.getFrameWidth(), myAnimation
				.getFrameHeight()).divide(2));
		pos = pos.add(new float2(offsetX, offsetY));

		myAnimation.drawFrame(buffer, myFrame / 3, (int) pos.x, (int) pos.y);
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public void update() {
		Hero hero = mRoom.getHero();

		float2 vel;

		float adjustedSpeed = UtilMethods.lerp(MIN_WALKING_SPEED,
				MAX_WALKING_SPEED, PlayerSkill.get());

		if (myType == Type.FLOOR || myType == Type.ROOF) {
			if (myFacing == Facing.LEFT_UP)
				vel = new float2(-adjustedSpeed, 0);
			else
				vel = new float2(adjustedSpeed, 0);
		} else {
			if (myFacing == Facing.LEFT_UP)
				vel = new float2(0, -adjustedSpeed);
			else
				vel = new float2(0, adjustedSpeed);
		}

		setVelocity(vel);

		EnumSet<Direction> bumps = moveWithCollision();

		if (bumps.contains(Direction.LEFT) || bumps.contains(Direction.UP)) {
			myFacing = Facing.RIGHT_DOWN;
		}

		if (bumps.contains(Direction.RIGHT) || bumps.contains(Direction.DOWN)) {
			myFacing = Facing.LEFT_UP;
		}

		int offsetX;
		int offsetY;

		if (myType == Type.FLOOR || myType == Type.ROOF) {
			offsetX = (int) ((myFacing == Facing.LEFT_UP) ? -getHalfSize().x - 2
					: getHalfSize().x + 2);
			offsetY = (int) ((myType == Type.FLOOR) ? getHalfSize().y + 2
					: -getHalfSize().y - 2);
		} else {
			offsetX = (int) ((myType == Type.LEFT_WALL) ? -getHalfSize().x - 2
					: getHalfSize().x + 2);
			offsetY = (int) ((myFacing == Facing.RIGHT_DOWN) ? getHalfSize().y + 2
					: -getHalfSize().y - 2);
		}

		float2 position = getPosition();

		int x = (int) ((position.x + offsetX) / mRoom.getTileWidth());
		int y = (int) ((position.y + offsetY) / mRoom.getTileHeight());

		if (!mRoom.isCollidable(x, y)) {
			if (myFacing == Facing.LEFT_UP) {
				myFacing = Facing.RIGHT_DOWN;
			} else {
				myFacing = Facing.LEFT_UP;
			}
		}

		if (hero.Collides(this)) {
			hero.kill();
		}

		if (hero.hasHook()
				&& hero.getHookCollisionRect().Collides(getCollisionRect())) {
			Sound.playSample("data/sounds/hook");
			hero.detachHook();
		}

		myFrame++;
	}

}
