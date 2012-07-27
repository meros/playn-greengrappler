package com.meros.playn.core.media;

import java.util.HashMap;
import java.util.Map;

public class Sound {

	static Map<String, playn.core.Sound> mSounds = new HashMap<String, playn.core.Sound>();

	public static void playSample(String soundFile) {
		if (!mSounds.containsKey(soundFile)) {
			mSounds.put(soundFile, playn.core.PlayN.assets()
					.getSound(soundFile));
		}

		mSounds.get(soundFile).play();
	}

	public static void onExit() {
		mSounds.clear();
	}
}
