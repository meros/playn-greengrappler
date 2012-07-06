package com.meros.playn.core;

import playn.core.Surface;

public class Spike extends Entity {
	
	Tile mySpikeTile = new Tile(Resource.getBitmap("data/images/tileset1.bmp"), 70, 0, 10, 10);

	public Spike()
	{
		setSize(new float2(10,10));
	}
	
	@Override
	public int getLayer() {
		return 3;
	}
	
	@Override
	public void update()
	{
	 	Hero hero = mRoom.getHero();
		
		if (Collides(hero.getCollisionRect(), getCollisionRect()))
		{
			hero.kill();
		}
	}
	
	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer)
	{
		float2 pos = getPosition().subtract(getHalfSize());
		//TODO: mySpikeTile.onDraw(buffer, (int)(offsetX+pos.x), (int)(offsetY+pos.y));
	}
}
