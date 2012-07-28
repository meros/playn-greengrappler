package se.darkbits.greengrappler.java;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import playn.core.PlayN;
import playn.java.JavaPlatform;
import se.darkbits.greengrappler.GreenGrappler;
import se.darkbits.greengrappler.media.Music;
import se.darkbits.greengrappler.media.Music.AbstractSong;
import se.darkbits.greengrappler.media.Music.SongFactory;


public class GreenGrapplerJava {

	public static void main(String[] args) {
		JavaPlatform platform = JavaPlatform.register();
		platform.assets().setPathPrefix("se/darkbits/greengrappler/resources");
		
		Music.setSongFactory(new SongFactory(){

			@Override
			public AbstractSong getSong(String resource) {
				try {
					return new Song(getClass().getClassLoader().getResourceAsStream("se/darkbits/greengrappler/resources/" + resource));
				} catch (LineUnavailableException e) {
					return null;
				} catch (IOException e) {
					return null;
				}
			}
		});
		
		PlayN.run(new se.darkbits.greengrappler.GreenGrappler(false,
				new GreenGrappler.ExitCallback() {

					@Override
					public void exit() {
						System.exit(0);

					}
				}));
	}
}
