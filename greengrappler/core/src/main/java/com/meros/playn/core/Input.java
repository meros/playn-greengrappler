package com.meros.playn.core;

import java.util.HashSet;
import java.util.HashMap;

import com.meros.playn.core.Constants.Buttons;

import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
import playn.core.PlayN;

public class Input implements Keyboard.Listener {

	static HashSet<Buttons> mIsHeld = new HashSet<Buttons>();
	static HashSet<Buttons> mIsPressed = new HashSet<Buttons>();
	static HashSet<Buttons> mIsReleased = new HashSet<Buttons>();

	static HashMap<Key, Buttons> mKeyMap = new HashMap<Key, Buttons>();

	public static void init()
	{
		PlayN.keyboard().setListener(new Input());

		mKeyMap.put(Key.DOWN, Buttons.Down);
		mKeyMap.put(Key.UP, Buttons.Up);
		mKeyMap.put(Key.LEFT, Buttons.Left);
		mKeyMap.put(Key.RIGHT, Buttons.Right);
		mKeyMap.put(Key.ENTER, Buttons.Fire);
		mKeyMap.put(Key.Z, Buttons.Fire);
		mKeyMap.put(Key.ESCAPE, Buttons.ForceQuit);
		mKeyMap.put(Key.X, Buttons.Jump);
	}

	public static void update()
	{
		mIsPressed.clear();
		mIsReleased.clear();
	}

	public static boolean isHeld(Buttons aButton) {
		return mIsHeld.contains(aButton);
	}

	public static boolean isPressed(Buttons aButton) {
		return mIsPressed.contains(aButton);
	}

	public static boolean isReleased(Buttons aButton) {
		return mIsReleased.contains(aButton);
	}

	public static void onButtonDown(Buttons aButton)
	{
		mIsPressed.add(aButton);
		mIsHeld.add(aButton);
	}

	public static void onButtonUp(Buttons aButton)
	{
		mIsHeld.remove(aButton);
		mIsReleased.add(aButton);
	}

	@Override
	public void onKeyDown(Event event) {
		if (mKeyMap.containsKey(event.key()))
		{
			if (!mIsHeld.contains(mKeyMap.get(event.key())))
			{
				event.setPreventDefault(true);
				onButtonDown(mKeyMap.get(event.key()));
			}
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
			event.setPreventDefault(true);
			onButtonUp(mKeyMap.get(event.key()));
		}
	}

	public static void enable() {
		// TODO Auto-generated method stub

	}

	public static void disable() {
		// TODO Auto-generated method stub

	}
}
