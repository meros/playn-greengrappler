package se.darkbits.greengrappler.entities;

import playn.core.Surface;
import se.darkbits.greengrappler.Entity;
import se.darkbits.greengrappler.floatpair.ImmutableFloatPair;


public class WallOfDeathStarter extends Entity {

	private boolean myUsed = false;

	public WallOfDeathStarter() {
		setSize(new ImmutableFloatPair(10, 10 * 100));
	}

	@Override
	public void onRespawn() {
		myUsed = false;
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer) {
		// Entity::draw(buffer, offsetX, offsetY, layer);
	}

	@Override
	public void update() {
		if (!myUsed && myRoom.getHero().Collides(this)) {
			myRoom.broadcastStartWallOfDeath();
			myUsed = true;
		}
	}

	@Override
	public int getLayer() {
		return 1;
	}
}
