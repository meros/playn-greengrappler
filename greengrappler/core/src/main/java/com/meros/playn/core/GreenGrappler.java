package com.meros.playn.core;

import static playn.core.PlayN.*;

import playn.core.Game;
import playn.core.CanvasImage;
import playn.core.Canvas;
import playn.core.ImageLayer;

public class GreenGrappler implements Game {

	Canvas canvas = null;
	int frame = 0;

	Room room;
	Coin coin;
	Hero hero;

	boolean myReadyForUpdates = false;

	@Override
	public void init() {
		// create and add background image layer
		//graphics().setSize(320, 240);
		CanvasImage canvasImage = graphics().createImage(320, 240);
		canvas = canvasImage.canvas();
		ImageLayer bgLayer = graphics().createImageLayer(canvasImage);
		graphics().rootLayer().add(bgLayer);
		
		Resource.preLoad("data/images/boss.bmp");
		Resource.preLoad("data/images/breakinghooktile.bmp");
		Resource.preLoad("data/images/button.bmp");
		Resource.preLoad("data/images/checkpoint.bmp");
		Resource.preLoad("data/images/coin.bmp");
		Resource.preLoad("data/images/completed_level.bmp");
		Resource.preLoad("data/images/core.bmp");
		Resource.preLoad("data/images/darkbitslogo_blink.bmp");
		Resource.preLoad("data/images/darken.bmp");
		Resource.preLoad("data/images/debris.bmp");
		Resource.preLoad("data/images/dialogue.bmp");
		Resource.preLoad("data/images/doctor_green_portrait.bmp");
		Resource.preLoad("data/images/door.bmp");
		Resource.preLoad("data/images/entities.bmp");
		Resource.preLoad("data/images/fade.bmp");
		Resource.preLoad("data/images/font.bmp");
		Resource.preLoad("data/images/hand.bmp");
		Resource.preLoad("data/images/hero_fall.bmp");
		Resource.preLoad("data/images/hero_hurt.bmp");
		Resource.preLoad("data/images/hero_jump.bmp");
		Resource.preLoad("data/images/hero_run.bmp");
		Resource.preLoad("data/images/hook.bmp");
		Resource.preLoad("data/images/icons.bmp");
		Resource.preLoad("data/images/lavafill.bmp");
		Resource.preLoad("data/images/lavatop.bmp");
		Resource.preLoad("data/images/level_exit_background.bmp");
		Resource.preLoad("data/images/level_select.bmp");
		Resource.preLoad("data/images/logo.bmp");
		Resource.preLoad("data/images/movinghooktile.bmp");
		Resource.preLoad("data/images/particles.bmp");
		Resource.preLoad("data/images/reactor_shell.bmp");
		Resource.preLoad("data/images/robot.bmp");
		Resource.preLoad("data/images/rope.bmp");
		Resource.preLoad("data/images/saw.bmp");
		Resource.preLoad("data/images/selected_level_background.bmp");
		Resource.preLoad("data/images/software.bmp");
		Resource.preLoad("data/images/ted_portrait.bmp");
		Resource.preLoad("data/images/tileset1.bmp");
		Resource.preLoad("data/images/title.bmp");
		Resource.preLoad("data/images/unselected_level_background.bmp");
		Resource.preLoad("data/images/wall.bmp");
		Resource.preLoad("data/images/z_coin.bmp");
	}

	@Override
	public void paint(float alpha) {
		if (!myReadyForUpdates)
			return;

		canvas.clear();
		coin.draw(canvas, 0, 0, 0);
		hero.draw(canvas, 0, 0, 3);
		// the background automatically paints itself, so no need to do anything
		// here!
	}

	@Override
	public void update(float delta) {
		if (myReadyForUpdates) {
			postPreloadUpdate();
		} else if (Resource.isDonePreloading()) {
			Sound.playSample("data/sounds/alarm");
			postPreloadInit();
			myReadyForUpdates = true;
		}
	}

	void postPreloadInit() {
		room = new Room();
		coin = new Coin();
		hero = new Hero();

		room.addEntity(coin);
		room.addEntity(hero);

		coin.setPosition(new float2(10, 10));
		coin.setLifeTime(10000);

		hero.setPosition(new float2(10, 70));
	}

	void postPreloadUpdate() {
		coin.update();
		hero.update();
	}

	@Override
	public int updateRate() {
		return 1000 / Time.TicksPerSecond;
	}
}
