package com.meros.playn.core;

import java.util.Stack;

public class Music {

	public interface AbstractSong
	{
		public abstract void play();
		public abstract void stop();
		public abstract void update();
	}

	public interface SongFactory
	{
		public abstract AbstractSong getSong(String resource);
	}

	private static AbstractSong myCurrentSong = null;
	private static Stack<AbstractSong> mySongStack = new Stack<AbstractSong>();

	private static SongFactory mySongFactory = null;

	public static void setSongFactory(SongFactory aSongFactory)
	{
		mySongFactory = aSongFactory;
	}

	public static void playSong(String path) {
		stop();

		if (mySongFactory != null)
		{
			myCurrentSong = mySongFactory.getSong(path);
		}
		
		play();
	}

	public static void play()
	{
		if (myCurrentSong != null)
		{
			myCurrentSong.play();
		}
	}

	public static void stop() {
		if (myCurrentSong != null)
		{
			myCurrentSong.stop();
		}
	}

	public static void pushSong() {
		stop();

		if (myCurrentSong != null)
		{
			mySongStack.push(myCurrentSong);
		}
	}

	public static void popSong() {
		stop();

		if (myCurrentSong != null && mySongStack.size() > 0)
		{
			myCurrentSong = mySongStack.pop();
		}

		play();
	}

	public static void update()
	{
		if (myCurrentSong != null)
		{
			myCurrentSong.update();
		}
	}
}
