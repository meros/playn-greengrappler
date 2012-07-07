package com.meros.playn.core;

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
		return (int) (aMin + (aMax - aMin) * aRatio);
	}
	
	public static float2 sincos(double d) {
		return new float2((float) Math.cos(d), (float) Math.sin(d));
	}
}
