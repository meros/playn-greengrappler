package com.meros.playn.core;

public class LevelDescription {
	public int myFrameIndex = 0;
	public String myLevelFile = "";
	public String myMusicFile = "";
	public String myName = "";

	public LevelDescription() {
	}

	public LevelDescription(String aName, String aLevelFile, int aFrameIndex,
			String aMusicFile)

	{
		myName = aName;
		myLevelFile = aLevelFile;
		myFrameIndex = aFrameIndex;
		myMusicFile = aMusicFile;
	}
}
