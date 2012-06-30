package com.meros.playn.core;

import static playn.core.PlayN.*;



import playn.core.Game;
import playn.core.CanvasImage;
import playn.core.Canvas;
import playn.core.ImageLayer;
import tiled.simple.reader.TMXReader;

public class GreenGrappler implements Game {

	Canvas canvas;
	int frame = 0;

	Room room = new Room();
	Coin coin = new Coin();
	Hero hero = new Hero();

	@Override
	public void init() {
		
		room.addEntity(coin);
		room.addEntity(hero);
		
		TMXReader reader = new TMXReader();
		
		// create and add background image layer
		graphics().setSize(320, 240);
		CanvasImage canvasImage = graphics().createImage(320,240);
		canvas = canvasImage.canvas(); 
		ImageLayer bgLayer = graphics().createImageLayer(canvasImage);
		graphics().rootLayer().add(bgLayer);

		coin.setPosition(new float2(10,10));
		coin.setLifeTime(10000);
		
		hero.setPosition(new float2(10, 70));
	}

	@Override
	public void paint(float alpha) 
	{
		canvas.clear();
		coin.draw(canvas, 0, 0, 0);
		hero.draw(canvas, 0, 0, 3);
		// the background automatically paints itself, so no need to do anything here!
	}

	@Override
	public void update(float delta) {
		coin.update();
		hero.update();
	}

	@Override
	public int updateRate() {
		return 1000/Time.TicksPerSecond;
	}
}
