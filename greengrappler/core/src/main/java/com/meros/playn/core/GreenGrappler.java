package com.meros.playn.core;

import static playn.core.PlayN.*;

import com.meros.playn.core.Constants.Buttons;

import playn.core.Game;
import playn.core.CanvasImage;
import playn.core.Canvas;
import playn.core.ImmediateLayer;
import playn.core.ImmediateLayer.Renderer;
import playn.core.Surface;

public class GreenGrappler implements Game, Renderer {


	public interface ExitCallback
	{
		public abstract void exit();
	}

	Canvas canvas = null;
	CanvasImage canvasImage = null;

	boolean myFullScreen = false;

	int frame = 0;

	boolean myReadyForUpdates = false;
	ExitCallback myExitCallback;

	public GreenGrappler(boolean aFullScreen, ExitCallback exitCallback)
	{
		myFullScreen = aFullScreen;
		myExitCallback = exitCallback;
	}

	@Override
	public void init() {
		log().debug("Green Grappler init");

		// create and add background image layer
		//graphics().setSize(320, 240);
		canvasImage = graphics().createImage(320, 240);
		canvas = canvasImage.canvas();
		ImmediateLayer imLayer = graphics().createImmediateLayer(this);
		graphics().rootLayer().add(imLayer);

		if (myFullScreen)
			graphics().setSize(graphics().screenWidth(),graphics().screenHeight());

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

		Resource.preLoadText("data/rooms/breaktilelevel.txt");
		Resource.preLoadText("data/rooms/level1.txt");
		Resource.preLoadText("data/rooms/levellava.txt");
		Resource.preLoadText("data/rooms/olof.txt");
		Resource.preLoadText("data/rooms/olof2.txt");
		Resource.preLoadText("data/rooms/per1.txt");
		Resource.preLoadText("data/rooms/per2.txt");
		Resource.preLoadText("data/rooms/per3.txt");
		Resource.preLoadText("data/rooms/per4.txt");
		Resource.preLoadText("data/rooms/smalltestroom.txt");
		Resource.preLoadText("data/rooms/test.txt");
		Resource.preLoadText("data/rooms/tutorial.txt");
		Resource.preLoadText("data/dialogues/1-tutorial1.txt");
		Resource.preLoadText("data/dialogues/2-tutorial2.txt");
		Resource.preLoadText("data/dialogues/boss_unlocked.txt");
		Resource.preLoadText("data/dialogues/level_select.txt");

		Resource.preLoadSound("data/sounds/alarm.mp3");
		Resource.preLoadSound("data/sounds/beep.mp3");
		Resource.preLoadSound("data/sounds/boot.mp3");
		Resource.preLoadSound("data/sounds/boss_saw.mp3");
		Resource.preLoadSound("data/sounds/coin.mp3");
		Resource.preLoadSound("data/sounds/damage.mp3");
		Resource.preLoadSound("data/sounds/green_peace.mp3");
		Resource.preLoadSound("data/sounds/hook.mp3");
		Resource.preLoadSound("data/sounds/hurt.mp3");
		Resource.preLoadSound("data/sounds/jump.mp3");
		Resource.preLoadSound("data/sounds/land.mp3");
		Resource.preLoadSound("data/sounds/no_hook.mp3");
		Resource.preLoadSound("data/sounds/reactor_explosion.mp3");
		Resource.preLoadSound("data/sounds/rope.mp3");
		Resource.preLoadSound("data/sounds/select.mp3");
		Resource.preLoadSound("data/sounds/start.mp3");
		Resource.preLoadSound("data/sounds/time.mp3");
		Resource.preLoadSound("data/sounds/timeout.mp3");

		Input.init();
	}

	@Override
	public void paint(float alpha) {
		if (!myReadyForUpdates)
			return;

		canvas.clear();
		ScreenManager.draw(canvas);
	}

	@Override
	public void update(float delta) {
		if (myReadyForUpdates) {
			postPreloadUpdate();
		} else if (Resource.isDonePreloading()) {
			postPreloadInit();
			myReadyForUpdates = true;
		}

		if (Input.isPressed(Buttons.ForceQuit) || ScreenManager.isEmpty())
		{
			myExitCallback.exit();
		}

		Input.update();
	}

	void postPreloadInit() {
		//ScreenManager.add(new EndScreen());
		ScreenManager.add(new TitleScreen());
		//TODO: ScreenManager.add(new SplashScreen());	
	}

	void postPreloadUpdate() {
		ScreenManager.onLogic();
	}

	@Override
	public int updateRate() {
		return 1000 / Time.TicksPerSecond;
	}

	@Override
	public void render(Surface surface) {
		surface.save();

		float w = surface.width();
		float h = surface.height();

		float targetAspect = 320/240;
		float aspect = w/h;

		if (aspect < targetAspect)
		{
			float scaleFactor = surface.width()/320;
			surface.scale(scaleFactor, scaleFactor);	
		}
		else
		{
			float scaleFactor = surface.height()/240;
			surface.scale(scaleFactor, scaleFactor);				
		}

		surface.drawImage(canvasImage, 0, 0);
		surface.restore();
	}
}
