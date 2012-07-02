package com.meros.playn.core;

import com.meros.playn.core.Constants.Buttons;

import playn.core.Canvas;
import playn.core.Color;

public class LevelScreen extends Screen {

	Room	  myRoom;
	Animation myDarken = Resource.getAnimation("data/images/darken.bmp");
	Animation myHand = Resource.getAnimation("data/images/hand.bmp", 1);
	Animation myExitBackground;
	boolean myExit = false;
	Font myFont = Resource.getFont("data/images/font.bmp");
	int mySelected = 0;
	LevelDescription myLevelDesc;

	public LevelScreen(LevelDescription aLevelDescription) {
		myLevelDesc = aLevelDescription;
		Music.playSong(myLevelDesc.myMusicFile);
		
		//TODO: tmeporary
		myRoom = new Room();
	}

	@Override
	public void onLogic() {

		if (myRoom.isCompleted())
		{
			if (myLevelDesc.myLevelFile == "data/rooms/olof.tmx")
				LevelSelectScreen.myBossLevelCompleted = true;

			GameState.put(myLevelDesc.myLevelFile, 1);
			exit();
		}

		if (!myExit)
			myRoom.onLogic();

		if (Input.isPressed(Buttons.Exit) && !myExit)
		{
			Sound.playSample("data/sounds/select");
			myExit = true;
		}
		else if (Input.isPressed(Buttons.Exit) && myExit)
		{
			Sound.playSample("data/sounds/select");
			myExit = false;
		}

		if (!myExit)
			return;

		if (Input.isPressed(Buttons.Down))
		{
			mySelected++;
			if (mySelected > 1)
				mySelected = 1;
			else
				Sound.playSample("data/sounds/select");
		}
		if (Input.isPressed(Buttons.Up))
		{
			mySelected--;
			if (mySelected < 0)
				mySelected = 0;
			else
				Sound.playSample("data/sounds/select");
		}

		if (Input.isPressed(Buttons.Fire))
		{
			Sound.playSample("data/sounds/select");
			if (mySelected == 0)
			{
				myExit = false;
			}
			else if (mySelected == 1)
			{
				exit();
			}
		}


	}

	@Override
	public void onDraw(Canvas aBuffer) {
		aBuffer.setFillColor(Color.rgb(57,56,41));
		aBuffer.fillRect(0,0,320,240);

		myRoom.onDraw(aBuffer);

		String coinsString = "[x" + GameState.getInt("coins");

		aBuffer.setFillColor(Color.rgb(57, 56, 41));
		aBuffer.fillRect(0,0,320,10);

		myFont.draw(aBuffer, coinsString, 1, 1);

		if (myExit)
		{
			for (int y = 0; y < 15; y++)
			{
				for (int x = 0; x < 20; x++)
				{
					myDarken.drawFrame(aBuffer, 0, x * 16, y * 16);
				}
			}
		}

		if (myExit)
		{
			myExitBackground.drawFrame(aBuffer, 0, 160 - myExitBackground.getFrameWidth() / 2, 120 - myExitBackground.getFrameHeight() / 2);
			myFont.draw(aBuffer, "CONTINUE", 130, 110);
			myFont.draw(aBuffer, "EXIT LEVEL", 130, 120);
			myHand.drawFrame(aBuffer, 0, 120, 110 + mySelected * 10);
		}
	}

}
