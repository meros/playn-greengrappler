package com.meros.playn.core;

import static playn.core.PlayN.*;



import playn.core.Game;
import playn.core.CanvasImage;
import playn.core.Canvas;
import playn.core.ImageLayer;

class TestEntity extends Entity
{
	public TestEntity()
	{
		mSize.x = 10;
		mSize.y = 10;
		mPosition.x = 50;
		mPosition.y = 40;
		mVelocity.x = 0.5f;
		mVelocity.y = 5.0f;
	}
	
	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void update()
	{
		super.update();
		
		mPosition = mPosition.add(mVelocity.divide(Time.TicksPerSecond));
	}
	
}

public class GreenGrappler implements Game {
	Canvas canvas;
	int frame = 0;

	Animation animation = new Animation("images/coin.bmp", 4);
	
	Entity entity = new TestEntity();

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
	public void paint(float alpha) 
	{
		canvas.clear();
		entity.draw(canvas, 0, 0, 0);
		//animation.drawFrame(canvas, frame++/10, 0, 0);
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
