package com.meros.playn.core;

import static playn.core.PlayN.*;



import playn.core.Game;
import playn.core.CanvasImage;
import playn.core.Canvas;
import playn.core.ImageLayer;

public class GreenGrappler implements Game {

	Canvas canvas;
	int frame = 0;

	Coin entity = new Coin();

	@Override
	public void init() {
		// create and add background image layer
		graphics().setSize(320, 240);
		CanvasImage canvasImage = graphics().createImage(320,240);
		canvas = canvasImage.canvas(); 
		ImageLayer bgLayer = graphics().createImageLayer(canvasImage);
		graphics().rootLayer().add(bgLayer);

		entity.setPosition(new float2(10,10));
		entity.setLifeTime(10000);
	}

	@Override
	public void paint(float alpha) 
	{
		canvas.clear();
		entity.draw(canvas, 0, 0, 0);
		// the background automatically paints itself, so no need to do anything here!
	}

	@Override
	public void update(float delta) {
		entity.update();
	}

	@Override
	public int updateRate() {
		return 1000/Time.TicksPerSecond;
	}
}
