package se.darkbits.greengrappler.android;

import java.io.IOException;

import playn.android.GameActivity;
import playn.core.PlayN;
import playn.core.PlayN.LifecycleListener;
import se.darkbits.greengrappler.GlobalOptions;
import se.darkbits.greengrappler.GreenGrappler;
import se.darkbits.greengrappler.GlobalOptions.AbstractVibrator;
import se.darkbits.greengrappler.GlobalOptions.VibrationType;
import se.darkbits.greengrappler.media.Music;
import se.darkbits.greengrappler.media.Music.AbstractSong;
import se.darkbits.greengrappler.media.Music.SongFactory;
import android.content.res.Configuration;
import android.os.Vibrator;
import android.view.KeyEvent;


public class GreenGrapplerActivity extends GameActivity {

	private GreenGrappler myGreenGrappler = null;

	@Override
	public void main(){

		Music.setSongFactory(new SongFactory(){

			@Override
			public AbstractSong getSong(String resource) {
				try {
					return new Song(getClass().getClassLoader().getResourceAsStream("se/darkbits/greengrappler/resources/" + resource));
				} catch (IOException e) {
					return null;
				}
			}
		});
		
		
		GlobalOptions.mVibrator = new AbstractVibrator() {
			Vibrator myVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

			@Override
			public void vibrate(int aVibrateTime, VibrationType aVibrationType) {
				if (aVibrationType == VibrationType.SIMPLE)
				{
					myVibrator.vibrate(aVibrateTime);
				}
				if (aVibrationType == VibrationType.PULSATING)
				{
					long[] pulsePattern = new long[2*aVibrateTime/100];

					for (int i = 0; i < 2*aVibrateTime/100; i++)
					{
						pulsePattern[i] = (((i%2)==0) ? 75 : 25);
					}
					
					myVibrator.vibrate(pulsePattern, -1);
				}
			}
		};
		
		GlobalOptions.myExitCallback = new GlobalOptions.ExitCallback() {

			@Override
			public void exit() {
				//I know this goes agains the android way - but I don't care!
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		};

		platform().assets().setPathPrefix("se/darkbits/greengrappler/resources");
		PlayN.setLifecycleListener(new LifecycleListener() {

			@Override
			public void onResume() {
				GlobalOptions.setPaused(false);
				Music.play();
			}

			@Override
			public void onPause() {
				if (PlayN.graphics().ctx().quadShader(null) != null) {
					PlayN.graphics().ctx().quadShader(null).clearProgram();
				}
				if (PlayN.graphics().ctx().trisShader(null) != null) {
					PlayN.graphics().ctx().trisShader(null).clearProgram();
				}

				GlobalOptions.setPaused(true);
				Music.stop();
			}

			@Override
			public void onExit() {
			}
		});
		
		synchronized(this)
		{
			myGreenGrappler = new GreenGrappler(true);
		}
		
		PlayN.run(myGreenGrappler);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent nativeEvent) {
		
		return super.onKeyDown(keyCode, nativeEvent);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		synchronized(this){
			if (myGreenGrappler != null)
			{
				myGreenGrappler.screenSizeChanged();
			}
		}
	}
}
