package se.darkbits.greengrappler.screens;

import playn.core.Color;
import playn.core.Surface;
import se.darkbits.greengrappler.Resource;
import se.darkbits.greengrappler.Screen;
import se.darkbits.greengrappler.media.Animation;
import se.darkbits.greengrappler.media.Sound;


public class SplashScreen extends Screen {

	int mFrameCounter = 0;
	Animation mLogo = Resource.getAnimation("data/images/logo.bmp", 1);

	@Override
	public void onEntered() {
	}

	@Override
	public void onDraw(Surface aBuffer) {
		int yOffset = -160 + mFrameCounter;

		if (yOffset == 0)
			Sound.playSample("data/sounds/boot");
		if (yOffset > 0)
			yOffset = 0;
		aBuffer.setFillColor(Color.rgb(231, 215, 156));
		aBuffer.fillRect(0, 0, 320, 240);
		mLogo.drawFrame(aBuffer, 0, 160 - mLogo.getFrameWidth() / 2, 120
				- mLogo.getFrameHeight() / 2 + yOffset);
	}

	@Override
	public void onLogic() {
		if (mFrameCounter > 250) {
			exit();
			return;
		}

		mFrameCounter++;
	}

}
