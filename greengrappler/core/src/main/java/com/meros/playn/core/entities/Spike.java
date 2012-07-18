package com.meros.playn.core.entities;

import playn.core.Image;
import playn.core.Surface;

import com.meros.playn.core.Entity;
import com.meros.playn.core.Resource;
import com.meros.playn.core.ImmutableFloatPair;

public class Spike extends Entity {

	Image mySpikeTile = Resource.getBitmap("data/images/tileset1.bmp").subImage(70, 0, 10, 10);

	public Spike() {
		setSize(new ImmutableFloatPair(10, 10));
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer) {
		buffer.drawImage(mySpikeTile, 
				offsetX + getPosition().getX() - getHalfSize().getX(), 
				offsetY + getPosition().getY() - getHalfSize().getY());
	}

	@Override
	public int getLayer() {
		return 3;
	}

	@Override
	public void update() {
		Hero hero = mRoom.getHero();

		if (hero.Collides(this)) {
			hero.kill();
		}
	}
}
