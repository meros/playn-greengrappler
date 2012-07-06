package com.meros.playn.core.entities;

import playn.core.Canvas;

import com.meros.playn.core.Entity;
import com.meros.playn.core.Resource;
import com.meros.playn.core.Tile;
import com.meros.playn.core.float2;

public class Spike extends Entity {

	Tile mySpikeTile = new Tile(Resource.getBitmap("data/images/tileset1.bmp"),
			70, 0, 10, 10);

	public Spike() {
		setSize(new float2(10, 10));
	}

	@Override
	public void draw(Canvas buffer, int offsetX, int offsetY, int layer) {
		float2 pos = getPosition().subtract(getHalfSize());
		mySpikeTile.onDraw(buffer, (int) (offsetX + pos.x),
				(int) (offsetY + pos.y));
	}

	@Override
	public int getLayer() {
		return 3;
	}

	@Override
	public void update() {
		Hero hero = mRoom.getHero();

		if (hero.getCollisionRect().Collides(getCollisionRect())) {
			hero.kill();
		}
	}
}
