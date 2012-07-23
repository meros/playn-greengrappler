package com.meros.playn.android;

import java.io.IOException;

import playn.android.GameActivity;
import playn.core.PlayN;
import playn.core.PlayN.LifecycleListener;
import android.os.Vibrator;

import com.meros.playn.core.GlobalOptions;
import com.meros.playn.core.GlobalOptions.AbstractVibrator;
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
				try {
					return new Song(getClass().getClassLoader().getResourceAsStream("com/meros/playn/resources/" + resource));
				} catch (IOException e) {
					return null;
				}
			}
		});

		GlobalOptions.mVibrator = new AbstractVibrator() {
			Vibrator myVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			@Override
			public void vibrate(int aVibrateTime) {
				myVibrator.vibrate(aVibrateTime);
			}
		};

		platform().assets().setPathPrefix("com/meros/playn/resources");
		PlayN.setLifecycleListener(new LifecycleListener() {

			@Override
			public void onResume() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPause() {
				if (PlayN.graphics().ctx().quadShader(null) != null) {
					PlayN.graphics().ctx().quadShader(null).clearProgram();
				}
				if (PlayN.graphics().ctx().trisShader(null) != null) {
					PlayN.graphics().ctx().trisShader(null).clearProgram();
				}
			}

			@Override
			public void onExit() {
				// TODO Auto-generated method stub

			}
		});
		
		PlayN.run(new GreenGrappler(true, new GreenGrappler.ExitCallback() {

			@Override
			public void exit() {
				// TODO Auto-generated method stub

			}
		}));
	}
}
