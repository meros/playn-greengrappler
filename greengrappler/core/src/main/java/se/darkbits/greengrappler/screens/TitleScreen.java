package se.darkbits.greengrappler.screens;

import playn.core.Surface;
import se.darkbits.greengrappler.GameState;
import se.darkbits.greengrappler.GreenGrappler;
import se.darkbits.greengrappler.Input;
import se.darkbits.greengrappler.LevelDescription;
import se.darkbits.greengrappler.Resource;
import se.darkbits.greengrappler.Screen;
import se.darkbits.greengrappler.ScreenManager;
import se.darkbits.greengrappler.UtilMethods;
import se.darkbits.greengrappler.Constants.Buttons;
import se.darkbits.greengrappler.media.Animation;
import se.darkbits.greengrappler.media.Font;
import se.darkbits.greengrappler.media.Music;
import se.darkbits.greengrappler.media.Sound;


public class TitleScreen extends Screen {

	// Title();
	// void onDraw(BITMAP* aBuffer);
	// void onLogic();
	// void onEntered();
	// private:
	// Animation* myTitle;
	// Animation* myHand;
	// Font* myFont;
	// int mySelected;
	// bool myGameStart;
	// int myFrameCounter;
	// bool myContinue;

	boolean myContinue = false;
	Font myFont = Resource.getFont("data/images/font.bmp");
	int myFrameCounter = 0;
	boolean myGameStart = false;

	Animation myHand = Resource.getAnimation("data/images/hand.bmp", 1);
	int mySelected = 0;
	Animation myTitle = Resource.getAnimation("data/images/title.bmp", 1);

	@Override
	public void onDraw(Surface aBuffer) {
		myTitle.drawFrame(aBuffer, 0, 0, 0);

		if (!myGameStart)
			myHand.drawFrame(aBuffer, 0, 115, 150 + mySelected * 10);

		if (!myGameStart || (mySelected != 0 || myFrameCounter % 10 < 5))
			myFont.draw(aBuffer, "NEW GAME", 126, 150);

		if (myContinue) {
			if (!myGameStart || (mySelected != 1 || myFrameCounter % 10 < 5))
				myFont.draw(aBuffer, "CONTINUE", 126, 160);
			myFont.draw(aBuffer, "EXIT GAME", 126, 170);
		} else {
			myFont.draw(aBuffer, "EXIT GAME", 126, 160);
		}

		myFont.draw(aBuffer, "DARKBITS", 130, 215);
		myFont.draw(aBuffer, "SPEEDHACK 2011", 113, 225);
	}

	@Override
	public void onEntered() {
		GreenGrappler.showTouchControls(true);

		myGameStart = false;
		Music.playSong("data/music/intro2.xm");

		if (GameState.isSavePresent())
			myContinue = true;
		else
			myContinue = false;
		if (myContinue)
			mySelected = 1;
	}

	@Override
	public void onLogic() {
		myFrameCounter++;

		if (myGameStart) {
			if (myFrameCounter > 100) {
				ScreenManager.add(new LevelSelectScreen());
				if (mySelected == 0) {
					ScreenManager.add(new LevelScreen(new LevelDescription(
							"tutorial", "data/rooms/tutorial.txt", 0,
							"data/music/olof9.xm")));
				}
			}
			return;
		}

		if (Input.isPressed(Buttons.DOWN)) {
			mySelected++;
			if (mySelected > 1 && !myContinue)
				mySelected = 1;
			else if (mySelected > 2)
				mySelected = 2;
			else
				UtilMethods.selectFeedback();
		}

		if (Input.isPressed(Buttons.UP)) {
			mySelected--;
			if (mySelected < 0)
				mySelected = 0;
			else
				UtilMethods.selectFeedback();
		}

		if (Input.isPressed(Buttons.EXIT)) {
			exit();
		}

		if (Input.isPressed(Buttons.FIRE)) {
			if (mySelected == 0) {
				myGameStart = true;
				myFrameCounter = 0;
				Music.stop();
				Sound.playSample("data/sounds/start");
				GameState.clear();
			}
			if (mySelected == 1 && myContinue) {
				myGameStart = true;
				myFrameCounter = 0;
				Music.stop();
				Sound.playSample("data/sounds/start");
				GameState.loadFromFile();
			}

			if (mySelected == 1 && !myContinue)
				exit();
			if (mySelected == 2 && myContinue)
				exit();
		}
	}

}
