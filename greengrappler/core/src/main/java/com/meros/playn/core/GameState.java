package com.meros.playn.core;

import playn.core.PlayN;

public class GameState {

	public static boolean isSavePresent() {
		return getInt("save_present") == 1;
	}
	
	public static void setSavePresent()
	{
		put("save_present", 1);
	}

	public static void clear() {
		//TODO: playn.core.PlayN.storage().
	}

	public static void saveToFile() {
		setSavePresent();
	}

	public static int getInt(String aKey) {
		String item = PlayN.storage().getItem(aKey);
		if (item == null)
			return 0;
		
		return Integer.parseInt(item);
	}

	public static void put(String aKey, int aInt) {
		PlayN.storage().setItem(aKey, "" + aInt);
	}

}
