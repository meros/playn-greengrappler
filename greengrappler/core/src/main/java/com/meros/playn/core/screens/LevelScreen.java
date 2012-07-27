package com.meros.playn.core.screens;

import java.util.HashMap;
import java.util.Map;

import playn.core.Color;
import playn.core.Surface;

import com.meros.playn.core.Constants.Buttons;
import com.meros.playn.core.GameState;
import com.meros.playn.core.GreenGrappler;
import com.meros.playn.core.Input;
import com.meros.playn.core.LevelDescription;
import com.meros.playn.core.Resource;
import com.meros.playn.core.Room;
import com.meros.playn.core.RoomLoader;
import com.meros.playn.core.Screen;
import com.meros.playn.core.UtilMethods;
import com.meros.playn.core.media.Animation;
import com.meros.playn.core.media.Font;
import com.meros.playn.core.media.Music;

public class LevelScreen extends Screen {

	Animation myDarken = Resource.getAnimation("data/images/darken.bmp");
	boolean myExit = false;
	Animation myExitBackground = Resource.getAnimation(
			"data/images/level_exit_background.bmp", 1);;
	Font myFont = Resource.getFont("data/images/font.bmp");
	Animation myHand = Resource.getAnimation("data/images/hand.bmp", 1);
	LevelDescription myLevelDesc;
	Room myRoom;
	int mySelected = 0;

	public LevelScreen(LevelDescription aLevelDescription) {
		myLevelDesc = aLevelDescription;
		Music.playSong(myLevelDesc.myMusicFile);

		myRoom = RoomLoader.LoadRoom(aLevelDescription.myLevelFile);
	}

	@Override
	public void onEntered() {
		GreenGrappler.showTouchControls(true);
	}

	Map<Integer, String> myCoinString = new HashMap<Integer, String>();

	@Override
	public void onDraw(Surface aBuffer) {
		aBuffer.setFillColor(Color.rgb(57, 56, 41));
		aBuffer.fillRect(0, 0, 320, 240);

		myRoom.onDraw(aBuffer);

		int coins = GameState.getInt("coins");
		if (!myCoinString.containsKey(coins)) {
			myCoinString.put(coins, "[x" + GameState.getInt("coins"));
		}

		String coinsString = myCoinString.get(coins);

		aBuffer.setFillColor(Color.rgb(57, 56, 41));
		aBuffer.fillRect(0, 0, 320, 10);

		myFont.draw(aBuffer, coinsString, 1, 1);

		if (myExit) {
			for (int y = 0; y < 15; y++) {
				for (int x = 0; x < 20; x++) {
					myDarken.drawFrame(aBuffer, 0, x * 16, y * 16);
				}
			}
		}

		if (myExit) {
			myExitBackground.drawFrame(aBuffer, 0,
					160 - myExitBackground.getFrameWidth() / 2,
					120 - myExitBackground.getFrameHeight() / 2);
			myFont.draw(aBuffer, "CONTINUE", 130, 110);
			myFont.draw(aBuffer, "EXIT LEVEL", 130, 120);
			myHand.drawFrame(aBuffer, 0, 120, 110 + mySelected * 10);
		}
	}

	@Override
	public void onLogic() {

		if (myRoom.isCompleted()) {
			if (myLevelDesc.myLevelFile == "data/rooms/olof.tmx")
				LevelSelectScreen.myBossLevelCompleted = true;

			GameState.put(myLevelDesc.myLevelFile, 1);
			exit();
		}

		if (!myExit)
			myRoom.onLogic();

		if (Input.isPressed(Buttons.EXIT) && !myExit) {
			UtilMethods.selectFeedback();
			myExit = true;
		} else if (Input.isPressed(Buttons.EXIT) && myExit) {
			UtilMethods.selectFeedback();
			myExit = false;
		}

		if (!myExit)
			return;

		if (Input.isPressed(Buttons.DOWN)) {
			mySelected++;
			if (mySelected > 1)
				mySelected = 1;
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

		if (Input.isPressed(Buttons.FIRE)) {
			UtilMethods.selectFeedback();
			if (mySelected == 0) {
				myExit = false;
			} else if (mySelected == 1) {
				exit();
			}
		}

	}

}
