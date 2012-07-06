package com.meros.playn.core;

public class Music {

	static playn.core.Sound sound;

	public static void playSong(String path) {
		stop();
		// TODO: mp3 converted songs are just too large!
		// sound = playn.core.PlayN.assets().getSound(path);
		// sound.setLooping(true);
		// sound.play();
	}

	public static void stop() {
		if (sound != null) {
			sound.stop();
		}
	}

}
