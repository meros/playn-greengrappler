package com.meros.playn.core;

import java.io.IOException;
import java.util.Stack;

import javax.sound.sampled.LineUnavailableException;

import com.meros.playn.core.micromod.Song;

public class Music {

	private static Song myCurrentSong = null;
	
	private static Stack<Song> mySongStack = new Stack<Song>();
	
	public interface MusicPlayer
	{
		
	}
	
	MusicPlayer myMusicPlayer = null;

	public static void playSong(String path) {
		stop();
		
		try {
			//TODO: ugly hack!
			
			myCurrentSong = new Song("com/meros/playn/resources/" + path);//"/Users/meros/Documents/development/playn-greengrappler/greengrappler/core/src/main/java/com/meros/playn/resources/data/music/intro2.xm");
			myCurrentSong.play();
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void stop() {
		if (myCurrentSong != null)
		{
			myCurrentSong.stop();
		}
	}

	public static void pushSong() {
		if (myCurrentSong != null)
		{
			myCurrentSong.stop();
			mySongStack.push(myCurrentSong);
		}
	}

	public static void popSong() {
		stop();
		
		if (myCurrentSong != null && mySongStack.size() > 0)
		{
			myCurrentSong = mySongStack.pop();
			myCurrentSong.play();
		}
	}
	
	public static void update()
	{
		if (myCurrentSong != null)
		{
			myCurrentSong.update();
		}
	}
}
