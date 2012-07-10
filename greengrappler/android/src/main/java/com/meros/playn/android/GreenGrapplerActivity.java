package com.meros.playn.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.meros.playn.core.GreenGrappler;
import com.meros.playn.core.Music;
import com.meros.playn.core.Music.AbstractSong;
import com.meros.playn.core.Music.SongFactory;

public class GreenGrapplerActivity extends GameActivity {

	@Override
	public void main(){
		
		Music.setSongFactory(new SongFactory(){

			@Override
			public AbstractSong getSong(String resource) {
				
				return null;
//				try {
//					return new Song(getClass().getClassLoader().getResourceAsStream("com/meros/playn/resources/" + resource));
//				} catch (LineUnavailableException e) {
//					return null;
//				} catch (IOException e) {
//					return null;
//				}
			}
		});
		
		platform().assets().setPathPrefix("com/meros/playn/resources");
		PlayN.run(new GreenGrappler(true, new GreenGrappler.ExitCallback() {

			@Override
			public void exit() {
				// TODO Auto-generated method stub

			}
		}));
	}
}
