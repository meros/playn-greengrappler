package com.meros.playn.core.entities;

import java.util.EnumSet;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.Constants.Direction;
import com.meros.playn.core.Entity;
import com.meros.playn.core.PlayerSkill;
import com.meros.playn.core.Resource;
import com.meros.playn.core.float2;

public class ReactorCore extends Entity {

	Animation mAnimation = Resource.getAnimation("data/images/core.bmp", 3);
	int mFrame = 0;

	public ReactorCore() {
		setSize(new float2(30, 30));
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer) {
		// Entity::draw(buffer, offsetX, offsetY, layer);
		float2 pos = getPosition();
		pos = pos.subtract(new float2(mAnimation.getFrameWidth(), mAnimation
				.getFrameHeight()).divide(2));
		pos = pos.add(new float2(offsetX, offsetY));

		mAnimation.drawFrame(buffer, mFrame / 5, (int) pos.x, (int) pos.y);
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public void update() {
		Hero hero = mRoom.getHero();

		mVelocity = new float2(0, mVelocity.y + 6.0f);

		EnumSet<Direction> bumps = moveWithCollision();

		if (bumps.contains(Direction.UP) || bumps.contains(Direction.DOWN)) {
			mVelocity = new float2(mVelocity.x, mVelocity.y*0.8f);
		}

		if (hero.getCollisionRect().Collides(getCollisionRect())) {
			if (hero.gotCore()) {
				PlayerSkill.playerDidSomethingClever(1.0f, 0.75f);
				mRoom.setCompleted();
				remove();
			}
		}

		mFrame++;
	}

}
