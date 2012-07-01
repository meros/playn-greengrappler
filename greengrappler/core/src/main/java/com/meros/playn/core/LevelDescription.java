package com.meros.playn.core;

public class LevelDescription {
	public String myName = "";
	public String myLevelFile = "";
	public int myFrameIndex = 0;
	public String myMusicFile = "";

	public LevelDescription()
	{}

	public LevelDescription(String aName, String aLevelFile, int aFrameIndex, String aMusicFile)

	{
		myName = aName;
		myLevelFile = aLevelFile;
		myFrameIndex = aFrameIndex;
		myMusicFile = aMusicFile;
	}
}
