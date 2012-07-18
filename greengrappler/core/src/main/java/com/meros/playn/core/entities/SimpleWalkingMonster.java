package com.meros.playn.core.entities;

import java.util.EnumSet;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.Constants.Direction;
import com.meros.playn.core.Entity;
import com.meros.playn.core.PlayerSkill;
import com.meros.playn.core.Resource;
import com.meros.playn.core.Sound;
import com.meros.playn.core.ImmutableFloatPair;

public class SimpleWalkingMonster extends Entity {

	enum Facing {
		LEFT, RIGHT
	}

	enum State {
		IDLING, WALKING
	}

	float IDLE_TO_WALK_CHANCE = 0.05f;

	Animation myAnimation = Resource.getAnimation("data/images/robot.bmp", 4);

	Facing myFacing = Facing.LEFT;

	int myFrame = 0;

	State myState = State.IDLING;

	float WALK_TO_IDLE_CHANCE = 0.01f;

	float WALKING_SPEED = 14.0f;

	public SimpleWalkingMonster() {
		setSize(new ImmutableFloatPair(20, 20));
	}

	public void die() {
		Sound.playSample("data/sounds/damage");

		ParticleSystem ps = new ParticleSystem(Resource.getAnimation(
				"data/images/debris.bmp", 4), 10, 30, 10, 1, 50, 5, new ImmutableFloatPair(
				0.0f, -30.0f), 2.0f);
		ps.setPosition(getPosition(), 5.0f, false);
		mRoom.addEntity(ps);
		remove();
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer) {
		ImmutableFloatPair pos = getPosition().subtract(
				new ImmutableFloatPair(myAnimation.getFrameWidth(), myAnimation
						.getFrameHeight()).divide(2)).add(
				new ImmutableFloatPair(offsetX, offsetY));

		myAnimation.drawFrame(buffer, myFrame / 15, (int) pos.x, (int) pos.y,
				myFacing == Facing.RIGHT, false);
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean isDamagable() {
		return true;
	}

	@Override
	public void onDamage() {
		PlayerSkill.playerDidSomethingClever(0.5f, 0.1f);
		die();
	}

	@Override
	public void update() {
		switch (myState) {
		case WALKING: {
			
			mVelocity = new ImmutableFloatPair(
					20.0f * ((myFacing == Facing.LEFT) ? -1 : 1),
					mVelocity.y + 6.0f);

			setVelocity(mVelocity);
			EnumSet<Direction> bumps = moveWithCollision();

			if (bumps.contains(Direction.UP) || bumps.contains(Direction.DOWN)) {
				mVelocity = new ImmutableFloatPair(mVelocity.x, 0);
			}

			if (bumps.contains(Direction.LEFT)) {
				myFacing = Facing.RIGHT;
			}

			if (bumps.contains(Direction.RIGHT)) {
				myFacing = Facing.LEFT;
			}

			int offsetX = (int) ((myFacing == Facing.RIGHT) ? -getHalfSize().x - 2
					: getHalfSize().x + 2);
			int offsetY = (int) (getHalfSize().y + 2);

			ImmutableFloatPair position = getPosition();

			int x = (int) ((position.x + offsetX) / mRoom.getTileWidth());
			int y = (int) ((position.y + offsetY) / mRoom.getTileHeight());

			if (!mRoom.isCollidable(x, y)) {
				if (myFacing == Facing.LEFT) {
					myFacing = Facing.RIGHT;
				} else {
					myFacing = Facing.LEFT;
				}
			}

			if (Math.random() < WALK_TO_IDLE_CHANCE) {
				myState = State.IDLING;
			}
		}

			break;
		case IDLING:

			if (Math.random() < IDLE_TO_WALK_CHANCE) {
				if (Math.random() > 0.5) {
					myFacing = Facing.LEFT;
				} else {
					myFacing = Facing.RIGHT;
				}
				myState = State.WALKING;
			}

			break;
		}

		Hero hero = mRoom.getHero();
		if (hero.Collides(this)) {
			hero.kill();
		}

		myFrame++;
	}
}
