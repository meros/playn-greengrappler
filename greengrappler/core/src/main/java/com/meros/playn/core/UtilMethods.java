package com.meros.playn.core;

import com.meros.playn.core.GlobalOptions.VibrationType;
import com.meros.playn.core.floatpair.ImmutableFloatPair;
import com.meros.playn.core.media.Sound;

public class UtilMethods {
	public static int lerp(int aMin, int aMax, float aRatio) {
		if (aRatio < 0.0f)
			return aMin;
		if (aRatio > 1.0f)
			return aMax;
		return (int) (aMin + (aMax - aMin) * aRatio);
	}

	public static float lerp(float aMin, float aMax, float aRatio) {
		if (aRatio < 0.0f)
			return aMin;
		if (aRatio > 1.0f)
			return aMax;
		return aMin + (aMax - aMin) * aRatio;
	}

	public static ImmutableFloatPair sincos(double d) {
		return new ImmutableFloatPair((float) Math.cos(d), (float) Math.sin(d));
	}

	public static void selectFeedback() {
		GlobalOptions.Vibrate(25, VibrationType.SIMPLE);
		Sound.playSample("data/sounds/select");
	}
}
