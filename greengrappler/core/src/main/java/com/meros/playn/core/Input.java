package com.meros.playn.core;

import java.util.HashSet;
import java.util.HashMap;

import com.meros.playn.core.Constants.Buttons;

import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;

public class Input implements Keyboard.Listener {

	static HashMap<Buttons, Integer> mIsHeld = new HashMap<Buttons, Integer>();
	static HashSet<Buttons> mIsPressed = new HashSet<Buttons>();
	static HashSet<Buttons> mIsReleased = new HashSet<Buttons>();

	static HashMap<Key, Buttons> mKeyMap = new HashMap<Key, Buttons>();

	public static void init()
	{
		playn.core.PlayN.keyboard().setListener(new Input());

		mKeyMap.put(Key.DOWN, Buttons.Down);
		mKeyMap.put(Key.UP, Buttons.Up);
		mKeyMap.put(Key.LEFT, Buttons.Left);
		mKeyMap.put(Key.RIGHT, Buttons.Right);
	}

	public static void update()
	{
		mIsPressed.clear();
		mIsReleased.clear();
	}

	public static boolean isHeld(Buttons aButton) {
		if (!mIsHeld.containsKey(aButton))
			return false;

		return (mIsHeld.get(aButton) != 0);
	}

	public static boolean isPressed(Buttons aButton) {
		return mIsPressed.contains(aButton);
	}

	public static boolean isReleased(Buttons aButton) {
		return mIsPressed.contains(aButton);
	}

	public static void onButtonDown(Buttons aButton)
	{
		mIsPressed.add(aButton);
		if (!mIsHeld.containsKey(aButton))
		{
			mIsHeld.put(aButton, 0);
		}

		mIsHeld.put(aButton, mIsHeld.get(aButton) + 1);
	}

	public static void onButtonUp(Buttons aButton)
	{
		if (mIsHeld.containsKey(aButton))
		{
			mIsHeld.put(aButton, mIsHeld.get(aButton) - 1);
			if (mIsHeld.get(aButton) == 0)
			{
				mIsReleased.add(aButton);
			}
		}
	}

	@Override
	public void onKeyDown(Event event) {
		if (mKeyMap.containsKey(event.key()))
		{
			onButtonDown(mKeyMap.get(event.key()));
		}
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyUp(Event event) {
		if (mKeyMap.containsKey(event.key()))
		{
			onButtonUp(mKeyMap.get(event.key()));
		}
	}
}
