package com.meros.playn.core;

public class Sound {

	public static void playSample(String soundFile) {
		playn.core.PlayN.assets().getSound(soundFile).play();
	}

}
