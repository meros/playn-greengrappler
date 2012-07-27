package se.darkbits.greengrappler.entities;

import playn.core.Image;
import playn.core.Surface;
import se.darkbits.greengrappler.Entity;
import se.darkbits.greengrappler.Resource;
import se.darkbits.greengrappler.floatpair.ImmutableFloatPair;


public class Spike extends Entity {

	Image mySpikeTile = Resource.getBitmap("data/images/tileset1.bmp")
			.subImage(70, 0, 10, 10);

	public Spike() {
		setSize(new ImmutableFloatPair(10, 10));
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer) {
		buffer.drawImage(mySpikeTile, offsetX + getPosition().getX()
				- getHalfSize().getX(), offsetY + getPosition().getY()
				- getHalfSize().getY());
	}

	@Override
	public int getLayer() {
		return 3;
	}

	@Override
	public void update() {
		Hero hero = myRoom.getHero();

		if (hero.Collides(this)) {
			hero.kill();
		}
	}
}
