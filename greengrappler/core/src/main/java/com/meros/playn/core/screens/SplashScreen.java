package com.meros.playn.core.screens;

import com.meros.playn.core.Animation;
import com.meros.playn.core.GreenGrappler;
import com.meros.playn.core.Resource;
import com.meros.playn.core.Screen;
import com.meros.playn.core.Sound;

import playn.core.Surface;
import playn.core.Color;

public class SplashScreen extends Screen {

	int mFrameCounter = 0;
	Animation mLogo = Resource.getAnimation("data/images/logo.bmp", 1);
	
	@Override
	public void onEntered()
	{
		GreenGrappler.showTouchControls(false);
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
