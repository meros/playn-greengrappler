package com.meros.playn.core;

import playn.core.Canvas;
import playn.core.Color;

public class SplashScreen extends Screen {

	Animation mLogo = Resource.getAnimation("data/images/logo.bmp", 1);
    int mFrameCounter = 0;

	
	@Override
	public void onDraw(Canvas aBuffer) {
		int yOffset = - 160 + mFrameCounter;

		if (yOffset == 0)
			Sound.playSample("data/sounds/boot");
		if (yOffset > 0)
			yOffset = 0;
		aBuffer.setFillColor(Color.rgb(231,215,156));
		aBuffer.fillRect(0, 0, 320,240);
	    mLogo.drawFrame(aBuffer, 0, 160 - mLogo.getFrameWidth() / 2, 120 - mLogo.getFrameHeight() / 2 + yOffset);
	}

	@Override
	public void onLogic() {
		if (mFrameCounter > 250)
		{
			exit();
			return;
		}

	    mFrameCounter++;
	}

}
