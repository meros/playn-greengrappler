package com.meros.playn.core;

import java.util.ArrayList;

import playn.core.Surface;

public class Dialogue extends Entity {

	enum CharacterPortrait {
		DoctorGreen, Ted
	}

	class Line {
		public String myLine;
		CharacterPortrait myPortrait;
	}

	Animation myBackground = Resource.getAnimation("data/images/dialogue.bmp",
			1);
	int myCurrentCharacter;

	int myCurrentLine;;

	Animation myDoctorGreenPortrait = Resource.getAnimation(
			"data/images/doctor_green_portrait.bmp", 1);
	boolean myDone = false;
	String myFile;
	Font myFont = Resource.getFont("data/images/font.bmp");
	int myFrameCounter;
	ArrayList<Line> myLines = new ArrayList<Line>();
	boolean myRunning = false;
	boolean myRunWithoutHero = false;

	Animation myTedPortrait = Resource.getAnimation(
			"data/images/ted_portrait.bmp", 1);

	public Dialogue(String aFilename) {
		String dialogueFileString = Resource.getText(aFilename);

		String[] lines = dialogueFileString.split("\n");

		for (String line : lines) {
			char character = line.charAt(0);
			CharacterPortrait portrait = null;
			if (character == 'D')
				portrait = CharacterPortrait.DoctorGreen;
			else if (character == 'T')
				portrait = CharacterPortrait.Ted;

			addLine(portrait, line.substring(1));
		}

		setSize(new float2(10, 10 * 200));
	}

	private void addLine(CharacterPortrait aPortrait, String aLine) {
		Line line = new Line();
		line.myLine = aLine;
		line.myPortrait = aPortrait;
		myLines.add(line);
	}

	@Override
	public void draw(Surface aBuffer, int offsetX, int offsetY, int layer) {
		// Entity::draw(buffer, offsetX, offsetY, layer);

		if (myDone)
			return;

		if (!myRunning)
			return;
		
		int position = 240 - myBackground.getFrameHeight();
		
		if (GlobalOptions.dialogueAtTop())
		{
			position = 10;
		}

		myBackground.drawFrame(aBuffer, 0, 0, position, false, GlobalOptions.dialogueAtTop());
		CharacterPortrait portrait = myLines.get(myCurrentLine).myPortrait;
		String line = myLines.get(myCurrentLine).myLine;

		int pX = 4;
		int pY = position + myBackground.getFrameHeight() - myTedPortrait.getFrameHeight() - 3;
		
		if (portrait == CharacterPortrait.Ted)
			myTedPortrait.drawFrame(aBuffer, 0, pX, pY);
		if (portrait == CharacterPortrait.DoctorGreen)
			myDoctorGreenPortrait.drawFrame(aBuffer, 0, pX, pY);
		
		int fontPosition = position + myBackground.getFrameHeight() - 16;
		if (GlobalOptions.dialogueAtTop())
		{
			fontPosition = position + 5;
		}

		myFont.drawWrap(aBuffer, line, 55, fontPosition, 300, myCurrentCharacter);
	}

	@Override
	public int getLayer() {
		return 3;
	}

	public void setRunWithoutHero() {
		myRunWithoutHero = true;
		myRunning = true;
	}

	@Override
	public void update() {
		if (myDone)
			return;

		if (!myRunWithoutHero) {
			if (mRoom.getHero().getCollisionRect().Collides(getCollisionRect())) {
				myRunning = true;
			}
		}

		if (!myRunning)
			return;

		myFrameCounter++;

		Line line = myLines.get(myCurrentLine);

		if (myFrameCounter > 2 && myCurrentCharacter < line.myLine.length()) {
			myCurrentCharacter++;
			myFrameCounter = 0;
			Sound.playSample("data/sounds/beep");
		}

		if (myFrameCounter > 90 && myLines.size() > myCurrentLine) {
			myCurrentLine++;
			myCurrentCharacter = 0;
			myFrameCounter = 0;

			if (myLines.size() == myCurrentLine) {
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

}
