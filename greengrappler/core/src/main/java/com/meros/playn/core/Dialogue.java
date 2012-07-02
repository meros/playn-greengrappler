package com.meros.playn.core;

import java.util.ArrayList;

import playn.core.Canvas;

public class Dialogue extends Entity {

	Font myFont = Resource.getFont("data/images/font.bmp");
	Animation myBackground = Resource.getAnimation("data/images/dialogue.bmp", 1);
	Animation myDoctorGreenPortrait = Resource.getAnimation("data/images/doctor_green_portrait.bmp", 1);
	Animation myTedPortrait = Resource.getAnimation("data/images/ted_portrait.bmp", 1);
	class Line
	{
		CharacterPortrait myPortrait;
		public String myLine;
	};
	ArrayList<Line> myLines = new ArrayList<Line>();
	int myCurrentLine;
	int myCurrentCharacter;
	int myFrameCounter;
	String myFile;
	boolean myDone = false;
	boolean myRunning = false;
	boolean myRunWithoutHero = false;

	enum CharacterPortrait
	{
		DoctorGreen,
		Ted
	}

	public Dialogue(String aFilename) {
		String dialogueFileString = Resource.getText(aFilename);

		ArrayList<String> lines = new ArrayList<String>(); 

		while (dialogueFileString.contains("\n"))
		{
			String before = dialogueFileString.substring(0, dialogueFileString.indexOf("\n"));
			String after = dialogueFileString.substring(dialogueFileString.indexOf("\n") + 1);
			lines.add(before);
			dialogueFileString = after;
		}

		for (int i = 0; i < lines.size(); i++)
		{
			char character = lines.get(i).charAt(0);
			CharacterPortrait portrait = null;
			if (character == 'D')
				portrait = CharacterPortrait.DoctorGreen;
			else if (character == 'T')
				portrait = CharacterPortrait.Ted;

			String line = lines.get(i).substring(2, lines.get(i).length());
			addLine(portrait, line);
		}

		setSize(new float2(10, 10 * 200));
	}

	private void addLine(CharacterPortrait aPortrait, String aLine) {
		Line line = new Line();
		line.myLine = aLine;
		line.myPortrait = aPortrait;
		myLines.add(line);
	}

	public void setRunWithoutHero() {
		myRunWithoutHero = true;
		myRunning = true;
	}

	public void draw(Canvas aBuffer, int offsetX, int offsetY, int layer) {
		//Entity::draw(buffer, offsetX, offsetY, layer);

		if (myDone)
			return;

		if (!myRunning)
			return;

		myBackground.drawFrame(aBuffer, 0, 0, 240 - myBackground.getFrameHeight());
		CharacterPortrait portrait = myLines.get(myCurrentLine).myPortrait;
		String line = myLines.get(myCurrentLine).myLine;

		int pX = 4;
		int pY = 240 - myTedPortrait.getFrameHeight() - 3;
		if (portrait == CharacterPortrait.Ted)
			myTedPortrait.drawFrame(aBuffer, 0, pX, pY);
		if (portrait == CharacterPortrait.DoctorGreen)
			myDoctorGreenPortrait.drawFrame(aBuffer, 0, pX, pY);

		myFont.drawWrap(aBuffer, line, 55, 240 - 16, 300, myCurrentCharacter);
	}

	@Override
	public void update()
	{
		if (myDone)
			return;

		if (!myRunWithoutHero)
		{
			if (Collides(mRoom.getHero().getCollisionRect(), getCollisionRect()))
			{
				myRunning = true;
			}
		}

		if (!myRunning)
			return;

		myFrameCounter++;

		Line line = myLines.get(myCurrentLine);

		if (myFrameCounter > 2
				&& myCurrentCharacter < (int)line.myLine.length())
		{
			myCurrentCharacter++;
			myFrameCounter = 0;
			Sound.playSample("data/sounds/beep");
		}

		if (myFrameCounter > 90
				&& (int)myLines.size() > myCurrentLine)
		{
			myCurrentLine++;
			myCurrentCharacter = 0;
			myFrameCounter = 0;

			if (myLines.size() == myCurrentLine)
			{
				myLines.clear(); // We are done!
				String key = "dialogue_";
				key += myFile;
				GameState.put(key, 1);
				myDone = true;
				if (!myRunWithoutHero)
					remove();
			}
		}
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 3;
	}

}
