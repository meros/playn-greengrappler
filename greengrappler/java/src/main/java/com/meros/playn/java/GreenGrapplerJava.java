package com.meros.playn.java;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.meros.playn.core.GreenGrappler;
import com.meros.playn.core.media.Music;
import com.meros.playn.core.media.Music.AbstractSong;
import com.meros.playn.core.media.Music.SongFactory;

public class GreenGrapplerJava {

	public static void main(String[] args) {
		JavaPlatform platform = JavaPlatform.register();
		platform.assets().setPathPrefix("com/meros/playn/resources");
		
		Music.setSongFactory(new SongFactory(){

			@Override
			public AbstractSong getSong(String resource) {
				try {
					return new Song(getClass().getClassLoader().getResourceAsStream("com/meros/playn/resources/" + resource));
				} catch (LineUnavailableException e) {
					return null;
				} catch (IOException e) {
					return null;
				}
			}
		});
		
		PlayN.run(new com.meros.playn.core.GreenGrappler(false,
				new GreenGrappler.ExitCallback() {

					@Override
					public void exit() {
						System.exit(0);

					}
				}));
	}
}
