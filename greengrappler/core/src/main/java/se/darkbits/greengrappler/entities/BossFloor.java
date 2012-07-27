package se.darkbits.greengrappler.entities;

import playn.core.Surface;
import se.darkbits.greengrappler.Entity;
import se.darkbits.greengrappler.floatpair.ImmutableFloatPair;


public class BossFloor extends Entity {

	private boolean myActive = false;
	private int myFrameCounter = 0;

	public BossFloor() {
		setSize(new ImmutableFloatPair(320, 10));
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer) {
		// Entity::draw(buffer, offsetX, offsetY, layer);
	}

	@Override
	public void update() {
		myFrameCounter++;

		if (!myActive)
			return;

		if (myRoom.getHero().Collides(this)) {
			myRoom.getHero().kill();
		}

		if (myFrameCounter > 60)
			myActive = false;
	}

	@Override
	public int getLayer() {
		return 4;
	}

	@Override
	public float getCollideTop() {
		return super.getCollideTop() - 1;
	}

	@Override
	public void onBossFloorActivate() {
		myActive = true;
		myFrameCounter = 0;
	}

	@Override
	public void onRespawn() {
		myActive = false;
	}

}
