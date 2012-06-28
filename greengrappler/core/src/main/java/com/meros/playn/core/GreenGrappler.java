package com.meros.playn.core;

import static playn.core.PlayN.*;

import playn.core.Game;
import playn.core.CanvasImage;
import playn.core.Canvas;
import playn.core.ImageLayer;

public class GreenGrappler implements Game {
	Canvas canvas;
	int frame = 0;

	Animation animation = new Animation("images/coin.bmp", 4);

	@Override
	public void init() {
		// create and add background image layer
		graphics().setSize(320, 240);
		CanvasImage canvasImage = graphics().createImage(320,240);
		canvas = canvasImage.canvas(); 
		ImageLayer bgLayer = graphics().createImageLayer(canvasImage);
		graphics().rootLayer().add(bgLayer);
	}

	@Override
	public void paint(float alpha) {
		// the background automatically paints itself, so no need to do anything here!
	}

	@Override
	public void update(float delta) {
		animation.drawFrame(canvas, frame++/10, 0, 0);
	}

	@Override
	public int updateRate() {
		return 25;
	}
}
