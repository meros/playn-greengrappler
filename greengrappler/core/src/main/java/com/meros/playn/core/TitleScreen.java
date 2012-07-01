package com.meros.playn.core;

import com.meros.playn.core.Constants.Buttons;

import playn.core.Canvas;

public class TitleScreen extends Screen {

	//	Title();
	//	void onDraw(BITMAP* aBuffer);
	//	void onLogic();
	//	void onEntered();
	//private:
	//	Animation* myTitle;
	//	Animation* myHand;
	//	Font* myFont;
	//	int mySelected;
	//	bool myGameStart;
	//	int myFrameCounter;
	//	bool myContinue;

	boolean myContinue = false;
	boolean myGameStart = false;
	int mySelected = 0;
	int myFrameCounter = 0;

	Animation myTitle = Resource.getAnimation("data/images/title.bmp", 1);
	Animation myHand = Resource.getAnimation("data/images/hand.bmp", 1);
	//Font myFont = Resource.getFont("data/images/font.bmp");	

	@Override
	public void onDraw(Canvas aBuffer) {
		myTitle.drawFrame(aBuffer, 0, 0, 0);

		if (!myGameStart)
			myHand.drawFrame(aBuffer, 0, 115, 150 + mySelected * 10);

		//if (!myGameStart || (mySelected != 0 || myFrameCounter % 10 < 5))
		//TODO:	myFont->draw(aBuffer, "NEW GAME", 126, 150);

		if (myContinue)
		{
			//			if (!myGameStart || (mySelected != 1 || myFrameCounter % 10 < 5))
			//				myFont->draw(aBuffer, "CONTINUE", 126, 160);
			//			myFont->draw(aBuffer, "EXIT GAME", 126, 170);
		}
		else
		{
			//			myFont->draw(aBuffer, "EXIT GAME", 126, 160);
		}

		//		myFont->draw(aBuffer, "DARKBITS", 130, 215);
		//		myFont->draw(aBuffer, "SPEEDHACK 2011", 113, 225);
	}

	@Override
	public void onLogic() {
		myFrameCounter++;

		if (myGameStart)
		{
			if (myFrameCounter > 100)
			{
				//TODO: ScreenManager.add(new LevelSelect());
				if (mySelected == 0)
				{
					//TODO: ScreenManager.add(new Level(LevelDesc("tutorial", "data/rooms/tutorial.tmx", 0, "data/music/olof9.xm")));
				}
			}
			return;
		}

		if (Input.isPressed(Buttons.Down))
		{
			mySelected++;
			if (mySelected > 1 && !myContinue)
				mySelected = 1;
			else if (mySelected > 2)
				mySelected = 2;
			else
				Sound.playSample("data/sounds/select.wav");
		}

		if (Input.isPressed(Buttons.Up))
		{
			mySelected--;
			if (mySelected < 0)
				mySelected = 0;
			else
				Sound.playSample("data/sounds/select.wav");
		}

		if (Input.isPressed(Buttons.Fire))
		{
			if (mySelected == 0)
			{
				myGameStart = true;
				myFrameCounter = 0;
				//TODO: Music::stop();
				Sound.playSample("data/sounds/start.wav");
				//TODO: GameState.clear();
			}
			if (mySelected == 1 && myContinue)
			{
				myGameStart = true;
				myFrameCounter = 0;
				//TODO: Music::stop();
				Sound.playSample("data/sounds/start.wav");
				//TODO: GameState.loadFromFile();
			}

			if (mySelected == 1 && !myContinue)
				exit();
			if (mySelected == 2 && myContinue)
				exit();
		}
	}
	
	@Override
	public void onEntered()
	{
		myGameStart = false;
		//TODO: Music.playSong("data/music/intro2.xm");

		if (false /*TODO: GameState.isSavePresent()*/)
			myContinue = true;
		else
			myContinue = false;
		if (myContinue)
			mySelected = 1;
	}

}
