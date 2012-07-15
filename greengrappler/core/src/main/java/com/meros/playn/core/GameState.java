package com.meros.playn.core;

import java.util.HashMap;
import java.util.Map;

import playn.core.PlayN;

public class GameState {
	
	private static Map<String, String> myDataMap = new HashMap<String, String>();

	public static void clear() {
		myDataMap.clear();
		//TODO: clear storage!
	}

	public static int getInt(String aKey) {
		String item = myDataMap.get(aKey);
		if (item == null)
			return 0;

		return Integer.parseInt(item);
	}

	public static boolean isSavePresent() {
		return getInt("save_present") == 1;
	}

	public static void loadFromFile() {
		myDataMap.clear();
		for (String key : PlayN.storage().keys())
		{
			myDataMap.put(key, PlayN.storage().getItem(key));
		}
	}

	public static void put(String aKey, int aInt) {
		myDataMap.put(aKey, "" + aInt);
	}

	public static void saveToFile() {
		setSavePresent();
		
		for (String key : myDataMap.keySet())
		{
			PlayN.storage().setItem(key, myDataMap.get(key));
		}
	}

	public static void setSavePresent() {
		put("save_present", 1);
	}

}
