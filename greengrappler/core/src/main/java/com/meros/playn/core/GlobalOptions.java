package com.meros.playn.core;

public class GlobalOptions {
	
	public static interface AbstractVibrator
	{
		public void vibrate(int aVibrateTime);
	}

	public static AbstractVibrator mVibrator = null;
	private static boolean mPaused = false;
	
	public static boolean showTouchControls()
	{
		return true;
	}
	
	public static boolean dialogueAtTop()
	{
		return true;
	}
	
	public static boolean avoidHeroAtThumbs()
	{
		return true;
	}
	
	public static void Vibrate(int aVibrateTime)
	{
		if (mVibrator  != null)
			mVibrator.vibrate(aVibrateTime);
	}

	public static void setPaused(boolean aPaused) {
		mPaused = aPaused;
	}
	
	public static boolean getPaused()
	{
		return mPaused;
	}
}
