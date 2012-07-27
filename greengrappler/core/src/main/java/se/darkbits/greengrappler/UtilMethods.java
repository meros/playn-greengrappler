package se.darkbits.greengrappler;

import se.darkbits.greengrappler.GlobalOptions.VibrationType;
import se.darkbits.greengrappler.floatpair.ImmutableFloatPair;
import se.darkbits.greengrappler.media.Sound;


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
		selectVibration();
		Sound.playSample("data/sounds/select");
	}

	public static void selectVibration() {
		GlobalOptions.Vibrate(25, VibrationType.SIMPLE);
	}

}
